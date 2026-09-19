package com.example;

/**
 * 表达式解析器：把字符串形式的四则运算表达式解析成表达式树
 */
public class ExpressionParser {

    private ExpressionParser() {
    }

    private static String input;
    private static int pos;

    public static Expression parse(String expr) {
        input = expr.replace(" ", "");
        pos = 0;
        Expression result = parseExpression();
        if (pos != input.length()) {
            throw new IllegalArgumentException("解析失败，位置: " + pos);
        }
        return result;
    }

    private static Expression parseExpression() {
        Expression left = parseTerm();
        while (pos < input.length()) {
            char c = input.charAt(pos);
            if (c == '+' || c == '-') {
                pos++;
                Expression right = parseTerm();
                Expression.Type type = (c == '+') ? Expression.Type.ADD : Expression.Type.SUBTRACT;
                left = new Expression(type, left, right);
            } else {
                break;
            }
        }
        return left;
    }

    private static Expression parseTerm() {
        Expression left = parseFactor();
        while (pos < input.length()) {
            char c = input.charAt(pos);
            if (c == '*' || c == '/') {
                pos++;
                Expression right = parseFactor();
                Expression.Type type = (c == '*') ? Expression.Type.MULTIPLY : Expression.Type.DIVIDE;
                left = new Expression(type, left, right);
            } else {
                break;
            }
        }
        return left;
    }

    private static Expression parseFactor() {
        if (pos >= input.length()) {
            throw new IllegalArgumentException("表达式不完整");
        }
        char c = input.charAt(pos);
        if (c == '(') {
            pos++;
            Expression inner = parseExpression();
            if (pos >= input.length() || input.charAt(pos) != ')') {
                throw new IllegalArgumentException("括号不匹配");
            }
            pos++;
            return inner;
        }
        return new Expression(parseNumber());
    }

    private static Fraction parseNumber() {
        int start = pos;
        boolean hasSlash = false;
        while (pos < input.length()) {
            char c = input.charAt(pos);
            if (Character.isDigit(c)) {
                pos++;
            } else if (c == '\'' && !hasSlash) {
                pos++;
            } else if (c == '/' && !hasSlash) {
                hasSlash = true;
                pos++;
            } else {
                break;
            }
        }
        String numStr = input.substring(start, pos);
        if (numStr.isEmpty()) {
            throw new IllegalArgumentException("未找到数字，位置: " + pos);
        }
        if (numStr.contains("'")) {
            int idx = numStr.indexOf("'");
            int whole = Integer.parseInt(numStr.substring(0, idx));
            String rest = numStr.substring(idx + 1);
            int slash = rest.indexOf("/");
            int num = Integer.parseInt(rest.substring(0, slash));
            int den = Integer.parseInt(rest.substring(slash + 1));
            return new Fraction(whole * den + num, den);
        } else if (numStr.contains("/")) {
            int slash = numStr.indexOf("/");
            int num = Integer.parseInt(numStr.substring(0, slash));
            int den = Integer.parseInt(numStr.substring(slash + 1));
            return new Fraction(num, den);
        } else {
            return new Fraction(Integer.parseInt(numStr));
        }
    }
}