package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TextProcessor 单元测试
 */
class TextProcessorTest {

    /**
     * 测试1：正常中文句子分词，应该包含 unigram 和 bigram
     */
    @Test
    void testTokenizeNormalChinese() {
        List<String> tokens = TextProcessor.tokenize("今天是星期天");
        assertNotNull(tokens);
        assertFalse(tokens.isEmpty());
        assertTrue(tokens.contains("今"));
        assertTrue(tokens.contains("天"));
        assertTrue(tokens.contains("今天"));
    }

    /**
     * 测试2：空输入、null、只有标点的输入，都应返回空列表
     */
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "，。！？、；："})
    void testTokenizeEmptyCases(String input) {
        List<String> tokens = TextProcessor.tokenize(input);
        assertNotNull(tokens);
        assertTrue(tokens.isEmpty());
    }

    /**
     * 测试3：英文大小写应被统一为小写
     */
    @Test
    void testTokenizeEnglishLowerCase() {
        List<String> tokens = TextProcessor.tokenize("Hello World");
        assertTrue(tokens.contains("h"));
        assertTrue(tokens.contains("e"));
        assertTrue(tokens.contains("he"));
    }

    /**
     * 测试4：中英文混合
     */
    @Test
    void testTokenizeMixed() {
        List<String> tokens = TextProcessor.tokenize("abc中文123");
        assertTrue(tokens.contains("a"));
        assertTrue(tokens.contains("中"));
        assertTrue(tokens.contains("1"));
    }
}