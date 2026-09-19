package com.example;

/**
 * 分数类，支持加减乘除、化简、比较、转字符串
 */
public class Fraction {

    private final int numerator;
    private final int denominator;

    public Fraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("分母不能为 0");
        }
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

    public Fraction add(Fraction other) {
        int num = this.numerator * other.denominator + other.numerator * this.denominator;
        int den = this.denominator * other.denominator;
        return new Fraction(num, den);
    }

    public Fraction subtract(Fraction other) {
        int num = this.numerator * other.denominator - other.numerator * this.denominator;
        int den = this.denominator * other.denominator;
        return new Fraction(num, den);
    }

    public Fraction multiply(Fraction other) {
        return new Fraction(this.numerator * other.numerator,
                this.denominator * other.denominator);
    }

    public Fraction divide(Fraction other) {
        if (other.numerator == 0) {
            throw new ArithmeticException("除数不能为 0");
        }
        return new Fraction(this.numerator * other.denominator,
                this.denominator * other.numerator);
    }

    public int compareTo(Fraction other) {
        int left = this.numerator * other.denominator;
        int right = other.numerator * this.denominator;
        return Integer.compare(left, right);
    }

    private static int gcd(int a, int b) {
        while (b != 0) {
            int t = b;
            b = a % b;
            a = t;
        }
        return a == 0 ? 1 : a;
    }

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