# 论文查重程序

## 功能
输入原文和抄袭版论文，输出重复率（保留两位小数）。

## 运行方式
java -jar main.jar <原文文件> <抄袭版论文> <答案文件>

## 示例
java -jar main.jar C:\tests\orig.txt C:\tests\orig_add.txt C:\tests\ans.txt

## 算法
基于余弦相似度，中文按字 + bigram 分词。

## 构建
mvn clean package