package com.example;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

public class Main {

    // 定义一个接口，代表我们的智能体
    interface WeatherAgent {
        String chat(String userMessage);
    }

    public static void main(String[] args) {
        String apiKey = System.getenv("DASHSCOPE_API_KEY");

        if (apiKey == null || apiKey.isEmpty()) {
            System.out.println("错误：未找到环境变量 DASHSCOPE_API_KEY，请检查配置。");
            return;
        }

        // 1. 初始化大模型
        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey(apiKey) 
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .modelName("qwen-max")
                .build();

        // 2. 实例化我们的天气工具
        WeatherTool weatherTool = new WeatherTool();

        // 3. 构建智能体：把大模型、记忆（多轮对话）和工具绑定在一起
        WeatherAgent agent = AiServices.builder(WeatherAgent.class)
                .chatLanguageModel(model)
                .tools(weatherTool) 
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10)) 
                // 加在这里，给Agent注入灵魂
                .systemMessageProvider(memoryId -> "你是一个专业的智能出行助手。当用户询问某地的天气时，请调用相应的工具获取数据。如果用户需要出行，请务必查询未来3天预报，并根据温度、降水情况给出合理的穿衣和出行建议。")
                .build();

        // 用一个 Scanner 做个简单的控制台交互
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        while (true) {
            System.out.print("\n你: ");
            String input = scanner.nextLine();
            if ("exit".equalsIgnoreCase(input)) {
                break;
            }
            
            try {
                // 智能体会自己判断要不要调用 weatherTool
                String response = agent.chat(input);
                System.out.println("Agent: " + response);
            } catch (Exception e) {
                System.out.println("请求出错（可能是限流，请稍后再试）: " + e.getMessage());
            }
        }
        scanner.close();
    }
}