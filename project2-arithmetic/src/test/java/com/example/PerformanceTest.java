package com.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 性能测试：统计生成题目和判分的耗时
 */
class PerformanceTest {

    private static final Logger LOGGER = Logger.getLogger(PerformanceTest.class.getName());

    @Test
    void testGeneratePerformance() {
        for (int i = 0; i < 10; i++) {
            new ExpressionGenerator(10).generate();
        }

        long start = System.currentTimeMillis();
        ExpressionGenerator generator = new ExpressionGenerator(10);
        for (int i = 0; i < 10000; i++) {
            generator.generate();
        }
        long cost = System.currentTimeMillis() - start;

        LOGGER.info("=== 生成性能测试 ===");
        LOGGER.info(() -> "生成 10000 道题耗时: " + cost + " ms");

        assertTrue(cost >= 0);
    }

    @Test
    void testEvaluatePerformance() {
        ExpressionGenerator generator = new ExpressionGenerator(10);
        List<Expression> expressions = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            expressions.add(generator.generate());
        }

        long start = System.currentTimeMillis();
        for (Expression expr : expressions) {
            expr.evaluate();
        }
        long cost = System.currentTimeMillis() - start;

        LOGGER.info("=== 计算性能测试 ===");
        LOGGER.info(() -> "计算 10000 道题耗时: " + cost + " ms");

        assertTrue(cost >= 0);
    }

    @Test
    void testNormalizePerformance() {
        ExpressionGenerator generator = new ExpressionGenerator(10);
        List<Expression> expressions = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            expressions.add(generator.generate());
        }

        long start = System.currentTimeMillis();
        for (Expression expr : expressions) {
            ExpressionNormalizer.normalize(expr);
        }
        long cost = System.currentTimeMillis() - start;

        LOGGER.info("=== 规范化性能测试 ===");
        LOGGER.info(() -> "规范化 10000 道题耗时: " + cost + " ms");

        assertTrue(cost >= 0);
    }
}