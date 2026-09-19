package com.example;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Grader 单元测试
 */
class GraderTest {

    @Test
    void testGradeAllCorrect() {
        List<String> exercises = Arrays.asList(
                "1. 1/2 + 1/3 =",
                "2. 3/4 - 1/4 ="
        );
        List<String> answers = Arrays.asList(
                "1. 1/2 + 1/3 = 5/6",
                "2. 3/4 - 1/4 = 1/2"
        );
        List<String> result = Grader.grade(exercises, answers);
        assertEquals("Correct: 2 (1, 2)", result.get(0));
        assertEquals("Wrong: 0 ()", result.get(1));
    }

    @Test
    void testGradePartiallyCorrect() {
        List<String> exercises = Arrays.asList(
                "1. 1/2 + 1/3 =",
                "2. 3/4 - 1/4 ="
        );
        List<String> answers = Arrays.asList(
                "1. 1/2 + 1/3 = 5/6",
                "2. 3/4 - 1/4 = 1/3"
        );
        List<String> result = Grader.grade(exercises, answers);
        assertEquals("Correct: 1 (1)", result.get(0));
        assertEquals("Wrong: 1 (2)", result.get(1));
    }
}