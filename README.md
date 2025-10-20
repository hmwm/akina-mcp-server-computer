# Akina MCP Server Computer

一个基于 Spring AI MCP (Model Context Protocol) 框架构建的计算机信息获取服务，为 AI 助手提供跨平台的系统信息查询功能。

## 🚀 项目特性

- **跨平台支持**: 支持 Windows、macOS、Linux 三大主流操作系统
- **MCP 协议**: 完全兼容 Model Context Protocol 标准
- **系统信息获取**: 提供详细的计算机硬件和系统配置信息
- **Spring AI 集成**: 基于 Spring AI MCP Server 框架构建
- **工具化设计**: 可作为 MCP 工具被 AI 助手调用
- **错误处理**: 完善的异常处理机制，确保服务稳定性

## 📁 项目结构

```
akina-mcp-server-computer/
├── src/main/java/com/akina/
│   ├── McpServerComputerApplication.java    # 应用启动类
│   ├── config/
│   │   └── McpServerConfig.java            # MCP 服务器配置
│   └── domain/
│       ├── model/dto/                      # 数据传输对象
│       │   ├── ComputerFunctionRequest.java # 请求对象
│       │   └── ComputerFunctionResponse.java # 响应对象
│       ├── os/
│       │   └── OsEnum.java                 # 操作系统枚举
│       └── service/
│           └── ComputerService.java        # 核心服务类
├── src/main/resources/
│   └── application.yml                     # 应用配置
├── src/test/java/                         # 测试代码
├── pom.xml                               # Maven 配置
└── README.md                             # 项目文档
```

## 🛠️ 技术栈

- **框架**: Spring Boot 3.4.3
- **MCP 框架**: Spring AI MCP Server 1.0.0-M6
- **构建工具**: Maven 3.x
- **Java 版本**: JDK 17
- **序列化**: FastJSON 2.0.49
- **工具库**: Lombok

## 📋 功能特性

### 系统信息获取
- **基础系统信息**: 操作系统名称、版本、架构
- **用户信息**: 用户名、主目录、工作目录
- **Java 环境**: Java 运行时版本信息
- **硬件信息**: 通过系统命令获取详细硬件配置

### 跨平台支持
- **Windows**: 使用 `systeminfo` 命令获取系统信息
- **macOS**: 使用 `system_profiler SPHardwareDataType` 获取硬件信息
- **Linux**: 使用 `lshw -short` 获取硬件列表

### MCP 工具集成
- **工具名称**: `queryConfig`
- **工具描述**: 获取电脑配置
- **输入参数**: 电脑名称 (可选)
- **输出格式**: 结构化的系统信息 JSON

## 🚀 快速开始

### 环境要求

- JDK 17+
- Maven 3.x

### 1. 克隆项目

```bash
git clone <repository-url>
cd akina-mcp-server-computer
```

### 2. 构建项目

```bash
mvn clean install
```

### 3. 运行服务

```bash
# 方式一：使用 Maven 运行
mvn spring-boot:run

# 方式二：运行 JAR 包
java -Dspring.ai.mcp.server.stdio=true -jar target/mcp-server-computer-1.0.0.jar
```

### 4. 作为 MCP 工具使用

在 MCP 客户端配置中添加此服务：

```json
{
  "mcpServers": {
    "mcp-server-computer": {
      "command": "java",
      "args": [
        "-Dspring.ai.mcp.server.stdio=true",
        "-jar",
        "path/to/mcp-server-computer-1.0.0.jar"
      ]
    }
  }
}
```

## 🔧 配置说明

### 应用配置 (application.yml)

```yaml
spring:
  application:
    name: mcp-server-computer
  ai:
    mcp:
      server:
        name: ${spring.application.name}
        version: 1.0.0
  main:
    banner-mode: off
    web-application-type: none

logging:
  pattern:
    console:
  file:
    name: ${spring.application.name}.log

server:
  servlet:
    encoding:
      charset: UTF-8
      force: true
      enabled: true
```

### MCP 服务器配置

服务会自动注册 `ComputerService` 中的工具方法：

```java
@Configuration
public class McpServerConfig {
    @Bean
    public ToolCallbackProvider computerTools(ComputerService computerService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(computerService)
                .build();
    }
}
```

## 📊 API 接口

### 工具接口

**工具名称**: `queryConfig`

**描述**: 获取电脑配置信息

**输入参数**:
```json
{
  "computer": "string"  // 电脑名称 (可选)
}
```

**输出格式**:
```json
{
  "osName": "Windows 10",
  "osVersion": "10.0",
  "osArch": "amd64",
  "userName": "username",
  "userHome": "C:\\Users\\username",
  "userDir": "C:\\workspace",
  "javaVersion": "17.0.2",
  "osInfo": "详细的系统硬件信息..."
}
```

