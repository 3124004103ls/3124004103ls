package com.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 余弦相似度计算器
 */
public class SimilarityCalculator {

    /**
     * 计算两段文本的余弦相似度
     *
     * @param text1 原文
     * @param text2 抄袭版
     * @return 相似度 [0.0, 1.0]
     */
    public static double calculate(String text1, String text2) {
        List<String> tokens1 = TextProcessor.tokenize(text1);
        List<String> tokens2 = TextProcessor.tokenize(text2);

        // 空文本处理
        if (tokens1.isEmpty() && tokens2.isEmpty()) {
            return 1.0;
        }
        if (tokens1.isEmpty() || tokens2.isEmpty()) {
            return 0.0;
        }

        // 构建词频向量
        Map<String, Integer> freq1 = buildFrequency(tokens1);
        Map<String, Integer> freq2 = buildFrequency(tokens2);

        // 计算点积
        double dotProduct = 0.0;
        for (Map.Entry<String, Integer> entry : freq1.entrySet()) {
            Integer v2 = freq2.get(entry.getKey());
            if (v2 != null) {
                dotProduct += entry.getValue() * v2;
            }
        }

        // 计算模长
        double norm1 = 0.0;
        for (int v : freq1.values()) {
            norm1 += v * v;
        }
        norm1 = Math.sqrt(norm1);

        double norm2 = 0.0;
        for (int v : freq2.values()) {
            norm2 += v * v;
        }
        norm2 = Math.sqrt(norm2);

        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }

        // 余弦相似度
        double similarity = dotProduct / (norm1 * norm2);

        // 限制在 [0, 1]
        if (similarity < 0.0) similarity = 0.0;
        if (similarity > 1.0) similarity = 1.0;

        return similarity;
    }

    /**
     * 统计词频
     */
    private static Map<String, Integer> buildFrequency(List<String> tokens) {
        Map<String, Integer> freq = new HashMap<>();
        for (String token : tokens) {
            freq.put(token, freq.getOrDefault(token, 0) + 1);
        }
        return freq;
    }
}