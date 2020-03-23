package com.localtest.config;

import com.aventstack.extentreports.ExtentTest;

/**
 * @Auther: zyh
 * @Date: 2020/03/11 15:33
 * @Description:
 */
public class MyReporter {
    public static ExtentTest report;
    private static String testName;

    public static String getTestName() {
        return testName;
    }

    public static void setTestName(String testName) {
        MyReporter.testName = testName;
    }
}