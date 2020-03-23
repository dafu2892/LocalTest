package com.localtest.model;

import lombok.Data;

@Data
public class UpdateUserInfoCase {

    private int id;
    private int user_id;
    private String user_name;
    private String user_age;
    private String user_sex;
    private String user_isDelete;
    private String expected;

}