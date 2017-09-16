package com.camp.share.core.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by winstar on 2017/9/17.
 */
@Data
public class ConfigDO {
    private int id;
    private String code;
    private String value;
    private Date dateCreate;
}
