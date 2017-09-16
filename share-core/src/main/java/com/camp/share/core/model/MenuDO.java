package com.camp.share.core.model;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by winstar on 2017/9/10.
 */
@Data
@ApiModel(value = "菜单信息", description = "UserDO")
public class MenuDO {

    @ApiModelProperty(value = "店铺名")
    private String corpRestaurant;

    @ApiModelProperty(value = "菜谱名")
    private String name;

    @ApiModelProperty(value = "菜谱id")
    private String revisionId;

    @ApiModelProperty(value = "放置位置")
    private String location;

    @ApiModelProperty(value = "放置位置")
    private Date timestamp = new Date();
}
