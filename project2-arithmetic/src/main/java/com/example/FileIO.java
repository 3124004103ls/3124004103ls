package com.example;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件读写工具类
 */
public class FileIO {

    private FileIO() {
        // 工具类，禁止实例化
    }

    /**
     * 读取所有行
     */
    public static List<String> readLines(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new IOException("文件不存在: " + filePath);
        }
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }

    /**
     * 写入所有行
     */
    public static void writeLines(String filePath, List<String> lines) throws IOException {
        Path path = Paths.get(filePath);
        Files.write(path, lines, StandardCharsets.UTF_8);
    }

    /**
     * 生成 Exercises.txt 和 Answers.txt 的内容
     */
    public static List<String> buildExerciseLines(List<Expression> expressions) {
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < expressions.size(); i++) {
            Expression expr = expressions.get(i);
            lines.add((i + 1) + ". " + ExpressionPrinter.print(expr) + " =");
        }
        return lines;
    }

    public static List<String> buildAnswerLines(List<Expression> expressions) {
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < expressions.size(); i++) {
            Expression expr = expressions.get(i);
            lines.add((i + 1) + ". " + ExpressionPrinter.print(expr) + " = " + expr.evaluate());
        }
        return lines;
    }
}