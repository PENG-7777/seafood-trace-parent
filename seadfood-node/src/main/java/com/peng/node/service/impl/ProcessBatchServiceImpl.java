package com.peng.node.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.peng.node.entity.FarmSeaBatch;
import com.peng.node.entity.FishBatch;
import com.peng.node.entity.NodeInfo;
import com.peng.node.entity.ProcessBatch;
import com.peng.node.entity.WholBatch;
import com.peng.node.mapper.FarmSeaBatchMapper;
import com.peng.node.mapper.FishBatchMapper;
import com.peng.node.mapper.NodeInfoMapper;
import com.peng.node.mapper.ProcessBatchMapper;
import com.peng.node.mapper.WholBatchMapper;
import com.peng.node.service.ProcessBatchService;
import com.peng.node.vo.SourceBatchVO;
import com.peng.node.vo.WholApplyVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 冷冻加工企业批号业务实现类
 * 完整上游链路：捕捞企业(1)/海水养殖企业(2) → 冷冻加工企业(3)
 * 上游数据表：fish_batch（捕捞）、farm_sea_batch（海水养殖）
 */
@Service
public class ProcessBatchServiceImpl extends ServiceImpl<ProcessBatchMapper, ProcessBatch> implements ProcessBatchService {

    /**
     * 批发商批号Mapper，用于查询、更新批发商批号状态
     */
    @Resource
    private WholBatchMapper wholBatchMapper;

    /**
     * 捕捞批号Mapper：fish_batch，校验上游捕捞原料状态
     */
    @Resource
    private FishBatchMapper fishBatchMapper;

    /**
     * 海水养殖批号Mapper：farm_sea_batch，校验海水养殖原料状态
     */
    @Resource
    private FarmSeaBatchMapper farmSeaBatchMapper;

    /**
     * 企业节点Mapper：根据企业id获取企业类型，区分捕捞 / 海水养殖
     */
    @Resource
    private NodeInfoMapper nodeInfoMapper;

    /**
     * 查询当前冷冻加工企业自己的批号列表
     * @param nodeId 当前登录冷冻加工企业id
     * @return 加工批号集合
     */
    @Override
    public List<ProcessBatch> getMyBatchList(Integer nodeId) {
        QueryWrapper<ProcessBatch> wrapper = new QueryWrapper<>();
        wrapper.eq("node_id", nodeId);
        return this.list(wrapper);
    }

    /**
     * 新增冷冻加工批号
     * 上游原料校验：fish_batch（捕捞） / farm_sea_batch（海水养殖），状态必须state=2【已发布】
     * @param processBatch 加工批号表单实体
     * @param nodeId 当前登录企业id
     */
    @Override
    public void addBatch(ProcessBatch processBatch, Integer nodeId) {
        // 获取前端提交的上游来源类型、上游原料批号主键
        Integer sourceNodeType = processBatch.getSourceNodeType();
        Integer sourceBatchId = processBatch.getSourceBatchId();

        // 如果选择了上游原料，则执行合法性校验
        if (sourceNodeType != null && sourceBatchId != null) {
            // 上游类型1：捕捞企业原料，对应表 fish_batch
            if (sourceNodeType == 1) {
                FishBatch fishBatch = fishBatchMapper.selectById(sourceBatchId);
                if (fishBatch == null) {
                    throw new RuntimeException("上游捕捞原料批号不存在");
                }
                // 校验原料状态必须是已发布，下游才可以选用
                if (!fishBatch.getState().equals(2)) {
                    throw new RuntimeException("上游捕捞原料批号未发布，不可作为加工原料");
                }
            }
            // 上游类型2：海水养殖企业原料，对应表 farm_sea_batch
            else if (sourceNodeType == 2) {
                FarmSeaBatch farmSeaBatch = farmSeaBatchMapper.selectById(sourceBatchId);
                if (farmSeaBatch == null) {
                    throw new RuntimeException("上游海水养殖原料批号不存在");
                }
                if (!farmSeaBatch.getState().equals(2)) {
                    throw new RuntimeException("上游海水养殖原料批号未发布，不可作为加工原料");
                }
            } else {
                throw new RuntimeException("上游企业类型非法，仅支持捕捞、海水养殖企业");
            }
        }

        // 强制设置所属加工企业id，防止前端篡改
        processBatch.setNodeId(nodeId);
        // 默认状态：1‑待发布
        if (processBatch.getState() == null) {
            processBatch.setState(1);
        }
        this.save(processBatch);
    }

