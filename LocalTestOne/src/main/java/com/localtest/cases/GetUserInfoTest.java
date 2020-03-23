package com.localtest.cases;

import com.localtest.config.TestConfig;
import com.localtest.model.GetUserInfoCase;
import com.localtest.model.TestUser;
import com.localtest.utils.DatabaseUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

public class GetUserInfoTest {

    @Test(dependsOnGroups = "loginTrue", description = "获取user_id为1的用户信息")
    public void getUserInfo() throws IOException {
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        GetUserInfoCase getUserInfoCase = sqlSession.selectOne("getUserInfoCase", 1);

        System.out.println(getUserInfoCase.toString());
        System.out.println(TestConfig.getUserInfoUrl);

        //发送请求
        JSONArray result = getJsonResult(getUserInfoCase);




        //验证返回结果
        TestUser testUser = sqlSession.selectOne(getUserInfoCase.getExpected(), getUserInfoCase);
        //把查询到的testUser放到list中，只有放到list中才能转换为jsonArray
        List userList = new ArrayList();
        userList.add(testUser);
        JSONArray testUserJsonArray = new JSONArray(userList);

        Assert.assertEquals(result.getJSONObject(0).get("user_name"),testUserJsonArray.getJSONObject(0).get("user_name"));


    }

    private JSONArray getJsonResult(GetUserInfoCase getUserInfoCase) throws IOException {
        //声明一个post方法
        HttpPost httpPost = new HttpPost(TestConfig.getUserInfoUrl);

        //添加参数 参数是JSONObject格式的
        JSONObject param = new JSONObject();
        param.put("id", getUserInfoCase.getUser_id());

        //设置请求头
        httpPost.setHeader("content-type","application/json");
        //将参数信息添加到方法中
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        //设置参数
        httpPost.setEntity(entity);

        //添加cookies
        TestConfig.closeableHttpClient = HttpClients.custom().setDefaultCookieStore(TestConfig.cookieStore).build();

        //发送请求
        CloseableHttpResponse response = TestConfig.closeableHttpClient.execute(httpPost);

        //获取整个响应里的全体内容，并将其转换为字符串
        String result;
        result = EntityUtils.toString(response.getEntity(),"utf-8");


        //先转换为list，再转换为JSONArray
        //List resultList = Arrays.asList(result);
        //JSONArray resultArray = new JSONArray(resultList);
        JSONArray resultArray = new JSONArray(result);


        response.close();
        TestConfig.closeableHttpClient.close();

        return resultArray;
    }
}