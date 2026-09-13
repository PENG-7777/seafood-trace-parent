package com.peng.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.peng.admin.dto.NodeSaveDTO;
import com.peng.admin.vo.NodeVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NodeInfoServiceImplTest {

    @Autowired
    private NodeInfoService nodeInfoService;

    /**
     * 分页查询测试，带省份筛选
     */
    @Test
    void testNodePageQuery() {
        IPage<NodeVO> page = nodeInfoService.getNodePage(1L, 10L, null, null, 19);
        assertNotNull(page);
        assertTrue(page.getTotal() > 0);
    }

    /**
     * 根据ID查询详情
     */
    @Test
    void testGetNodeDetail() {
        //数据库预置B001存在
        NodeVO vo = nodeInfoService.getNodeDetailById(1);
        assertNotNull(vo);
        assertEquals("捕捞企业", vo.getTypeName());
    }
}
