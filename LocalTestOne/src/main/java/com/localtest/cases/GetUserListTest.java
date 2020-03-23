package com.localtest.cases;

import com.localtest.config.TestConfig;
import com.localtest.model.GetUserInfoCase;
import com.localtest.model.GetUserListCase;
import com.localtest.model.TestUser;
import com.localtest.utils.DatabaseUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class GetUserListTest {
    @Test(dependsOnGroups = "loginTrue", description = "获取性别为男的用户信息")
    public void getUserList() throws IOException {
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        GetUserListCase getUserListCase = sqlSession.selectOne("getUserListCase", 1);

        System.out.println(getUserListCase.toString());
        System.out.println(TestConfig.getUserInfoUrl);


        //发送请求，获取结果
        JSONArray resultJson = getJsonResult(getUserListCase);


        //验证结果
        List<TestUser> userList = sqlSession.selectList("getUserList",getUserListCase);

        for (TestUser testUser : userList) {
            System.out.println("获取到user: " + testUser.toString());
        }

        //把userList转成JSONArray
        JSONArray userListJson = new JSONArray(userList);

        Assert.assertEquals(userListJson.length(), resultJson.length());

        /*for (int i = 0; i < resultJson.length(); i++) {
            JSONObject expect = (JSONObject) userListJson.get(i);
            JSONObject actual = (JSONObject) resultJson.get(i);

            Assert.assertEquals(expect.toString(), actual.toString());
        }*/



    }

    private JSONArray getJsonResult(GetUserListCase getUserListCase) throws IOException {
        HttpPost httpPost = new HttpPost(TestConfig.getUserInfoUrl);
        JSONObject param = new JSONObject();
        param.put("user_name", getUserListCase.getUser_name());
        param.put("user_sex", getUserListCase.getUser_sex());
        param.put("user_age", getUserListCase.getUser_age());

        httpPost.setHeader("content-type", "application/json");

        //将请求信息添加到方法中
        StringEntity entity = new StringEntity(param.toString(), "utf-8");
        httpPost.setEntity(entity);

        String result;

        TestConfig.closeableHttpClient = HttpClients.custom().setDefaultCookieStore(TestConfig.cookieStore).build();
        CloseableHttpResponse response = TestConfig.closeableHttpClient.execute(httpPost);

        result = EntityUtils.toString(response.getEntity(), "utf-8");


        JSONArray jsonArray = new JSONArray(result);

        List<Cookie> cookieList = TestConfig.cookieStore.getCookies();
        for (Cookie cookie : cookieList) {
            String name = cookie.getName();
            String value = cookie.getValue();
            System.out.println("name=" + name + ", value=" + value);
        }

        response.close();
        TestConfig.closeableHttpClient.close();

        return jsonArray;
    }
}