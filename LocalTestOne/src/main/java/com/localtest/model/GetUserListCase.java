package com.localtest.model;

import lombok.Data;

@Data
public class GetUserListCase {

    private int id;
    private String user_name;
    private String user_age;
    private String user_sex;
    private String expected;

}