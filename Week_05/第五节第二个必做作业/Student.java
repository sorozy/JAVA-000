package com.zhao.javalesson;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("com.zhao.javalesson")
public class Student {
    private int no =1;
    private int score=100;
    private String name="zhapzhen";

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
