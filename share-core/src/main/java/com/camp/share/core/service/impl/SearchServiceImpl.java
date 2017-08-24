package com.camp.share.core.service.impl;

import com.camp.share.core.dao.UserDao;
import com.camp.share.core.model.UserDO;
import com.camp.share.core.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wushengsheng on 2017/6/10.
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private UserDao userDao;

    public List<UserDO> getUserInfo() {
        return userDao.getUserInfo();
    }
}
