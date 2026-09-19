package com.example;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ExpressionGenerator 单元测试
 */
class ExpressionGeneratorTest {

    @Test
    void testGenerateNotNull() {
        ExpressionGenerator generator = new ExpressionGenerator(10);
        Expression expr = generator.generate();
        assertNotNull(expr);
    }

    @Test
    void testGenerateNoRepeat() {
        ExpressionGenerator generator = new ExpressionGenerator(10);
        Set<String> seen = new HashSet<>();
        for (int i = 0; i < 20; i++) {
            Expression expr = generator.generate();
            String key = ExpressionNormalizer.normalize(expr);
            assertFalse(seen.contains(key), "重复题目: " + key);
            seen.add(key);
        }
    }

    @Test
    void testEvaluateNoNegative() {
        ExpressionGenerator generator = new ExpressionGenerator(10);
        for (int i = 0; i < 50; i++) {
            Expression expr = generator.generate();
            Fraction result = expr.evaluate();
            assertTrue(result.getNumerator() >= 0, "出现负数: " + expr);
        }
    }
}