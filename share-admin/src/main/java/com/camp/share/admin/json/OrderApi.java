package com.camp.share.admin.json;

import com.camp.share.admin.biz.service.AutoOrderService;
import com.camp.share.core.enhance.Result;
import com.camp.share.core.model.MenuDO;
import com.camp.share.core.model.UserDO;
import com.camp.share.core.model.vo.OrderVO;
import com.camp.share.core.util.StringUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by wushengsheng on 2017/9/10.
 */
@Api(value = "auto-order-api" , description = "自动点餐api")
@Controller
@RequestMapping(value = "/order")
@Slf4j
@ResponseBody
public class OrderApi {

    @Autowired
    private AutoOrderService autoOrderService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ApiOperation(value = "获取登录美餐信息", httpMethod = "Get", notes = "初始化登录")
    public Result getInitInfo(HttpServletRequest request, HttpServletResponse response, @ApiParam(name = "token", value = "美餐token", required = false) @Param(value = "token") String token) {
        String meiCanCookie = getCookieFromRequest(request);
        if (StringUtil.isEmpty(meiCanCookie)) {

            if (StringUtil.isEmpty(token)) {
                return Result.fail("-1", "您尚未获取过点餐信息哦！");
            }

            OrderVO orderVO = autoOrderService.getUserInfoByCookie(token);
            if (orderVO == null || orderVO.getUserDO() == null || orderVO.getUserDO().getToken() == null) {
                return Result.fail("-1", "login fail");
            }

            Cookie cookie = new Cookie("token", orderVO.getUserDO().getToken());
            cookie.setMaxAge(30 * 60 * 24 * 365);// 设置为30min
            cookie.setPath("/");
            response.addCookie(cookie);

            return Result.success(orderVO);
        }

        OrderVO orderVO = autoOrderService.getUserInfoByCookie(meiCanCookie);
        return Result.success(orderVO);
    }

    @RequestMapping(value = "/getAllMenuInfo", method = RequestMethod.GET)
    @ApiOperation(value = "获取所有可下单餐馆的菜单", httpMethod = "Get", notes = "用户下单")
    public Result<List<MenuDO>> getAllMenuInfo() {
        List<MenuDO> menuDOS = autoOrderService.getAllMenuInfo();
        return Result.success(menuDOS);
    }

    @RequestMapping(value = "/addOrder", method = RequestMethod.GET)
    @ApiOperation(value = "用户下单", httpMethod = "Get", notes = "用户下单")
    public Result addOrder(HttpServletRequest request, @ApiParam(name = "token", value = "美餐token", required = false) @Param(value = "token") String token,
                           @ApiParam(name = "dateIndex", value = "下单的日期", required = true) @Param(value = "dateIndex") String dateIndex,
                           @ApiParam(name = "revisionId", value = "菜谱id", required = true) @Param(value = "revisionId") String revisionId,
                           @ApiParam(name = "name", value = "菜名", required = true) @Param(value = "name") String name,
                           @ApiParam(name = "corpRestaurant", value = "餐馆名称", required = true) @Param(value = "corpRestaurant") String corpRestaurant) {
        String meiCanCookie = getCookieFromRequest(request);
        boolean isSccuss = false;
        MenuDO menuDO = new MenuDO();
        menuDO.setRevisionId(revisionId);
        menuDO.setName(name);
        menuDO.setCorpRestaurant(corpRestaurant);
        if (StringUtil.isAnyEmpty(dateIndex, menuDO.getCorpRestaurant(), menuDO.getName(), menuDO.getRevisionId())) {
            return Result.fail("-1", "下单失败");
        }
        if (StringUtil.isEmpty(meiCanCookie)) {

            if (StringUtil.isEmpty(token)) {
                return Result.fail("-1", "请先获取点餐信息吧！");
            }
            isSccuss = autoOrderService.addMenuInfoByCookie(token, menuDO, dateIndex);
            return Result.success(isSccuss);
        }
        isSccuss = autoOrderService.addMenuInfoByCookie(meiCanCookie, menuDO, dateIndex);
        return Result.success(isSccuss);
    }

    @RequestMapping(value = "/delOrder", method = RequestMethod.GET)
    @ApiOperation(value = "用户取消下单", httpMethod = "Get", notes = "取消下单")
    public Result delUserOrder(HttpServletRequest request, @ApiParam(name = "token", value = "美餐token", required = false) @Param(value = "token") String token,
                               @ApiParam(name = "dateIndex", value = "取消下单的日期", required = true) @Param(value = "dateIndex") String dateIndex) {
        String meiCanCookie = getCookieFromRequest(request);
        boolean isSuccess = false;
        if (StringUtil.isAnyEmpty(meiCanCookie, dateIndex)) {
            if (StringUtil.isEmpty(token)) {
                return Result.fail("-1", "请先获取点餐信息吧！");
            }
            isSuccess = autoOrderService.delUserOrder(meiCanCookie, dateIndex);
        }
        return Result.success(isSuccess);
    }

    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    @ApiOperation(value = "获取使用的用户信息", httpMethod = "Get", notes = "用户信息")
    public Result<List<UserDO>> getUserInfo(@ApiParam(name = "token", value = "美餐token", required = false) @Param(value = "token") String token) {
        List<UserDO> userDOS = autoOrderService.getAllUserInfo();
        return Result.success(userDOS);
    }

    private String getCookieFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String meiCanCookie = null;
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    meiCanCookie = cookie.getValue();
                    break;
                }
            }
        }
        return meiCanCookie;
    }
}
