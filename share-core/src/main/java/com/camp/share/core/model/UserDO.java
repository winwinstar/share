package com.camp.share.core.model;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by wushengsheng on 2017/6/10.
 */
@Data
@ApiModel(value = "用户基本信息", description = "UserDO")
public class UserDO {

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "年龄")
    private int age;

    @ApiModelProperty(value = "家庭地址")
    private String address;
}
