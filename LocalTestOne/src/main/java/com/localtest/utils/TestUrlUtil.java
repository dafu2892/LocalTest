package com.localtest.utils;

import com.localtest.model.InterfaceName;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * 拼接url的工具类
 * 工具类都会命名为静态方法，即不用new就可以使用
 */
public class TestUrlUtil {

    //用ResourceBundle对象就能轻松的将配置文件读取出来
    private static ResourceBundle bundle = ResourceBundle.getBundle("application", Locale.CHINA);

    //这里我们会用到model中创建的枚举类InterfaceName（即，传入的必须是这个类中写的，不能乱传）
    public static String getUrl(InterfaceName name){
        String address = bundle.getString("test.url");
        String uri = ""; //用来根据传入的枚举值判断要拼接的uri；
        String testUrl;
        if (name == InterfaceName.ADDUSER){
            uri =bundle.getString("addUser.uri");
        }

        if (name == InterfaceName.GETUSERINFO){
            uri =bundle.getString("getUserInfo.uri");
        }

        if (name == InterfaceName.GETUSERLIST){
            uri =bundle.getString("getUserList.uri");
        }

        if (name == InterfaceName.LOGIN){
            uri =bundle.getString("login.uri");
        }

        if (name == InterfaceName.UPDATEUSERINFO){
            uri =bundle.getString("updateUserInfo.uri");
        }

        testUrl = address + uri; //最终的测试地址（拼接后的地址）
        return testUrl;
    }

}