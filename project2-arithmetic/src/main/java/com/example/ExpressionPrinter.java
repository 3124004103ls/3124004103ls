package com.example;

/**
 * 表达式打印工具类：把表达式树转成带括号的字符串
 */
public class ExpressionPrinter {

    private ExpressionPrinter() {
        // 工具类，禁止实例化
    }

    /**
     * 打印表达式（顶层不加多余括号）
     */
    public static String print(Expression expr) {
        return print(expr, 0);
    }

    /**
     * 递归打印，parentPrecedence 表示父节点的优先级
     */
    private static String print(Expression expr, int parentPrecedence) {
        if (expr.isNumber()) {
            return expr.getValue().toString();
        }

        int currentPrecedence = precedence(expr.getType());
        String left = print(expr.getLeft(), currentPrecedence);
        String right = print(expr.getRight(), currentPrecedence + 1);

        String op;
        switch (expr.getType()) {
          case ADD: op = "+"; break;
          case SUBTRACT: op = "-"; break;
          case MULTIPLY: op = "*"; break;
          case DIVIDE: op = "/"; break;
          default: op = "?"; break;
        }
        String result = left + " " + op + " " + right;

        // 如果当前优先级低于父节点，需要加括号
        if (currentPrecedence < parentPrecedence) {
            return "(" + result + ")";
        }
        return result;
    }

    /**
     * 运算符优先级
     */
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