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

    private static WeatherAgent agent;

    interface WeatherAgent {
        String chat(String userMessage);
    }

    public static void main(String[] args) {
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

        // 构建 Agent，注册天气工具和待办工具
        agent = AiServices.builder(WeatherAgent.class)
                .chatLanguageModel(model)
                .tools(new WeatherTool(), new TaskTool()) 
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .systemMessageProvider(memoryId -> "你是一个专业的智能出行与校园管家。你可以调用天气工具查天气，也可以调用待办工具添加、查看、删除用户的待办事项。删除时，请让用户提供完整的待办内容再进行调用。")
                .build();

        SpringApplication.run(WebApp.class, args);
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return agent.chat(message);
    }
}