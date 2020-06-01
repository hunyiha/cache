package com.example.guava.cache.pojo;

import lombok.Data;

/**
 * @program: guava->Student
 * @description:
 * @author: hunyiha
 * @create: 2020-06-01 21:04
 **/

@Data
public class Student {
    private String uid;
    private String name;
    private Integer age;
    private String location;
    private Double score;

    public Student() {
    }

    public Student(String uid, String name, Integer age, String location, Double score) {
        this.uid = uid;
        this.name = name;
        this.age = age;
        this.location = location;
        this.score = score;
    }
}
