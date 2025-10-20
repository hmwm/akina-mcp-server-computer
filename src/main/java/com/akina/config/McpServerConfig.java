package com.akina.config;

import com.akina.domain.service.ComputerService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpServerConfig {

    // toolObjects 可以设置你所有的工具包，ComputerService、XxxService 等。
    @Bean
    public ToolCallbackProvider computerTools(ComputerService computerService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(computerService)
                .build();
    }
}
