package com.akina.domain.service;

import com.akina.domain.model.dto.ComputerFunctionRequest;
import com.akina.domain.model.dto.ComputerFunctionResponse;
import com.akina.domain.os.OsEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

@Slf4j
@Service
public class ComputerService {

    @Tool(description = "获取电脑配置")
    public ComputerFunctionResponse queryConfig(ComputerFunctionRequest request) {

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

        // 根据操作系统执行特定的命令来获取更多信息 win, mac, linux

        // 根据当前操作系统名称识别对应的 OS 枚举对象（Windows / Mac / Linux 等），
        // 如果识别成功，则调用统一方法获取该系统的特定信息；
        // 如果识别失败，则返回统一错误提示。
        // 最终将获取到的信息封装到 MCP 响应对象中返回。
        OsEnum os = OsEnum.detect(osName);
        String osInfo = (os == null)
                ? "[ERROR] Unsupported OS"
                : getOsSpecificInfo(os.cmd());

        //        String osInfo = "";
//        if (osName.toLowerCase().contains("win")){
//            // win特定的代码
//            osInfo = getOsSpecificInfo(OsEnum.WIN_OS_COMMAND.getOsCommand());
//        } else if (osName.toLowerCase().contains("mac")){
//            // mac特定代码
//            osInfo = getOsSpecificInfo(OsEnum.MAC_OS_COMMAND.getOsCommand());
//        } else if (osName.toLowerCase().contains("nix") || osName.toLowerCase().contains("nux")){
//            // linux
//            osInfo = getOsSpecificInfo(OsEnum.LINUX_OS_COMMAND.getOsCommand());
//        }
        ComputerFunctionResponse response = new ComputerFunctionResponse();
        response.setOsName(osName);
        response.setOsVersion(osVersion);
        response.setOsArch(osArch);
        response.setUserName(userName);
        response.setUserHome(userHome);
        response.setUserDir(userDir);
        response.setJavaVersion(javaVersion);
        response.setOsInfo(osInfo);

        return response;
    }


    private String getOsSpecificInfo(String command){
        StringBuilder cache = new StringBuilder();
        // w获取特定的系统信息
        try{
            Process process = Runtime.getRuntime().exec(command);

            try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while((line = reader.readLine()) != null) {
                    cache.append(line).append("\n");
                }
            }
            // 等待结束
            process.waitFor();
        } catch (Exception e) {
            // MCP 作为「被调用的服务」，抛异常会向上游（例如客户端、host agent）冒泡，导致调用方收到 error，而不是正常的 result/schema 响应。
            // 可能被当成 调用失败 而不是业务失败，
            // 甚至导致流程中断、工具不可用。
            // MCP 友好写法 不抛出，不 silent，不破协议，错误显式表达。
            log.warn("Failed to execute systemInfo", e);
            return "[ERROR] systemInfo command failed：" + e.getMessage();
        }

        return cache.toString();
    }

//    private String getWindowsSpecificInfo() {
//        StringBuilder cache = new StringBuilder();
//        // win获取特定的系统信息
//        try{
//            Process process = Runtime.getRuntime().exec("systemInfo");
//
//            try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//                String line;
//                while((line = reader.readLine()) != null) {
//                    cache.append(line).append("\n");
//                }
//            }
//
//            // 等待结束
//            process.waitFor();
//        } catch (Exception e) {
//            // MCP 作为「被调用的服务」，抛异常会向上游（例如客户端、host agent）冒泡，导致调用方收到 error，而不是正常的 result/schema 响应。
////            可能被当成 调用失败 而不是业务失败，
////            甚至导致流程中断、工具不可用。
//            // MCP 友好写法 不抛出，不 silent，不破协议，错误显式表达。
//           log.warn("Failed to execute systemInfo", e);
//           return "[ERROR] systemInfo command failed：" + e.getMessage();
//        }
//
//        return cache.toString();
//    }
//
//    private String getMacSpecificInfo() {
//
//        StringBuilder cache = new StringBuilder();
//        // mac获取特定的系统信息
//        try{
//            Process process = Runtime.getRuntime().exec("system_profiler SPHardwareDataType");
//            try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//                String line;
//                while((line = reader.readLine()) != null) {
//                    cache.append(line).append("\n");
//                }
//            }
//
//            process.waitFor();
//
//        } catch (Exception e) {
//            log.warn("Failed to execute systemInfo", e);
//            return "[ERROR] systemInfo command failed：" + e.getMessage();
//        }
//
//        return cache.toString();
//    }
//
//    private String getLinuxSpecificInfo() {
//
//        StringBuilder cache = new StringBuilder();
//        // linux获取特定的系统信息
//        try{
//            Process process = Runtime.getRuntime().exec("lshw -short");
//            try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//                String line;
//                while((line = reader.readLine()) != null) {
//                    cache.append(line).append("\n");
//                }
//            }
//
//            process.waitFor();
//        } catch (Exception e) {
//            log.warn("Failed to execute systemInfo", e);
//            return "[ERROR] systemInfo command failed：" + e.getMessage();
//        }
//
//        return cache.toString();
//    }


}
