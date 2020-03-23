package com.localtest.controller;

import com.localtest.model.TestUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Objects;


@Log4j2
@RestController
@Api(value = "v1", description = "本地测试")
@RequestMapping("/v1")
public class LocalTestAPI {
    /**
     * 以下写接口方法
     */


    //访问数据库的对象
    @Autowired
    private SqlSessionTemplate template;


    //登录的接口；
    // 因为要返回cookies，所以要HttpServletResponse response；
    //@RequestBody TestUser testUser 是请求参数
    @ApiOperation(value = "登录接口", httpMethod = "POST")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Boolean login(HttpServletResponse response, @RequestBody TestUser testUser){
        int i = template.selectOne("login", testUser);
        //返回cookies
        Cookie cookie = new Cookie("login","true");
        response.addCookie(cookie);
        log.info("查询到的结果是:" +i);
        if (i != 0){
            log.info("登录用户是："+ testUser.getUser_name());
            return true;
        }
        return false;
    }


    //添加用户接口
    // 因为要使用cookies，所以要HttpServletRequest request；
    @ApiOperation(value = "添加用户接口", httpMethod = "POST")
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public boolean addUser(HttpServletRequest request, @RequestBody TestUser testUser){
        Boolean x = verifyCookies(request);
        int result = 0;

        if (x != null){
            result = template.insert("addUser", testUser);
        }

        if (result > 0){
            log.info("添加用户的数量是" + result);
            return true;
        }

        return false;
    }


    @ApiOperation(value = "获取用户信息接口", httpMethod = "POST")
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    public List<TestUser> getUserInfo(HttpServletRequest request, @RequestBody TestUser testUser){
        Boolean x = verifyCookies(request);
        System.out.println(x);
        if (x == true){
            List<TestUser> users = template.selectList("getUserInfo", testUser);
            log.info("getUserInfo获取到的用户数量是" + users.size());
            log.info(users.toString());
            return users;
        }else {
            return null;
        }
    }

    @ApiOperation(value = "更新/删除用户接口", httpMethod = "POST")
    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    public int updateUserInfo(HttpServletRequest request, @RequestBody TestUser testUser){
        Boolean x = verifyCookies(request);
        int i = 0;
        if (x == true){
            i = template.update("updateUserInfo", testUser);
        }
        log.info("更新数据的条数为：" + i);
        return i;
    }


    //因为很多接口都要验证cookies，所以单独写一个方法
    private Boolean verifyCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies)){
            log.info("cookies为空");
            return false;
        }

        for (Cookie cookie : cookies){
            if (cookie.getName().equals("login") && cookie.getValue().equals("true")){
                log.info("cookies验证通过");
                return true;
            }
        }

        return false;
    }


}
