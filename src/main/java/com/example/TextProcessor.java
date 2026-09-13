package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * 文本预处理工具类：
 * - 去除标点、空白
 * - 分词（中文按字 + bigram，英文按单词）
 */
public class TextProcessor {

    private TextProcessor() {
       // 工具类，禁止实例化
    }

    /**
     * 预处理文本，返回分词后的 token 列表
     */
        public static List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            return tokens;
        }

        // 1. 转小写，去除标点与空白
        StringBuilder cleaned = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                cleaned.append(Character.toLowerCase(c));
            }
        }

        String s = cleaned.toString();
        if (s.isEmpty()) {
            return tokens;
        }

        int len = s.length();

        // 2. unigram
        for (int i = 0; i < len; i++) {
            tokens.add(s.substring(i, i + 1));
        }

        // 3. bigram
        for (int i = 0; i + 1 < len; i++) {
            tokens.add(s.substring(i, i + 2));
        }

        return tokens;
    }
}