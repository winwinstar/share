package com.camp.share.admin.biz.service.impl;

import com.camp.share.admin.biz.service.UserService;
import com.camp.share.core.model.UserDO;
import com.camp.share.core.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wushengsheng on 2017/6/11.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SearchService searchService;

    public List<UserDO> getUserInfo() {
        return searchService.getUserInfo();
    }
}
