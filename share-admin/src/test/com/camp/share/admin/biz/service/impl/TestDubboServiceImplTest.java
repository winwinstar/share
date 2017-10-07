package com.camp.share.admin.biz.service.impl;

import com.camp.share.admin.biz.service.AutoOrderService;
import com.camp.share.admin.biz.service.impl.base.BaseSpringTest;
import com.camp.share.service.TestDubboService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by winstar on 2017/10/6.
 */
@Slf4j
public class TestDubboServiceImplTest  extends BaseSpringTest{

    @Autowired
    private TestDubboService testDubboService;

    @Autowired
    private AutoOrderService autoOrderService;

    @Test
    public void testTestDubbo() throws Exception {

        System.out.println(autoOrderService.getAllUserInfo());
//        testDubboService.testDubbo();
    }

}