    /**
     * 更新冷冻加工批号；仅【待发布】状态允许更新
     * 修改上游原料时重新校验 fish_batch / farm_sea_batch 批号有效性
     * @param processBatch 表单实体
     * @param nodeId 当前登录企业id
     */
    @Override
    public void updateBatch(ProcessBatch processBatch, Integer nodeId) {
        ProcessBatch dbBatch = this.getById(processBatch.getPbId());
        if (dbBatch == null) {
            throw new RuntimeException("加工批号不存在");
        }
        // 数据权限校验：只能操作本企业批号
        if (!dbBatch.getNodeId().equals(nodeId)) {
            throw new RuntimeException("无权操作该批号");
        }
        // 只有待发布允许修改
        if (!dbBatch.getState().equals(1)) {
            throw new RuntimeException("只有待发布状态批号可以修改");
        }

        // 修改上游原料时，重新校验上游批号状态
        Integer sourceNodeType = processBatch.getSourceNodeType();
        Integer sourceBatchId = processBatch.getSourceBatchId();
        if (sourceNodeType != null && sourceBatchId != null) {
            if (sourceNodeType == 1) {
                FishBatch fishBatch = fishBatchMapper.selectById(sourceBatchId);
                if (fishBatch == null) {
                    throw new RuntimeException("上游捕捞原料批号不存在");
                }
                if (!fishBatch.getState().equals(2)) {
                    throw new RuntimeException("上游捕捞原料批号未发布，不可作为加工原料");
                }
            } else if (sourceNodeType == 2) {
                FarmSeaBatch farmSeaBatch = farmSeaBatchMapper.selectById(sourceBatchId);
                if (farmSeaBatch == null) {
                    throw new RuntimeException("上游海水养殖原料批号不存在");
                }
                if (!farmSeaBatch.getState().equals(2)) {
                    throw new RuntimeException("上游海水养殖原料批号未发布，不可作为加工原料");
                }
            } else {
                throw new RuntimeException("上游企业类型非法，仅支持捕捞、海水养殖企业");
            }
        }

        processBatch.setNodeId(nodeId);
        this.updateById(processBatch);
    }

    /**
     * 删除批号；仅【待发布】状态允许删除
     * @param pbId 加工批号主键
     * @param nodeId 当前登录企业id
     */
    @Override
    public void deleteBatch(Integer pbId, Integer nodeId) {
        ProcessBatch dbBatch = this.getById(pbId);
        if (dbBatch == null) {
            throw new RuntimeException("加工批号不存在");
        }
        if (!dbBatch.getNodeId().equals(nodeId)) {
            throw new RuntimeException("无权操作该批号");
        }
        if (!dbBatch.getState().equals(1)) {
            throw new RuntimeException("只有待发布状态批号可以删除");
        }
        this.removeById(pbId);
    }

    /**
     * 批号下架：修改状态为4‑已下架
     * @param pbId 加工批号主键
     * @param nodeId 当前登录企业id
     */
    @Override
    public void offShelve(Integer pbId, Integer nodeId) {
        ProcessBatch dbBatch = this.getById(pbId);
        if (dbBatch == null) {
            throw new RuntimeException("加工批号不存在");
        }
        if (!dbBatch.getNodeId().equals(nodeId)) {
            throw new RuntimeException("无权操作该批号");
        }
        dbBatch.setState(4);
        this.updateById(dbBatch);
    }

    /**
     * 向上游源头（捕捞/海水养殖）发送进场确认请求，状态修改为2‑待确认
     * @param pbId 加工批号主键
     * @param nodeId 当前登录冷冻加工企业id
     */
    @Override
    public void sendConfirmRequest(Integer pbId, Integer nodeId) {
        ProcessBatch dbBatch = this.getById(pbId);
        if (dbBatch == null) {
            throw new RuntimeException("加工批号不存在");
        }
        if (!dbBatch.getNodeId().equals(nodeId)) {
            throw new RuntimeException("无权操作该批号");
        }
        // 只有待发布才可以向上游发起确认请求
        if (!dbBatch.getState().equals(1)) {
            throw new RuntimeException("只有待发布批号才可以发起进场确认请求");
        }
        // 修改状态为待确认，等待上游捕捞/海水养殖企业确认
        dbBatch.setState(2);
        this.updateById(dbBatch);
    }

