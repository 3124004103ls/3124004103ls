package com.example;

/**
 * 分数类，支持加减乘除、化简、比较、转字符串
 */
public class Fraction {

    private final int numerator;    // 分子
    private final int denominator;  // 分母

    public Fraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("分母不能为 0");
        }
        // 保证分母为正
        if (denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }
        int gcd = gcd(Math.abs(numerator), denominator);
        this.numerator = numerator / gcd;
        this.denominator = denominator / gcd;
    }

    public Fraction(int numerator) {
        this(numerator, 1);
    }

    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    /**
     * 加法
     */
    public Fraction add(Fraction other) {
        int num = this.numerator * other.denominator + other.numerator * this.denominator;
        int den = this.denominator * other.denominator;
        return new Fraction(num, den);
    }

    /**
     * 减法
     */
    public Fraction subtract(Fraction other) {
        int num = this.numerator * other.denominator - other.numerator * this.denominator;
        int den = this.denominator * other.denominator;
        return new Fraction(num, den);
    }

    /**
     * 乘法
     */
    public Fraction multiply(Fraction other) {
        return new Fraction(this.numerator * other.numerator,
                this.denominator * other.denominator);
    }

    /**
     * 除法
     */
    public Fraction divide(Fraction other) {
        if (other.numerator == 0) {
            throw new ArithmeticException("除数不能为 0");
        }
        return new Fraction(this.numerator * other.denominator,
                this.denominator * other.numerator);
    }

    /**
     * 比较大小
     */
    public int compareTo(Fraction other) {
        int left = this.numerator * other.denominator;
        int right = other.numerator * this.denominator;
        return Integer.compare(left, right);
    }

    /**
     * 求最大公约数
     */
    private static int gcd(int a, int b) {
        while (b != 0) {
            int t = b;
            b = a % b;
            a = t;
        }
        return a == 0 ? 1 : a;
    }

    /**
     * 转字符串：真分数用 3/5，带分数用 2'3/8
     */
    @Override
    public String toString() {
        if (denominator == 1) {
            return String.valueOf(numerator);
        }
        if (Math.abs(numerator) > denominator) {
            int whole = numerator / denominator;
            int rest = Math.abs(numerator % denominator);
            return whole + "'" + rest + "/" + denominator;
        }
        return numerator + "/" + denominator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fraction)) return false;
        Fraction f = (Fraction) o;
        return numerator == f.numerator && denominator == f.denominator;
    }

    @Override
    public int hashCode() {
        return 31 * numerator + denominator;
    }
}