## 🧪 测试

### 运行测试

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=ApiTest
```

### 测试示例

```java
@SpringBootTest
public class ComputerServiceTest {
    
    @Autowired
    private ComputerService computerService;
    
    @Test
    public void testQueryConfig() {
        ComputerFunctionRequest request = new ComputerFunctionRequest();
        request.setComputer("test-computer");
        
        ComputerFunctionResponse response = computerService.queryConfig(request);
        
        assertNotNull(response.getOsName());
        assertNotNull(response.getJavaVersion());
        // 更多断言...
    }
}
```

## 🔍 使用示例

### 在 AI 助手中使用

当 AI 助手需要获取计算机信息时，可以调用此 MCP 工具：

```python
# 示例：在 Python AI 助手中使用
import mcp

# 连接到 MCP 服务器
client = mcp.Client("mcp-server-computer")

# 调用工具获取系统信息
result = client.call_tool("queryConfig", {"computer": "my-computer"})

print(f"操作系统: {result['osName']}")
print(f"Java 版本: {result['javaVersion']}")
print(f"系统信息: {result['osInfo']}")
```

### 在 Spring AI 应用中使用

```java
@Autowired
private ChatClient chatClient;

public String getSystemInfo() {
    return chatClient.prompt("获取当前计算机的配置信息")
        .call()
        .content();
}
```

## 🏗️ 架构设计

### 核心组件

1. **McpServerComputerApplication**: 应用启动入口
2. **McpServerConfig**: MCP 服务器配置，注册工具
3. **ComputerService**: 核心业务逻辑，提供系统信息查询
4. **OsEnum**: 操作系统类型枚举，支持跨平台命令执行
5. **DTO 类**: 请求和响应的数据传输对象

### 设计模式

- **策略模式**: 通过 `OsEnum` 实现不同操作系统的命令策略
- **工具模式**: 使用 Spring AI 的 `@Tool` 注解实现 MCP 工具
- **配置模式**: 通过 `McpServerConfig` 统一管理工具注册

## 🔧 扩展开发

### 添加新的系统信息

1. 在 `ComputerService` 中添加新的方法：

```java
@Tool(description = "获取网络配置信息")
public NetworkInfoResponse getNetworkInfo(NetworkInfoRequest request) {
    // 实现网络信息获取逻辑
}
```

2. 在 `McpServerConfig` 中注册新的服务：

```java
@Bean
public ToolCallbackProvider networkTools(NetworkService networkService) {
    return MethodToolCallbackProvider.builder()
            .toolObjects(networkService)
            .build();
}
```

### 添加新的操作系统支持

1. 在 `OsEnum` 中添加新的操作系统：

```java
NEW_OS("newos", "newos-specific-command")
```

2. 实现对应的命令执行逻辑

## 🐛 故障排除

### 常见问题

1. **命令执行失败**
   - 检查操作系统是否支持对应的命令
   - 确认用户权限是否足够
   - 查看日志中的错误信息

2. **MCP 连接问题**
   - 确认服务是否正常启动
   - 检查 MCP 客户端配置是否正确
   - 验证 JAR 包路径是否正确

3. **编码问题**
   - 确认系统编码设置
   - 检查命令输出编码格式

### 日志配置

```yaml
logging:
  level:
    com.akina: DEBUG
    org.springframework.ai: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

## 📝 开发指南

### 代码规范

- 使用 Lombok 减少样板代码
- 遵循 Spring Boot 最佳实践
- 完善的异常处理，避免向上抛出异常
- 详细的 JavaDoc 注释

### 提交规范

```bash
# 功能开发
git commit -m "feat: 添加网络信息获取功能"

# 问题修复
git commit -m "fix: 修复 Windows 系统信息获取问题"

# 文档更新
git commit -m "docs: 更新 README 文档"
```

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 📞 联系方式

- 项目维护者: Akina Team
- 邮箱: [your-email@example.com]
- 项目链接: [https://github.com/your-username/akina-mcp-server-computer](https://github.com/your-username/akina-mcp-server-computer)

## 🙏 致谢

- [Spring AI](https://spring.io/projects/spring-ai) - 强大的 AI 集成框架
- [Model Context Protocol](https://modelcontextprotocol.io/) - AI 工具集成协议
- [Spring Boot](https://spring.io/projects/spring-boot) - 企业级 Java 应用框架

## 📈 版本历史

### v1.0.0 (2024-01-01)
- 初始版本发布
- 支持 Windows、macOS、Linux 系统信息获取
- 集成 Spring AI MCP Server 框架
- 提供完整的 MCP 工具接口