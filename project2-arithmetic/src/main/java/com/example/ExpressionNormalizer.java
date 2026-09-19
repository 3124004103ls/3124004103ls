package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * 表达式规范化：用于判断两道题是否重复。
 */
public class ExpressionNormalizer {

    private ExpressionNormalizer() {
    }

    public static String normalize(Expression expr) {
        if (expr.isNumber()) {
            return expr.getValue().toString();
        }

        String left = normalize(expr.getLeft());
        String right = normalize(expr.getRight());

        switch (expr.getType()) {
            case ADD:
                return normalizeAssociative("+", left, right);
            case MULTIPLY:
                return normalizeAssociative("*", left, right);
            case SUBTRACT:
                return "(" + left + "-" + right + ")";
            case DIVIDE:
                return "(" + left + "/" + right + ")";
            default:
                return "?";
        }
    }

    private static String normalizeAssociative(String op, String left, String right) {
        List<String> operands = new ArrayList<>();
        collectOperands(op, left, operands);
        collectOperands(op, right, operands);
        operands.sort(String::compareTo);
        return "(" + String.join(op, operands) + ")";
    }

    private static void collectOperands(String op, String expr, List<String> operands) {
        if (expr.startsWith("(") && expr.endsWith(")")) {
            String inner = expr.substring(1, expr.length() - 1);
            List<String> parts = splitByOperator(inner, op);
            if (parts != null && parts.size() > 1) {
                operands.addAll(parts);
                return;
            }
        }
        operands.add(expr);
    }

    private static List<String> splitByOperator(String s, String op) {
        List<String> parts = new ArrayList<>();
        int depth = 0;
        StringBuilder cur = new StringBuilder();
        char opChar = op.charAt(0);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') depth++;
            if (c == ')') depth--;
            if (depth == 0 && c == opChar) {
                parts.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        parts.add(cur.toString());
        return parts;
    }
}