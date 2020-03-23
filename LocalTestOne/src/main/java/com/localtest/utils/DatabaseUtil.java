package com.localtest.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

public class DatabaseUtil {

    //SqlSession是能够执行SQL的对象？
    public static SqlSession getSqlSession() throws IOException {

        //① 获取配置的资源文件
        //org.apache.ibatis.io.Resources;
        // databaseConfig.xml是resources中写的配置文件；
        Reader reader = Resources.getResourceAsReader("databaseConfig.xml");

        //② 把上一步读到的文件build出来
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader);

        //③ 返回这个SQLSession，这个sqlSession能够执行配置文件中的SQL语句
        SqlSession sqlSession = factory.openSession();
        return sqlSession;
    }

}