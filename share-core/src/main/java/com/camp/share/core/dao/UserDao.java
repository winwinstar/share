package com.camp.share.core.dao;

import com.camp.share.core.model.UserDO;

import java.util.List;

/**
 * Created by wushengsheng on 2017/6/10.
 */
public interface UserDao {

    /**
     * 获取所有用户信息
     * @return
     */
    List<UserDO> getUserInfo();
}
