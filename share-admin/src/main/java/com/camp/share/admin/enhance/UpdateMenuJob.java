package com.camp.share.admin.enhance;

import com.camp.share.admin.biz.service.AutoOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Created by winstar on 2017/9/19.
 */
public class UpdateMenuJob implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private AutoOrderService autoOrderService;

    public void onApplicationEvent(ContextRefreshedEvent event) {
        autoOrderService.getAllMenuInfoJob();
    }
}