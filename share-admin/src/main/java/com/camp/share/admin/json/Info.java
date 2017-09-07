package com.camp.share.admin.json;

import com.camp.share.admin.biz.service.UserService;
import com.camp.share.core.enhance.Result;
import com.camp.share.core.model.UserDO;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by wushengsheng on 2017/6/10.
 */
@Api(value = "index-api" , description = "后台首页api")
@Controller
@RequestMapping(value = "/info")
@Slf4j
@ResponseBody
public class Info {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getInitInfo", method = RequestMethod.GET)
    @ApiOperation(value = "获取首页初始化信息", httpMethod = "Get", notes = "初始化")
    public Result<List<UserDO>> getInitInfo() {
        List<UserDO> userDOS = userService.getUserInfo();
        return Result.success(userDOS);
    }
}
