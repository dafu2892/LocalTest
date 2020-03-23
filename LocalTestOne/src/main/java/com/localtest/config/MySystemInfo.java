package com.localtest.config;

import com.vimalselvam.testng.SystemInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther:
 * @Date:
 * @Description:
 */
public class MySystemInfo implements SystemInfo {
    @Override
    public Map<String, String> getSystemInfo() {

        Map<String, String> systemInfo = new HashMap<>();
        systemInfo.put("测试人员", "dafu");

        return systemInfo;
    }
}