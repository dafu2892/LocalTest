package com.localtest.model;

import lombok.Data;

@Data
public class GetUserInfoCase {

    private int id;
    private int user_id;
    private String expected;

}