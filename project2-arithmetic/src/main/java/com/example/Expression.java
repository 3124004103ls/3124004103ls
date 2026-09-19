package com.example;

/**
 * 表达式节点，支持叶子（数字）和内部节点（运算符 + 左右子树）
 */
public class Expression {

    /**
     * 节点类型
     */
    public enum Type {
        NUMBER,     // 数字
        ADD,        // 加法
        SUBTRACT,   // 减法
        MULTIPLY,   // 乘法
        DIVIDE      // 除法
    }

    private final Type type;
    private final Fraction value;       // 仅 NUMBER 节点有值
    private final Expression left;      // 左子树
    private final Expression right;     // 右子树

    /**
     * 构造数字节点
     */
    public Expression(Fraction value) {
        this.type = Type.NUMBER;
        this.value = value;
        this.left = null;
        this.right = null;
    }

    /**
     * 构造运算符节点
     */
    public Expression(Type type, Expression left, Expression right) {
        this.type = type;
        this.value = null;
        this.left = left;
        this.right = right;
    }

    public Type getType() {
        return type;
    }

    public Fraction getValue() {
        return value;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public boolean isNumber() {
        return type == Type.NUMBER;
    }

    /**
     * 计算表达式的值
     */
    public Fraction evaluate() {
        if (isNumber()) {
            return value;
        }
        Fraction l = left.evaluate();
        Fraction r = right.evaluate();
        switch (type) {
            case ADD:
                return l.add(r);
            case SUBTRACT:
                return l.subtract(r);
            case MULTIPLY:
                return l.multiply(r);
            case DIVIDE:
                return l.divide(r);
            default:
                throw new IllegalStateException("未知类型: " + type);
        }
    }

    /**
     * 获取运算符符号
     */
    public String getOperatorSymbol() {
        switch (type) {
            case ADD:
                return "+";
            case SUBTRACT:
                return "-";
            case MULTIPLY:
                return "*";
            case DIVIDE:
                return "/";
            default:
                return "";
        }
    }
}