    /**
     * 冷冻加工企业确认批发商进场请求
     * 事务：更新whol_batch批发商批号状态为已确认
     * @param wholBatchId 批发商批号主键
     * @param nodeId 当前登录冷冻加工企业id（权限校验）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmWholBatch(Integer wholBatchId, Integer nodeId) {
        // 1. 查询批发商批号记录
        WholBatch wholBatch = wholBatchMapper.selectById(wholBatchId);
        if (wholBatch == null) {
            throw new RuntimeException("批发商批号不存在");
        }

        // 2. 获取批发商批号关联的上游冷冻加工批号主键
        Integer sourceProcessPbId = wholBatch.getSourceBatchId();
        ProcessBatch sourceProcessBatch = this.getById(sourceProcessPbId);
        if (sourceProcessBatch == null) {
            throw new RuntimeException("上游冷冻加工原料批号不存在");
        }

        // 3. 权限校验：该上游加工批号必须属于当前登录冷冻加工企业
        if (!sourceProcessBatch.getNodeId().equals(nodeId)) {
            throw new RuntimeException("无权确认非本企业原料的批发商批号");
        }

        // 4. 校验上游加工批号必须为【3‑已确认】，原料本身必须已经经过源头确认
        if (!sourceProcessBatch.getState().equals(3)) {
            throw new RuntimeException("上游冷冻加工原料批号尚未确认，无法确认批发商进场");
        }

        // 5. 批发商批号必须处于【2‑待确认】状态，才允许确认
        if (!wholBatch.getState().equals(2)) {
            throw new RuntimeException("仅可确认状态为待确认的批发商批号");
        }

        // 6. 修改批发商批号状态为3‑已确认，可以继续向下游零售商流转
        wholBatch.setState(3);
        wholBatchMapper.updateById(wholBatch);
    }

    /**
     * 根据上游企业ID，查询该企业【已发布state=2】的原料批号下拉数据
     * 前端联动下拉：选中上游企业后加载可选原料批号
     * type=1 → fish_batch 捕捞
     * type=2 → farm_sea_batch 海水养殖
     * @param sourceNodeId 上游捕捞/海水养殖企业编号
     * @return 上游原料批号VO列表
     */
    @Override
    public List<SourceBatchVO> getUpstreamBatchByNodeId(Integer sourceNodeId) {
        // 根据企业ID查询企业信息，判断是捕捞还是海水养殖企业
        NodeInfo nodeInfo = nodeInfoMapper.selectById(sourceNodeId);
        if (nodeInfo == null) {
            throw new RuntimeException("所选上游企业不存在");
        }
        Integer upstreamType = nodeInfo.getType();
        List<SourceBatchVO> voResultList = new ArrayList<>();

        // 上游企业类型1：捕捞企业，查询 fish_batch 表中已发布批号
        if (upstreamType == 1) {
            QueryWrapper<FishBatch> fishQuery = new QueryWrapper<>();
            fishQuery.eq("node_id", sourceNodeId);
            fishQuery.eq("state", 2);
            List<FishBatch> fishBatchList = fishBatchMapper.selectList(fishQuery);
            for (FishBatch fishBatch : fishBatchList) {
                SourceBatchVO vo = new SourceBatchVO();
                vo.setSourceBatchId(fishBatch.getFbId());
                vo.setBatchId(fishBatch.getBatchId());
                vo.setType(fishBatch.getType());
                voResultList.add(vo);
            }
        }
        // 上游企业类型2：海水养殖企业，查询 farm_sea_batch 表中已发布批号
        else if (upstreamType == 2) {
            QueryWrapper<FarmSeaBatch> seaQuery = new QueryWrapper<>();
            seaQuery.eq("node_id", sourceNodeId);
            seaQuery.eq("state", 2);
            List<FarmSeaBatch> seaBatchList = farmSeaBatchMapper.selectList(seaQuery);
            for (FarmSeaBatch seaBatch : seaBatchList) {
                SourceBatchVO vo = new SourceBatchVO();
                vo.setSourceBatchId(seaBatch.getFsbId());
                vo.setBatchId(seaBatch.getBatchId());
                vo.setType(seaBatch.getType());
                voResultList.add(vo);
            }
        } else {
            throw new RuntimeException("该企业不是合法上游源头企业（仅捕捞/海水养殖）");
        }
        return voResultList;
    }

    @Override
    public List<WholApplyVO> getWholApplyList(Integer nodeId) {
        List<WholApplyVO> result = new ArrayList<>();
        QueryWrapper<WholBatch> wholWrapper = new QueryWrapper<>();
        // 查询全部批发商批号
        List<WholBatch> allWholList = wholBatchMapper.selectList(wholWrapper);

        for (WholBatch wholBatch : allWholList) {
            // 通过sourceBatchId拿到上游加工批号
            Integer processBatchId = wholBatch.getSourceBatchId();
            ProcessBatch processBatch = this.getById(processBatchId);
            // 过滤：上游加工批号所属企业 = 当前登录加工企业
            if (processBatch == null || !nodeId.equals(processBatch.getNodeId())) {
                continue;
            }

            WholApplyVO vo = new WholApplyVO();
            // 主键是 wbId
            vo.setWholBatchId(wholBatch.getWbId());
            vo.setProcessBatchId(processBatchId);
            vo.setApplyState(wholBatch.getState());
            // 没有createTime，时间字段置为 "-"；后续如果数据库增加录入时间再替换
            vo.setApplyTime("-");

            // 查询批发企业名称
            NodeInfo wholesaler = nodeInfoMapper.selectById(wholBatch.getNodeId());
            if (wholesaler != null) {
                vo.setWholNodeName(wholesaler.getName());
            }
            result.add(vo);
        }
        return result;
    }

}
