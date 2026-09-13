package com.example;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * 程序入口：论文查重
 * 命令行参数：
 *   args[0] 原文文件绝对路径
 *   args[1] 抄袭版论文文件绝对路径
 *   args[2] 答案输出文件绝对路径
 */
public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        // 参数校验
        if (args.length != 3) {
            LOGGER.severe("用法: java -jar main.jar <原文文件> <抄袭版论文> <答案文件>");
            System.exit(1);
        }

        String origPath = args[0];
        String copyPath = args[1];
        String answerPath = args[2];

        try {
            // 读取文件
            String origText = readFile(origPath);
            String copyText = readFile(copyPath);

            // 计算相似度
            double similarity = SimilarityCalculator.calculate(origText, copyText);

            // 格式化输出：保留两位小数
            String result = String.format("%.2f", similarity);

            // 写入答案文件
            writeFile(answerPath, result);

            LOGGER.info(() -> "重复率: " + result);

        } catch (IOException e) {
            LOGGER.severe("文件读写错误: " + e.getMessage());
            System.exit(2);
        } catch (Exception e) {
            LOGGER.severe("程序异常: " + e.getMessage());
            System.exit(3);
        }
    }

    /**
     * 读取文件内容（UTF-8）
     */
    static String readFile(String path) throws IOException {
        Path p = Paths.get(path);
        if (!Files.exists(p)) {
            throw new IOException("文件不存在: " + path);
        }
        if (!Files.isReadable(p)) {
            throw new IOException("文件不可读: " + path);
        }
        return new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
    }

    /**
     * 写入答案文件（UTF-8）
     */
    static void writeFile(String path, String content) throws IOException {
        Path p = Paths.get(path);
        Files.write(p, content.getBytes(StandardCharsets.UTF_8));
    }
}
