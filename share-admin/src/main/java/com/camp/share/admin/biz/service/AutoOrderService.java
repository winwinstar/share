package com.camp.share.admin.biz.service;

import com.camp.share.core.model.MenuDO;
import com.camp.share.core.model.UserDO;
import com.camp.share.core.model.vo.OrderVO;

import java.util.List;

/**
 * Created by wushengsheng on 2017/6/11.
 */
public interface AutoOrderService {

    /**
     * 获取所有用户的信息
     *
     * @return
     */
    List<UserDO> getAllUserInfo();

    /**
     * 通过cookie登录获取用户的信息
     *
     * @param cookie
     * @return
     */
    OrderVO getUserInfoByCookie(String cookie);

    /**
     * 用户下单
     *
     * @param cookie
     * @param menuDO
     * @return
     */
    boolean addMenuInfoByCookie(String cookie, MenuDO menuDO);

    /**
     * 获取所有餐馆的菜单信息
     *
     * @return
     */
    List<MenuDO> getAllMenuInfo();
}
