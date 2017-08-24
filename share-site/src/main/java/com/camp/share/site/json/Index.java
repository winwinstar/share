package com.camp.share.site.json;

import com.camp.share.core.enhance.Result;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by wushengsheng on 2017/6/20.
 */
@Api(value = "index-api" , description = "活动首页api")
@Controller
@RequestMapping(value = "/index")
@Slf4j
@ResponseBody
public class Index {

    @RequestMapping(value = "/getIndexShop", method = RequestMethod.GET)
    @ApiOperation(value = "获取首页店铺", httpMethod = "Get", notes = "初始化")
    public Result<String> getIndexShop() {
        return Result.success("success");
    }
}
