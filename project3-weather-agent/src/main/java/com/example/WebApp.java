package com.example;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("/api")
public class WebApp {

    // 将 Agent 定义为静态变量，保证 Spring Boot 启动时加载一次，全局可用
    private static WeatherAgent agent;

    // 复用之前的接口定义
    interface WeatherAgent {
        String chat(String userMessage);
    }

    public static void main(String[] args) {
        // 1. 初始化大模型
        String apiKey = System.getenv("DASHSCOPE_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("错误：未找到环境变量 DASHSCOPE_API_KEY！");
            return;
        }

        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .modelName("qwen-max")
                .build();

        // 2. 构建 Agent
        agent = AiServices.builder(WeatherAgent.class)
                .chatLanguageModel(model)
                .tools(new WeatherTool())
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .systemMessageProvider(memoryId -> "你是一个专业的智能出行助手。当用户询问某地天气时，请调用工具获取数据，并根据温度、降水等给出合理的穿衣和出行建议。")
                .build();

        // 3. 启动 Spring Boot Web 服务
        SpringApplication.run(WebApp.class, args);
    }

    // 接收前端发来的对话请求
    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return agent.chat(message);
    }
}