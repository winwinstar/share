package com.camp.share.admin.json;

import com.camp.share.core.util.HttpClient;
import com.wordnik.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by winstar on 2017/9/20.
 */
@Api(value = "test-api" , description = "测试")
@Controller
@Slf4j
@ResponseBody
public class Test {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void excute(HttpServletResponse response) {
        try {
            Map htmlBuffer = HttpClient.get("https://agreement.souche.com/agreement/100.html", "", "");
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println(htmlBuffer.get("lines"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
