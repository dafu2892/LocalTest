package com.localtest.model;

import lombok.Data;

@Data
public class AddUserCase {

    private int id;
    private String user_name;
    private String user_password;
    private String user_age;
    private String user_sex;
    private String isDelete;
    private String expected;

}