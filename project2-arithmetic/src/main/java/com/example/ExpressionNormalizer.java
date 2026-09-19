package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * 表达式规范化：用于判断两道题是否重复。
 * 规则：
 * - 加法、乘法满足交换律：a + b 和 b + a 视为相同
 * - 加法、乘法满足结合律：a + (b + c) 和 (a + b) + c 视为相同
 * - 减法、除法不满足交换律，左右顺序不能变
 */
public class ExpressionNormalizer {

    /**
     * 把表达式树规范化为字符串
     */
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
                return normalizeAssociative("×", left, right);
            case SUBTRACT:
                return "(" + left + "-" + right + ")";
            case DIVIDE:
                return "(" + left + "÷" + right + ")";
            default:
                return "?";
        }
    }

    /**
     * 处理加法和乘法的交换律、结合律：
     * - 把同一运算符的子表达式展平成列表
     * - 按字典序排序
     */
    private static String normalizeAssociative(String op, String left, String right) {
        List<String> operands = new ArrayList<>();
        collectOperands(op, left, operands);
        collectOperands(op, right, operands);
        operands.sort(String::compareTo);
        return "(" + String.join(op, operands) + ")";
    }

    /**
     * 如果字符串是同一运算符的表达式（形如 "(a+b)"），拆开它的操作数
     */
    private static void collectOperands(String op, String expr, List<String> operands) {
        // 判断 expr 是否被 () 包裹且内部由 op 连接
        if (expr.startsWith("(") && expr.endsWith(")")) {
            String inner = expr.substring(1, expr.length() - 1);
            // 按 op 分隔，且所有子部分都不含该 op
            List<String> parts = splitByOperator(inner, op);
            if (parts != null && parts.size() > 1) {
                operands.addAll(parts);
                return;
            }
        }
        operands.add(expr);
    }

    /**
     * 按运算符分割，如果遇到子表达式中包含该运算符（说明不是同一层级），返回 null
     */
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