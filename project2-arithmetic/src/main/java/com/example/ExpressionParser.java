package com.example;

/**
 * 表达式解析器：把字符串形式的四则运算表达式解析成表达式树
 * 支持：+ - × ÷、括号、分数、带分数
 */
public class ExpressionParser {

    private ExpressionParser() {
        // 工具类，禁止实例化
    }

    private static String input;
    private static int pos;

    /**
     * 解析表达式
     */
    public static Expression parse(String expr) {
        input = expr.replace(" ", "");
        pos = 0;
        Expression result = parseExpression();
        if (pos != input.length()) {
            throw new IllegalArgumentException("解析失败，位置: " + pos);
        }
        return result;
    }

    /**
     * 加减法（最低优先级）
     */
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

    /**
     * 乘除法（较高优先级）
     */
    private static Expression parseTerm() {
        Expression left = parseFactor();
        while (pos < input.length()) {
            char c = input.charAt(pos);
            if (c == '×' || c == '÷'|| c == '*' || c == '/') {
                pos++;
                Expression right = parseFactor();
                Expression.Type type = (c == '×'|| c == '*') ? Expression.Type.MULTIPLY : Expression.Type.DIVIDE;
                left = new Expression(type, left, right);
            } else {
                break;
            }
        }
        return left;
    }

    /**
     * 因子：数字、括号表达式
     */
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

    /**
     * 解析数字：支持 "3", "3/5", "2'3/8"
     */
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