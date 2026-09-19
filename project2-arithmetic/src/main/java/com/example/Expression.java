package com.example;

/**
 * 表达式节点，支持叶子（数字）和内部节点（运算符 + 左右子树）
 */
public class Expression {

    public enum Type {
        NUMBER, ADD, SUBTRACT, MULTIPLY, DIVIDE
    }

    private final Type type;
    private final Fraction value;
    private final Expression left;
    private final Expression right;

    public Expression(Fraction value) {
        this.type = Type.NUMBER;
        this.value = value;
        this.left = null;
        this.right = null;
    }

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