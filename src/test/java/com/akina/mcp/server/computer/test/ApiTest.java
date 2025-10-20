package com.akina.mcp.server.computer.test;

import com.akina.domain.os.OsEnum;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

@Slf4j
public class ApiTest {

    public static void main(String[] args) {

        // 获取系统属性
        Properties props = System.getProperties();
        // 获取系统名称
        String osName = props.getProperty("os.name");
        // 操作系统版本
        String osVersion = props.getProperty("os.version");
        // 操作系统架构
        String osArch = props.getProperty("os.arch");
        // 用户的账户名称
        String userName = props.getProperty("user.name");
        // 用户的主目录
        String userHome = props.getProperty("user.home");
        // 用户的当前工作目录
        String userDir = props.getProperty("user.dir");
        // Java 运行时环境版本
        String javaVersion = props.getProperty("java.version");

        System.out.println("操作系统名称: " + osName);
        System.out.println("操作系统版本: " + osVersion);
        System.out.println("操作系统架构: " + osArch);
        System.out.println("用户名称: " + userName);
        System.out.println("用户主目录: " + userHome);
        System.out.println("用户当前工作目录: " + userDir);
        System.out.println("Java 版本: " + javaVersion);



        OsEnum os = OsEnum.detect(osName);
        String osInfo = (os == null)
                ? "[ERROR] Unsupported OS"
                : getOsSpecificInfo(os.cmd());

    }

    private static String getOsSpecificInfo(String command){
        StringBuilder cache = new StringBuilder();

        try{
            Process process = Runtime.getRuntime().exec(command);

            try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while((line = reader.readLine()) != null) {

                    cache.append(line).append("\n");
                }
            }

            process.waitFor();
        } catch (Exception e) {

            log.warn("Failed to execute systemInfo", e);
            return "[ERROR] systemInfo command failed：" + e.getMessage();
        }

        return cache.toString();
    }
}
