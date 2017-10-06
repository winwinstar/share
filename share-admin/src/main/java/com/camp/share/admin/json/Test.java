package com.camp.share.admin.json;

import com.camp.share.core.util.HttpClient;
import com.wordnik.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
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

    private JdbcTemplate jdbcTemplate;

    //注入方法2
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @RequestMapping(value = "/transTest", method = RequestMethod.GET)
    public void trans() {
        jdbcTemplate.execute("INSERT INTO test (name) VALUES ('wss')");
        ((Test)AopContext.currentProxy()).test();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void test() {
        jdbcTemplate.execute("INSERT INTO test2 (name) VALUES ('wss')");
        throw new RuntimeException("子方法异常出现。。。");
    }
}
