package com.camp.share.core.dao;

import com.camp.share.core.model.ConfigDO;
import com.camp.share.core.model.MenuDO;
import com.camp.share.core.model.UserDO;

import java.util.List;

/**
 * Created by wushengsheng on 2017/6/10.
 */
public interface UserDao {

    /**
     * 获取所有用户信息
     *
     * @return
     */
    List<UserDO> getAllUserInfo();

    /**
     * 获取用户信息
     *
     * @param userDO
     * @return
     */
    UserDO getUserInfo(UserDO userDO);

    /**
     * 添加用户
     *
     * @param userDO
     * @return
     */
    int addUserInfo(UserDO userDO);

    /**
     * 更新用户的信息（自动点餐的订单信息）
     *
     * @param menuDO
     * @return
     */
    int addUserOrder(MenuDO menuDO);

    /**
     * 更新用户的信息（自动点餐的订单信息）
     *
     * @param menuDO
     * @return
     */
    int updateUserOrder(MenuDO menuDO);

    /**
     * 获取用户下单信息
     *
     * @param menuDO
     * @return
     */
    List<MenuDO> getUserOrder(MenuDO menuDO);

    /**
     * 删除用户下单信息
     *
     * @param menuDO
     * @return
     */
    int delUserOrder(MenuDO menuDO);

    /**
     * 更新配置信息
     *
     * @param configDO
     * @return
     */
    int updateConfigInfo(ConfigDO configDO);

    /**
     * 查找配置信息
     *
     * @param configDO
     * @return
     */
    ConfigDO getConfigInfo(ConfigDO configDO);
}
