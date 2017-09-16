package com.camp.share.core.model.vo;

import com.camp.share.core.model.MenuDO;
import com.camp.share.core.model.UserDO;
import com.wordnik.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * Created by winstar on 2017/9/10.
 */
@Data
@ApiModel(value = "用户订单信息", description = "OrderVO")
public class OrderVO {

    private UserDO userDO;

    private MenuDO menuDO;

    private List<MenuDO> menuDOS;

}
