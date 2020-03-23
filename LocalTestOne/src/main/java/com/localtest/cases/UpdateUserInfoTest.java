package com.localtest.cases;

import com.localtest.config.TestConfig;
import com.localtest.model.TestUser;
import com.localtest.model.UpdateUserInfoCase;
import com.localtest.utils.DatabaseUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class UpdateUserInfoTest {

    @Test(dependsOnGroups = "loginTrue", description = "更改用户信息")
    public void updateUserInfo() throws IOException {
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        UpdateUserInfoCase updateUserInfoCase = sqlSession.selectOne("updateUserInfoCase",1);

        System.out.println(updateUserInfoCase.toString());
        System.out.println(TestConfig.updateUserInfoUrl);


        int result = getResult(updateUserInfoCase);

        TestUser testUser = sqlSession.selectOne(updateUserInfoCase.getExpected(),updateUserInfoCase);

        Assert.assertNotNull(testUser);
        Assert.assertNotNull(result);
    }



    @Test(dependsOnGroups = "loginTrue", description = "删除用户信息")
    public void deleteUser() throws IOException {
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        UpdateUserInfoCase updateUserInfoCase = sqlSession.selectOne("updateUserInfoCase",2);

        System.out.println(updateUserInfoCase.toString());
        System.out.println(TestConfig.updateUserInfoUrl);

        int result = getResult(updateUserInfoCase);

        TestUser testUser = sqlSession.selectOne(updateUserInfoCase.getExpected(),updateUserInfoCase);

        Assert.assertNotNull(testUser);
        Assert.assertNotNull(result);
    }

    private int getResult(UpdateUserInfoCase updateUserInfoCase) throws IOException {
        HttpPost httpPost = new HttpPost(TestConfig.updateUserInfoUrl);
        JSONObject param = new JSONObject();
        param.put("id", updateUserInfoCase.getUser_id());
        param.put("user_name", updateUserInfoCase.getUser_name());
        param.put("user_sex", updateUserInfoCase.getUser_sex());
        param.put("user_age", updateUserInfoCase.getUser_age());
        param.put("isDelete", updateUserInfoCase.getUser_isDelete());

        httpPost.setHeader("content-type","application/json");

        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        httpPost.setEntity(entity);

        String result;

        TestConfig.closeableHttpClient = HttpClients.custom().setDefaultCookieStore(TestConfig.cookieStore).build();
        CloseableHttpResponse response = TestConfig.closeableHttpClient.execute(httpPost);

        result = EntityUtils.toString(response.getEntity(), "utf-8");

        return Integer.parseInt(result);
    }
}