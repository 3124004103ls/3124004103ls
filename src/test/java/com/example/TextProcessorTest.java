package com.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TextProcessor 单元测试
 */
public class TextProcessorTest {

    /**
     * 测试1：正常中文句子分词，应该包含 unigram 和 bigram
     */
    @Test
    public void testTokenizeNormalChinese() {
        List<String> tokens = TextProcessor.tokenize("今天是星期天");
        assertNotNull(tokens);
        assertFalse(tokens.isEmpty());
        assertTrue(tokens.contains("今"));
        assertTrue(tokens.contains("天"));
        assertTrue(tokens.contains("今天"));
    }

    /**
     * 测试2：空字符串应返回空列表
     */
    @Test
    public void testTokenizeEmptyString() {
        List<String> tokens = TextProcessor.tokenize("");
        assertNotNull(tokens);
        assertTrue(tokens.isEmpty());
    }

    /**
     * 测试3：null 应返回空列表
     */
    @Test
    public void testTokenizeNull() {
        List<String> tokens = TextProcessor.tokenize(null);
        assertNotNull(tokens);
        assertTrue(tokens.isEmpty());
    }

    /**
     * 测试4：只有标点符号，应返回空列表
     */
    @Test
    public void testTokenizeOnlyPunctuation() {
        List<String> tokens = TextProcessor.tokenize("，。！？、；：");
        assertNotNull(tokens);
        assertTrue(tokens.isEmpty());
    }

    /**
     * 测试5：英文大小写应被统一为小写
     */
    @Test
    public void testTokenizeEnglishLowerCase() {
        List<String> tokens = TextProcessor.tokenize("Hello World");
        assertTrue(tokens.contains("h"));
        assertTrue(tokens.contains("e"));
        assertTrue(tokens.contains("he"));
    }

    /**
     * 测试6：中英文混合
     */
    @Test
    public void testTokenizeMixed() {
        List<String> tokens = TextProcessor.tokenize("abc中文123");
        assertTrue(tokens.contains("a"));
        assertTrue(tokens.contains("中"));
        assertTrue(tokens.contains("1"));
    }
}