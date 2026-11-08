package com.example;

import dev.langchain4j.agent.tool.Tool;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class TaskTool {

    private static final String FILE_PATH = "tasks.txt";

    // 工具 1：添加待办
    @Tool("添加一个新的待办事项到列表。输入参数为待办的具体内容，比如：周五交软件工程作业")
    public String addTask(String taskContent) {
        try {
            // 追加写入文件
            Files.write(Paths.get(FILE_PATH), 
                        (taskContent + "\n").getBytes(StandardCharsets.UTF_8), 
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return "✅ 成功添加待办：" + taskContent;
        } catch (IOException e) {
            return "❌ 添加待办失败：" + e.getMessage();
        }
    }

    // 工具 2：查看所有待办
    @Tool("查看当前所有的待办事项列表")
    public String listTasks() {
        try {
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path)) {
                return "当前没有待办事项哦。";
            }
            List<String> tasks = Files.readAllLines(path, StandardCharsets.UTF_8);
            if (tasks.isEmpty()) {
                return "当前没有待办事项哦。";
            }
            StringBuilder sb = new StringBuilder("📋 您当前的待办事项有：\n");
            for (int i = 0; i < tasks.size(); i++) {
                sb.append(i + 1).append(". ").append(tasks.get(i)).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            return "❌ 读取待办失败：" + e.getMessage();
        }
    }
        // 工具 3：删除特定内容的待办
    @Tool("根据待办事项的具体内容删除该条待办。输入参数为要删除的待办事项的完整文字内容")
    public String deleteTask(String taskContent) {
        try {
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path)) {
                return "❌ 当前没有待办事项，无法删除。";
            }
            List<String> tasks = Files.readAllLines(path, StandardCharsets.UTF_8);
            boolean removed = tasks.removeIf(t -> t.contains(taskContent) || taskContent.contains(t));
            
            if (removed) {
                Files.write(path, tasks, StandardCharsets.UTF_8);
                return "✅ 成功删除了包含该内容的待办事项：" + taskContent;
            } else {
                return "❌ 未找到包含该内容的待办事项，请检查输入。";
            }
        } catch (IOException e) {
            return "❌ 删除待办失败：" + e.getMessage();
        }
    }
}
