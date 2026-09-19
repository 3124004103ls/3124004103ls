package com.example;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 题目生成器：随机生成符合规则的四则运算题
 */
public class ExpressionGenerator {

    private final int range;        // 数值范围（0 到 range-1）
    private final Random random;
    private final Set<String> seen; // 用于去重

    public ExpressionGenerator(int range) {
        this.range = range;
        this.random = new Random();
        this.seen = new HashSet<>();
    }

    /**
     * 生成一道题目，保证合法且不重复
     */
    public Expression generate() {
        while (true) {
            int operatorCount = random.nextInt(3) + 1; // 1-3 个运算符
            Expression expr = build(operatorCount);

            // 检查合法性：计算过程中不能出现负数、除法必须是真分数
            if (!isValid(expr)) {
                continue;
            }

            // 检查是否重复
            String key = ExpressionNormalizer.normalize(expr);
            if (seen.contains(key)) {
                continue;
            }
            seen.add(key);
            return expr;
        }
    }

    /**
     * 递归构建表达式，共 operatorCount 个运算符
     */
    private Expression build(int operatorCount) {
        if (operatorCount == 0) {
            return new Expression(randomFraction());
        }
        // 随机把运算符分给左右子树
        int leftCount = random.nextInt(operatorCount);
        int rightCount = operatorCount - 1 - leftCount;

        Expression left = build(leftCount);
        Expression right = build(rightCount);

        Expression.Type op = randomOperator();
        return new Expression(op, left, right);
    }

    /**
     * 随机生成一个数字（自然数或真分数）
     */
    private Fraction randomFraction() {
        if (random.nextBoolean()) {
            // 自然数
            int n = random.nextInt(range);
            return new Fraction(n);
        } else {
            // 真分数
            int den = random.nextInt(range - 1) + 2; // 分母 >= 2
            int num = random.nextInt(den - 1) + 1;   // 分子 < 分母
            return new Fraction(num, den);
        }
    }

    /**
     * 随机运算符
     */
    private Expression.Type randomOperator() {
        int t = random.nextInt(4);
        switch (t) {
            case 0: return Expression.Type.ADD;
            case 1: return Expression.Type.SUBTRACT;
            case 2: return Expression.Type.MULTIPLY;
            default: return Expression.Type.DIVIDE;
        }
    }

    /**
     * 检查表达式是否合法：
     * - 计算过程中不能出现负数（减法时左 >= 右）
     * - 除法结果必须是真分数
     */
    private boolean isValid(Expression expr) {
        if (expr.isNumber()) {
            return true;
        }
        if (!isValid(expr.getLeft()) || !isValid(expr.getRight())) {
            return false;
        }
        Fraction l = expr.getLeft().evaluate();
        Fraction r = expr.getRight().evaluate();

        switch (expr.getType()) {
            case MULTIPLY:
                // 乘法：两个数都不能是 0
                if (l.getNumerator() == 0 || r.getNumerator() == 0) {
                    return false;
                }
                break;
            case DIVIDE:
                // 除法：除数不能是 0，且结果必须是真分数
                if (r.getNumerator() == 0) {
                    return false;
                }
                // 被除数不能是 0
                if (l.getNumerator() == 0) {
                    return false;
                }
                Fraction result = l.divide(r);
                if (result.getNumerator() >= result.getDenominator()) {
                    return false;
                }
                break;
            case SUBTRACT:
                // 减法：结果不能是负数
                if (l.compareTo(r) < 0) {
                    return false;
                }
                break;
            default:
                break;
        }
        return true;
    }
}