package com.camp.share.core.model;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by wushengsheng on 2017/6/10.
 */
@Data
@ApiModel(value = "用户基本信息", description = "UserDO")
public class UserDO {

    @ApiModelProperty(value = "美餐用户uid")
    private String uniqueid;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "年龄")
    private int age;

    @ApiModelProperty(value = "家庭地址")
    private String address;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "美餐token")
    private String token;

    private Date createDate;
}
