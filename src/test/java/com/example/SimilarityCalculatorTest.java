package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SimilarityCalculator 单元测试
 */
public class SimilarityCalculatorTest {

    /**
     * 测试1：完全相同的文本，相似度应为 1.00
     */
    @Test
    public void testIdenticalText() {
        double sim = SimilarityCalculator.calculate("今天是星期天", "今天是星期天");
        assertEquals(1.0, sim, 0.01);
    }

    /**
     * 测试2：完全不同的文本，相似度应接近 0
     */
    @Test
    public void testCompletelyDifferent() {
        double sim = SimilarityCalculator.calculate("abcde", "12345");
        assertTrue(sim < 0.3);
    }

    /**
     * 测试3：高度相似的文本（只改了几个字）
     */
    @Test
    public void testHighlySimilar() {
        double sim = SimilarityCalculator.calculate(
                "今天是星期天，天气晴，今天晚上我要去看电影。",
                "今天是周天，天气晴朗，我晚上要去看电影。"
        );
        assertTrue(sim > 0.5);
    }

    /**
     * 测试4：两段都为空，应返回 1.0
     */
    @Test
    public void testBothEmpty() {
        double sim = SimilarityCalculator.calculate("", "");
        assertEquals(1.0, sim, 0.01);
    }

    /**
     * 测试5：一段为空，一段非空，应返回 0.0
     */
    @Test
    public void testOneEmpty() {
        double sim = SimilarityCalculator.calculate("今天是星期天", "");
        assertEquals(0.0, sim, 0.01);
    }

    /**
     * 测试6：null 输入，不应抛异常
     */
    @Test
    public void testNullInput() {
        double sim1 = SimilarityCalculator.calculate(null, "abc");
        double sim2 = SimilarityCalculator.calculate("abc", null);
        double sim3 = SimilarityCalculator.calculate(null, null);
        assertEquals(0.0, sim1, 0.01);
        assertEquals(0.0, sim2, 0.01);
        assertEquals(1.0, sim3, 0.01);
    }

    /**
     * 测试7：相似度结果应在 [0, 1] 范围内
     */
    @Test
    public void testRange() {
        double sim = SimilarityCalculator.calculate("abcdefg", "abcxyz");
        assertTrue(sim >= 0.0 && sim <= 1.0);
    }

    /**
     * 测试8：忽略标点和空白
     */
    @Test
    public void testIgnorePunctuation() {
        double sim = SimilarityCalculator.calculate(
                "今天，是星期天！",
                "今天是星期天。"
        );
        assertEquals(1.0, sim, 0.01);
    }

    /**
     * 测试9：英文大小写不影响相似度
     */
    @Test
    public void testIgnoreCase() {
        double sim = SimilarityCalculator.calculate("Hello World", "hello world");
        assertEquals(1.0, sim, 0.01);
    }

    /**
     * 测试10：长文本相似度计算
     */
    @Test
    public void testLongText() {
        String s1 = "今天是星期天，天气晴，今天晚上我要去看电影。";
        String s2 = "今天是星期天，天气晴，今天晚上我要去看电影。";
        double sim = SimilarityCalculator.calculate(s1, s2);
        assertEquals(1.0, sim, 0.01);
    }
}