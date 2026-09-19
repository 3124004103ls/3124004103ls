package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * 判分器：对比用户答案和标准答案，输出 Grade.txt
 */
public class Grader {

    private Grader() {
    }

    public static List<String> grade(List<String> exerciseLines, List<String> answerLines) {
        List<Integer> correct = new ArrayList<>();
        List<Integer> wrong = new ArrayList<>();

        int count = Math.min(exerciseLines.size(), answerLines.size());
        for (int i = 0; i < count; i++) {
            String exercise = exerciseLines.get(i);
            String userAnswer = answerLines.get(i);

            Fraction standard = evaluateExercise(exercise);
            Fraction user = parseAnswer(userAnswer);

            if (standard != null && standard.equals(user)) {
                correct.add(i + 1);
            } else {
                wrong.add(i + 1);
            }
        }

        List<String> result = new ArrayList<>();
        result.add("Correct: " + correct.size() + " " + formatIndices(correct));
        result.add("Wrong: " + wrong.size() + " " + formatIndices(wrong));
        return result;
    }

    private static String formatIndices(List<Integer> indices) {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < indices.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(indices.get(i));
        }
        sb.append(")");
        return sb.toString();
    }

    private static Fraction evaluateExercise(String line) {
        try {
            int dot = line.indexOf(". ");
            String expr = line.substring(dot + 2);
            if (expr.endsWith(" =")) {
                expr = expr.substring(0, expr.length() - 2);
            }
            expr = expr.replace(" ", "");
            return ExpressionParser.parse(expr).evaluate();
        } catch (Exception e) {
            return null;
        }
    }

    private static Fraction parseAnswer(String line) {
        try {
            int eq = line.lastIndexOf("= ");
            String ans = line.substring(eq + 2).trim();
            return parseFraction(ans);
        } catch (Exception e) {
            return null;
        }
    }

    private static Fraction parseFraction(String s) {
        if (s.contains("'")) {
            int idx = s.indexOf("'");
            int whole = Integer.parseInt(s.substring(0, idx));
            String rest = s.substring(idx + 1);
            int slash = rest.indexOf("/");
            int num = Integer.parseInt(rest.substring(0, slash));
            int den = Integer.parseInt(rest.substring(slash + 1));
            return new Fraction(whole * den + num, den);
        } else if (s.contains("/")) {
            int slash = s.indexOf("/");
            int num = Integer.parseInt(s.substring(0, slash));
            int den = Integer.parseInt(s.substring(slash + 1));
            return new Fraction(num, den);
        } else {
            return new Fraction(Integer.parseInt(s));
        }
    }
}