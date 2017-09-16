package com.camp.share.admin.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camp.share.admin.biz.service.AutoOrderService;
import com.camp.share.core.dao.UserDao;
import com.camp.share.core.model.ConfigDO;
import com.camp.share.core.model.MenuDO;
import com.camp.share.core.model.dto.RestaurantDTO;
import com.camp.share.core.model.UserDO;
import com.camp.share.core.model.vo.OrderVO;
import com.camp.share.core.service.UserService;
import com.camp.share.core.util.DateUtil;
import com.camp.share.core.util.HttpClient;
import com.camp.share.core.util.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wushengsheng on 2017/6/11.
 */
@Slf4j
@Service
public class AutoOrderServiceImpl implements AutoOrderService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    // 获取美餐用户信息get
    private static final String getUserInfo = "https://meican.com/preorder/api/v2.1/accounts/show";

    // 获取从当天起一周的点餐信息get
    private static final String getOrderInfoByWeek = "https://meican.com/preorder/api/v2.1/calendarItems/list";

    // 获取点餐信息post
    private static final String getOrderInfo = "https://meican.com/preorder/api/v2.1/orders/show";

    // 获取所有可下单的餐馆信息get
    private static final String getAllRestaurantInfo = "https://meican.com/preorder/api/v2.1/restaurants/list";

    // 获取所有可下单的餐馆的菜单信息get
    private static final String getAllRestaurantMenuInfo = "https://meican.com/preorder/api/v2.1/restaurants/show";

    // 获取所有可下单的餐馆的菜单信息post
    private static final String addOrder = "https://meican.com/preorder/api/v2.1/orders/add";

    // 取消下单
    private static final String delOrder = "https://meican.com/preorder/api/v2.1/orders/delete";

    public List<UserDO> getAllUserInfo() {
        return userService.getAllUserInfo();
    }

    @Transactional
    public OrderVO getUserInfoByCookie(String cookie) {

        if (StringUtil.isEmpty(cookie)) {
            return null;
        }

        OrderVO orderVO = new OrderVO();
        // 获取美餐登录信息
        try {
            Map loginResult = HttpClient.get(getUserInfo, "", "remember=" + cookie);
            if (loginResult == null || loginResult.get("lines") == null) {
                return null;
            }

            JSONObject jsonObject = JSON.parseObject(loginResult.get("lines").toString());
            if (jsonObject == null || jsonObject.get("username") == null) {
                return null;
            }

            // 提取用户信息
            UserDO tmp = new UserDO();

            tmp.setUniqueid(jsonObject.getString("uniqueId"));
            tmp.setName(jsonObject.getString("username"));
            tmp.setEmail(jsonObject.getString("nameForShow"));
            tmp.setMobile(jsonObject.getJSONObject("mobileAccount").getString("mobileNumber"));
            tmp.setToken(cookie);
            tmp.setAddress("hangzhou");
            tmp.setCreateDate(new Date());
            UserDO userDO = userDao.getUserInfo(tmp);

            // 如果数据库不存在该用户则添加用户到数据库
            if (userDO == null || userDO.getUniqueid() == null) {
                userDao.addUserInfo(tmp);
            }

            MenuDO queryParam = new MenuDO();
            queryParam.setId(userDao.getUserInfo(tmp).getId());
            List<MenuDO> list = userDao.getUserOrder(queryParam);
            orderVO.setMenuDOS(list);

            // 查找用户当天的点餐信息按周查询
            String dateInfo = "beginDate=" + DateUtil.format(new Date(), DateUtil.DEFAULT_DATE) + "&endDate=" + DateUtil.format(DateUtil.getAfterDate(6, 0), DateUtil.DEFAULT_DATE);
//            String dateInfo = "beginDate=" + "2017-9-15" + "&endDate=" + DateUtil.format(DateUtil.getAfterDate(6, 0), DateUtil.DEFAULT_DATE);

            Map orderInfo = HttpClient.get(getOrderInfoByWeek, dateInfo, "remember=" + cookie);
            if (orderInfo == null || orderInfo.get("lines") == null) {
                orderVO.setUserDO(userDO);
                orderVO.setMenuDO(null);
                return orderVO;
            }

            JSONObject orderInfoObject = JSON.parseObject(orderInfo.get("lines").toString());

            JSONArray dateList = orderInfoObject.getJSONArray("dateList");
            JSONObject tmp1 = dateList.getJSONObject(0);
            JSONObject calendarItemList = tmp1.getJSONArray("calendarItemList").getJSONObject(0);
            JSONObject corpOrderUser = calendarItemList.getJSONObject("corpOrderUser");

            log.info("orderInfoObject: {}", JSON.toJSONString(orderInfoObject));

            if (corpOrderUser == null) {
                orderVO.setUserDO(userDO);
                orderVO.setMenuDO(null);
                return orderVO;
            }

            // 拼接查找订单信息
            Map params = Maps.newHashMap();
            params.put("uniqueId", corpOrderUser.get("uniqueId"));
            params.put("type", "CORP_ORDER");
            Map orderResult = HttpClient.post(getOrderInfo, StringUtil.urlAppend(params), "remember=" + cookie);

            if (orderResult == null || orderResult.get("lines") == null || orderResult.get("cookies") == null) {
                orderVO.setUserDO(userDO);
                orderVO.setMenuDO(null);
                return orderVO;
            }

            MenuDO menuDO = new MenuDO();

            JSONObject menuObject = JSONObject.parseObject(orderResult.get("lines").toString());
            JSONArray restaurantItemList = menuObject.getJSONArray("restaurantItemList");
            String restaurantName = restaurantItemList.getJSONObject(0).get("restaurantName").toString();
            JSONArray dishItemList = restaurantItemList.getJSONObject(0).getJSONArray("dishItemList");
            JSONObject dish = dishItemList.getJSONObject(0).getJSONObject("dish");
            JSONObject menuLocation = menuObject.getJSONObject("postbox");

            menuDO.setCorpRestaurant(restaurantName.toString());
            menuDO.setName(dish.getString("name"));
            menuDO.setRevisionId(corpOrderUser.get("uniqueId").toString());
            menuDO.setLocation(menuLocation.getString("postboxCode"));

            orderVO.setUserDO(userDO);
            orderVO.setMenuDO(menuDO);

        } catch (IOException e) {
            log.error("get userInfo error: {}", e.getMessage());
            e.printStackTrace();
        }

        return orderVO;
    }

    public boolean addMenuInfoByCookie(String cookie, MenuDO menuDO, String dateIndex) {

        // 拼接添加订单信息
        Map<String, String> params = Maps.newHashMap();
        // 大搜车地址
        params.put("corpAddressUniqueId", "e5606ba8d703");
        // 下单数量，菜谱id
        params.put("order", "[{\"count\":1,\"dishId\":"+ menuDO.getRevisionId() +"}]");
        params.put("tabUniqueId", "a70cf5fe-c875-4dd8-9dd1-ad0a7ae3d214");
        // 下单截止时间
        params.put("targetTime", DateUtil.format(new Date(), DateUtil.DEFAULT_DATE) + "+17:00");
        // 大搜车地址
        params.put("userAddressUniqueId", "e5606ba8d703");

        Date today = new Date();
        if (DateUtil.getWeekdayNumber(today) == Integer.parseInt(dateIndex)) {
            Map orderResult = null;
            try {
                orderResult = HttpClient.post(addOrder, StringUtil.urlAppend(params), "remember=" + cookie);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (orderResult == null || orderResult.get("lines") == null || orderResult.get("cookies") == null) {
                return false;
            }
        }

        UserDO userDO = new UserDO();
        userDO.setToken(cookie);
        UserDO tmp = userDao.getUserInfo(userDO);
        menuDO.setId(tmp.getId());
        menuDO.setWeekDate(Integer.parseInt(dateIndex));

        MenuDO menuTmp = new MenuDO();
        menuTmp.setId(tmp.getId());
        menuTmp.setWeekDate(Integer.parseInt(dateIndex));
        List<MenuDO> menuDOList = userDao.getUserOrder(menuTmp);
        for (MenuDO tmpDO : menuDOList) {
            if (tmpDO.getWeekDate() == Integer.parseInt(dateIndex)) {
                return userDao.updateUserOrder(menuDO) > 0;
            }
        }

        return userDao.addUserOrder(menuDO) > 0;
    }

    public boolean delUserOrder(String cookie, String dateIndex) {
        // 拼接取消订单信息
        Map<String, String> params = Maps.newHashMap();
        params.put("uniqueId", ""); // todo 待周一查看具体数据
        params.put("type", "CORP_ORDER");

        Date today = new Date();
        Map orderResult = null;
        if (DateUtil.getWeekdayNumber(today) == Integer.parseInt(dateIndex)) {
            try {
                orderResult = HttpClient.post(delOrder, StringUtil.urlAppend(params), "remember=" + cookie);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (orderResult == null || orderResult.get("lines") == null || orderResult.get("cookies") == null) {
                return false;
            }
        }

        UserDO userDO = new UserDO();
        userDO.setToken(cookie);
        UserDO tmp = userDao.getUserInfo(userDO);
        MenuDO menuDOTmp = new MenuDO();
        menuDOTmp.setId(tmp.getId());
        menuDOTmp.setWeekDate(Integer.parseInt(dateIndex));

        return userDao.delUserOrder(menuDOTmp) > 0;
    }

    public List<MenuDO> getAllMenuInfo() {

        List<MenuDO> menuDOList = Lists.newArrayList();
        ConfigDO queryConfig = new ConfigDO();
        queryConfig.setCode("menu_config");
        ConfigDO configTmp = userDao.getConfigInfo(queryConfig);
        if (!StringUtil.isEmpty(configTmp.getValue())) {
            String dateCreate = DateUtil.floorToStr(configTmp.getDateCreate());
            String dateNow = DateUtil.floorToStr(new Date());
            if (dateNow.compareTo(dateCreate) == 0) {
                menuDOList = JSONArray.parseArray(configTmp.getValue(), MenuDO.class);
            }
        }
        if (menuDOList != null && menuDOList.size() > 0) {
            return menuDOList;
        }

        String cookie = "2874ddd684eb29ba98300ef07e5404d07cffaca8-605578";
        Map loginResult = null;
//        String urlParams = "tabUniqueId=a70cf5fe-c875-4dd8-9dd1-ad0a7ae3d214&targetTime=" + DateUtil.format(new Date(), DateUtil.DEFAULT_DATE) + "+17:00";
        String urlParams = "tabUniqueId=a70cf5fe-c875-4dd8-9dd1-ad0a7ae3d214&targetTime=" + "2017-9-15" + "+17:00";

        try {
            loginResult = HttpClient.get(getAllRestaurantInfo, urlParams, "remember=" + cookie);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (loginResult == null || loginResult.get("lines") == null) {
            return menuDOList;
        }

        JSONObject jsonObject = JSON.parseObject(loginResult.get("lines").toString());
        if (jsonObject == null || jsonObject.get("restaurantList") == null) {
            return menuDOList;
        }

        JSONArray restaurantList = jsonObject.getJSONArray("restaurantList");
        List<RestaurantDTO> restaurantDTOS = restaurantList.toJavaList(RestaurantDTO.class);

        if (restaurantDTOS == null || restaurantDTOS.size() == 0) {
            return menuDOList;
        }

        Map<String, String> restaurantMap = Maps.newHashMap();
        for (RestaurantDTO restaurantDTO : restaurantDTOS) {
            restaurantMap.put(restaurantDTO.getUniqueId(), restaurantDTO.getName());
        }

        for (String restrurantId : restaurantMap.keySet()) {

            Map restrurantMenuInfo = null;
            String menuInfoUrlParam = "tabUniqueId=a70cf5fe-c875-4dd8-9dd1-ad0a7ae3d214&targetTime="+  DateUtil.format(new Date(), DateUtil.DEFAULT_DATE) + "+17:00" +"&restaurantUniqueId=" + restrurantId;
            try {
                restrurantMenuInfo = HttpClient.get(getAllRestaurantMenuInfo, menuInfoUrlParam, "remember=" + cookie);

                if (restrurantMenuInfo == null || restrurantMenuInfo.get("lines") == null) {
                    continue;
                }

                JSONObject restrurantMenuInfoJson = JSON.parseObject(restrurantMenuInfo.get("lines").toString());
                if (restrurantMenuInfoJson == null || restrurantMenuInfoJson.get("dishList") == null) {
                    continue;
                }

                JSONArray dishList = restrurantMenuInfoJson.getJSONArray("dishList");

                for (int i = 0; i < dishList.size(); i++) {
                    JSONObject tmp = dishList.getJSONObject(i);
                    MenuDO menuDO = new MenuDO();
                    menuDO.setCorpRestaurant(restaurantMap.get(restrurantId));
                    menuDO.setName(tmp.getString("name"));
                    menuDO.setRevisionId(tmp.getString("id"));
                    menuDOList.add(menuDO);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        ConfigDO configDO = new ConfigDO();
        configDO.setCode("menu_config");
        configDO.setValue(JSONArray.toJSONString(menuDOList));
        userDao.updateConfigInfo(configDO);

        return menuDOList;
    }
}
