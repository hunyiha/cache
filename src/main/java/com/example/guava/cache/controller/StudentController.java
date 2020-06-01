package com.example.guava.cache.controller;

import com.example.guava.cache.pojo.Student;
import com.example.guava.cache.service.StudentService;
import com.example.guava.cache.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @program: guava->StudentController
 * @description:
 * @author: hunyiha
 * @create: 2020-06-01 21:33
 **/

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/{location}")
    public List<Student> getListStudent(@PathVariable("location") String location){
        try {
            return studentService.listStudent(location);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/delete")
    public boolean delete(String location, String uid){
       return studentService.delete(location, uid);
    }

}
