package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Fraction 单元测试
 */
class FractionTest {

    @Test
    void testAdd() {
        Fraction a = new Fraction(1, 6);
        Fraction b = new Fraction(1, 8);
        Fraction result = a.add(b);
        assertEquals(new Fraction(7, 24), result);
    }

    @Test
    void testSubtract() {
        Fraction a = new Fraction(3, 4);
        Fraction b = new Fraction(1, 4);
        Fraction result = a.subtract(b);
        assertEquals(new Fraction(1, 2), result);
    }

    @Test
    void testMultiply() {
        Fraction a = new Fraction(2, 3);
        Fraction b = new Fraction(3, 4);
        Fraction result = a.multiply(b);
        assertEquals(new Fraction(1, 2), result);
    }

    @Test
    void testDivide() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(1, 4);
        Fraction result = a.divide(b);
        assertEquals(new Fraction(2), result);
    }

    @Test
    void testSimplify() {
        Fraction f = new Fraction(4, 8);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());
    }

    @Test
    void testToStringProper() {
        Fraction f = new Fraction(3, 5);
        assertEquals("3/5", f.toString());
    }

    @Test
    void testToStringMixed() {
        Fraction f = new Fraction(19, 8);
        assertEquals("2'3/8", f.toString());
    }

    @Test
    void testCompareTo() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(1, 3);
        assertTrue(a.compareTo(b) > 0);
    }
}
