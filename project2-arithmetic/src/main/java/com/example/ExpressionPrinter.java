package com.example;

/**
 * 表达式打印工具类：把表达式树转成带括号的字符串
 */
public class ExpressionPrinter {

    private ExpressionPrinter() {
    }

    public static String print(Expression expr) {
        return print(expr, 0);
    }

    private static String print(Expression expr, int parentPrecedence) {
        if (expr.isNumber()) {
            return expr.getValue().toString();
        }

        int currentPrecedence = precedence(expr.getType());
        String left = print(expr.getLeft(), currentPrecedence);
        String right = print(expr.getRight(), currentPrecedence + 1);

        String result = left + " " + expr.getOperatorSymbol() + " " + right;

        if (currentPrecedence < parentPrecedence) {
            return "(" + result + ")";
        }
        return result;
    }

    private static int precedence(Expression.Type type) {
        switch (type) {
            case ADD:
            case SUBTRACT:
                return 1;
            case MULTIPLY:
            case DIVIDE:
                return 2;
            default:
                return 0;
        }
    }
}