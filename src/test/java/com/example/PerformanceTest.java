package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 性能测试：统计各方法耗时
 */
class PerformanceTest {

    @Test
    void testPerformance() {
        // 构造一个大文本（重复 10000 次）
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append("今天是星期天，天气晴，今天晚上我要去看电影。");
        }
        String bigText = sb.toString();

        // 预热（让 JIT 编译）
        for (int i = 0; i < 10; i++) {
            TextProcessor.tokenize(bigText);
        }

        // 测试 tokenize 耗时
        long start1 = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            TextProcessor.tokenize(bigText);
        }
        long cost1 = System.currentTimeMillis() - start1;

        // 测试 calculate 耗时
        long start2 = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            SimilarityCalculator.calculate(bigText, bigText);
        }
        long cost2 = System.currentTimeMillis() - start2;

        System.out.println("=== 性能测试结果 ===");
        System.out.println("tokenize 耗时: " + cost1 + " ms (100 次)");
        System.out.println("calculate 耗时: " + cost2 + " ms (100 次)");

        assertTrue(cost1 >= 0 && cost2 >= 0);
    }
}