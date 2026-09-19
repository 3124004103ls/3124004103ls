package com.example;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 题目生成器：随机生成符合规则的四则运算题
 */
public class ExpressionGenerator {

    private final int range;
    private final Random random;
    private final Set<String> seen;

    public ExpressionGenerator(int range) {
        this.range = range;
        this.random = new Random();
        this.seen = new HashSet<>();
    }

    public Expression generate() {
        while (true) {
            int operatorCount = random.nextInt(3) + 1;
            Expression expr = build(operatorCount);

            String key = ExpressionNormalizer.normalize(expr);
            if (!isValid(expr) || seen.contains(key)) {
                continue;
            }
            seen.add(key);
            return expr;
        }
    }

    private Expression build(int operatorCount) {
        if (operatorCount == 0) {
            return new Expression(randomFraction());
        }
        int leftCount = random.nextInt(operatorCount);
        int rightCount = operatorCount - 1 - leftCount;

        Expression left = build(leftCount);
        Expression right = build(rightCount);

        Expression.Type op = randomOperator();
        return new Expression(op, left, right);
    }

    private Fraction randomFraction() {
        if (random.nextBoolean()) {
            int n = random.nextInt(range);
            return new Fraction(n);
        } else {
            int den = random.nextInt(range - 1) + 2;
            int num = random.nextInt(den - 1) + 1;
            return new Fraction(num, den);
        }
    }

    private Expression.Type randomOperator() {
        int t = random.nextInt(4);
        switch (t) {
            case 0: return Expression.Type.ADD;
            case 1: return Expression.Type.SUBTRACT;
            case 2: return Expression.Type.MULTIPLY;
            default: return Expression.Type.DIVIDE;
        }
    }

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
                if (l.getNumerator() == 0 || r.getNumerator() == 0) {
                    return false;
                }
                break;
            case DIVIDE:
                if (r.getNumerator() == 0) {
                    return false;
                }
                if (l.getNumerator() == 0) {
                    return false;
                }
                Fraction result = l.divide(r);
                if (result.getNumerator() >= result.getDenominator()) {
                    return false;
                }
                break;
            case SUBTRACT:
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