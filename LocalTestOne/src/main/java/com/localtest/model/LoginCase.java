package com.localtest.model;

import lombok.Data;

@Data
public class LoginCase {

    //这里的名称一定要与数据库字段的名称一致，否则获取不到数据
    private int id;
    private String user_name;
    private String user_password;
    private String expected;

}