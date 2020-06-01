package com.example.guava.cache.service;


import com.example.guava.cache.pojo.Student;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface StudentService {

    List<Student> listStudent(String location) throws ExecutionException;

    boolean delete(String localtion,String uid);

    void refresh(String location);
}
