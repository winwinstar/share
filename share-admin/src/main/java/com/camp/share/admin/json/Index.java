package com.camp.share.admin.json;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by winstar on 2017/9/7.
 */
@Controller
@RequestMapping(value = "/index")
@Slf4j
public class Index {

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public String excute() {
        return "index";
    }
}
