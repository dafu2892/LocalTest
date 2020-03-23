package com.localtest.cases;

import com.localtest.config.TestConfig;
import com.localtest.model.InterfaceName;
import com.localtest.model.LoginCase;
import com.localtest.utils.DatabaseUtil;
import com.localtest.utils.TestUrlUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class LoginTest {

    //private static CookieStore cookieStore;

    //（1）因为我们要跨文件来进行执行，所以涉及到组依赖
    @BeforeTest(groups = "loginTrue", description = "测试准备工作，获取HttpClient对象之类的数据")
    public void beforeTest(){
        /*给config包下的TestConfig类中的各变量赋值，这个值通过utils包下的TestUrlUtil 类的getUrl方法来获取，这个方法传的是config
         *包下的InterFaceName中对应的枚举值*/
        TestConfig.getUserInfoUrl = TestUrlUtil.getUrl(InterfaceName.GETUSERINFO);
        TestConfig.addUserUrl = TestUrlUtil.getUrl(InterfaceName.ADDUSER);
        TestConfig.getUserListUrl = TestUrlUtil.getUrl(InterfaceName.GETUSERLIST);
        TestConfig.loginUrl = TestUrlUtil.getUrl(InterfaceName.LOGIN);
        TestConfig.updateUserInfoUrl = TestUrlUtil.getUrl(InterfaceName.UPDATEUSERINFO);

        /*
         * 有cookiestore用：
         * TestConfig.closeableHttpClient = HttpClients.custom().setDefaultCookieStore(store).build();
         *
         * 没有cookies用：
         * TestConfig.closeableHttpClient = HttpClients.createDefault();
         * */
        //TestConfig.closeableHttpClient = HttpClients.custom().setDefaultCookieStore(TestConfig.cookieStore).build();
    }


    //（2）正式写测试代码
    @Test(groups = "loginTrue", description = "用户登录成功接口测试")
    public void loginTrue() throws IOException {
        //①把测试数据从数据库中取出来，这里可以用util包中DatabaseUtil类配置的SqlSession来取
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        /**
         * Retrieve a single row mapped from the statement key and parameter.检索从语句键和参数映射的一行
         * @param <T> the returned object type
         * @param statement 与要使用语句匹配的唯一标识符，
         * 本例中statement要与SQLMapper.xml中select标签的id名称一致
         * @param parameter 传递给语句的参数对象。
         * 本例中parameter为传递给SQLMapper.xml中对应语句的参数。即#{**}
         * @return Mapped object
         * <T> T selectOne(String statement, Object parameter);
         */
        LoginCase loginCase = sqlSession.selectOne("loginCase",1);

        System.out.println(loginCase.toString());
        System.out.println(TestConfig.loginUrl);

        //②用这些测试数据发送HttpClient请求
        String result = getResult(loginCase);

        //③ 获取HTTPRequest返回结果，查看是否符合预期
        Assert.assertEquals(loginCase.getExpected(),result);
    }


    @Test(groups = "loginFalse", description = "用户登录失败接口测试")
    public void loginFalse() throws IOException {
        //把测试数据从数据库中取出来，这里可以用util包中的DatabaseUtil类配置的SqlSession来取
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        LoginCase loginCase = sqlSession.selectOne("loginCase",2);

        System.out.println(loginCase.toString());
        System.out.println(TestConfig.loginUrl);

        //②用这些测试数据发送HttpClient请求
        String result = getResult(loginCase);


        //③ 获取HTTPRequest返回结果，查看是否符合预期
        Assert.assertEquals(loginCase.getExpected(),result);
    }



    private String getResult(LoginCase loginCase) throws IOException {
        HttpPost httpPost = new HttpPost(TestConfig.loginUrl);
        JSONObject param = new JSONObject();
        param.put("user_name",loginCase.getUser_name());
        param.put("user_password",loginCase.getUser_password());

        httpPost.setHeader("content-type", "application/json");

        //将请求信息添加到方法中
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        httpPost.setEntity(entity);

        String result;
        TestConfig.cookieStore = new BasicCookieStore();

        TestConfig.closeableHttpClient = HttpClients.custom().setDefaultCookieStore(TestConfig.cookieStore).build();
        CloseableHttpResponse response = TestConfig.closeableHttpClient.execute(httpPost);

        result = EntityUtils.toString(response.getEntity(),"utf-8");

        List<Cookie> cookieList = TestConfig.cookieStore.getCookies();
        for (Cookie cookie : cookieList){
            String name = cookie.getName();
            String value = cookie.getValue();
            System.out.println("name=" + name + ", value=" + value);
        }

        //response.close();
        //TestConfig.closeableHttpClient.close();

        return result;
    }

}