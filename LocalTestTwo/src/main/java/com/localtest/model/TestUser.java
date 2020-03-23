package com.localtest.model;

import lombok.Data;

@Data
public class TestUser {

    //这里的名称一定要与数据库字段的名称一致，否则获取不到数据
    private int id;
    private String user_name;
    private String user_password;
    private String user_age;
    private String user_sex;
    private String isDelete;

    //还需要加入json的处理，重写toString的方法；这样就可以变成json格式的字符串
    @Override
    public String toString(){
        return(
                "{" +
                        "id:" + id + "," +
                        "userName:" + user_name + "," +
                        "userPassword:" + user_password + "," +
                        "userAge:" + user_age + "," +
                        "userSex:" + user_sex + "," +
                        "isDelete:" + isDelete +
                        "}"
        );
    }

}
