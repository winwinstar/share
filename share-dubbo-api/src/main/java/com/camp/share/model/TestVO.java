package com.camp.share.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by winstar on 2017/10/6.
 */
@Data
public class TestVO implements Serializable {

    private static final long serialVersionUID = 8989317095963188519L;

    private String name;

    private int age;
}
