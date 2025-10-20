package com.akina;

import com.akina.domain.service.ComputerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class McpServerComputerApplication {
    public static void main(String[] args) {
        SpringApplication.run(McpServerComputerApplication.class);
    }



    public void run(String... args) throws Exception {
        log.info("mcp server computer successfully started!");
    }
}
