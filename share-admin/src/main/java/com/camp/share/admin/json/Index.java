package com.camp.share.admin.json;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by winstar on 2017/9/7.
 */
@Controller
@Slf4j
public class Index {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String excute(HttpServletResponse response) {
        return "index";
    }
}
