package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 程序入口：小学四则运算题目生成器
 *
 * 用法：
 *   java -jar main.jar -n 10          生成 10 道题
 *   java -jar main.jar -n 10 -r 20    生成 10 道题，数值范围 0-19
 *   java -jar main.jar -e Exercises.txt -a Answers.txt   判分
 */
public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            System.exit(1);
        }

        try {
            if (contains(args, "-e") && contains(args, "-a")) {
                String exerciseFile = getArgValue(args, "-e");
                String answerFile = getArgValue(args, "-a");
                grade(exerciseFile, answerFile);
                return;
            }

            if (!contains(args, "-n")) {
                LOGGER.severe("缺少 -n 参数");
                printUsage();
                System.exit(1);
            }

            int n = Integer.parseInt(getArgValue(args, "-n"));
            int r = contains(args, "-r") ? Integer.parseInt(getArgValue(args, "-r")) : 10;

            generate(n, r);

        } catch (Exception e) {
            LOGGER.severe("程序异常: " + e.getMessage());
            System.exit(2);
        }
    }

    private static void generate(int n, int r) throws IOException {
        ExpressionGenerator generator = new ExpressionGenerator(r);
        List<Expression> expressions = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            expressions.add(generator.generate());
        }

        List<String> exerciseLines = FileIO.buildExerciseLines(expressions);
        List<String> answerLines = FileIO.buildAnswerLines(expressions);

        FileIO.writeLines("Exercises.txt", exerciseLines);
        FileIO.writeLines("Answers.txt", answerLines);

        LOGGER.info(() -> "已生成 " + n + " 道题目，范围 0-" + (r - 1));
        LOGGER.info("题目文件: Exercises.txt");
        LOGGER.info("答案文件: Answers.txt");
    }

    private static void grade(String exerciseFile, String answerFile) throws IOException {
        List<String> exerciseLines = FileIO.readLines(exerciseFile);
        List<String> answerLines = FileIO.readLines(answerFile);

        List<String> result = Grader.grade(exerciseLines, answerLines);
        FileIO.writeLines("Grade.txt", result);

        LOGGER.info("判分完成，结果已写入 Grade.txt");
    }

    private static boolean contains(String[] args, String key) {
        for (String arg : args) {
            if (arg.equals(key)) return true;
        }
        return false;
    }

    private static String getArgValue(String[] args, String key) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals(key)) {
                return args[i + 1];
            }
        }
        throw new IllegalArgumentException("参数 " + key + " 缺少值");
    }

    private static void printUsage() {
        LOGGER.info("用法:");
        LOGGER.info("  生成题目: java -jar main.jar -n <数量> [-r <范围>]");
        LOGGER.info("  判分:     java -jar main.jar -e <题目文件> -a <答案文件>");
        LOGGER.info("示例:");
        LOGGER.info("  java -jar main.jar -n 10");
        LOGGER.info("  java -jar main.jar -n 10 -r 20");
        LOGGER.info("  java -jar main.jar -e Exercises.txt -a Answers.txt");
    }
}