package com.camp.share.core.dao;

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
}
