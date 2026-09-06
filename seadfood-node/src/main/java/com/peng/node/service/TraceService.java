package com.peng.node.service;

import com.peng.node.vo.TraceVO;

/**
 * 消费者溯源查询服务接口
 */
public interface TraceService {

    /**
     * 根据溯源编号查询完整溯源链路
     * @param sourceId 32位大写UUID溯源码
     * @return TraceVO 全链路数据
     */
    TraceVO getTraceInfoBySourceId(String sourceId);
}
