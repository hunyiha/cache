package com.example.guava.cache.controller;

import com.example.guava.cache.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: guava->UserController
 * @description:
 * @author: hunyiha
 * @create: 2020-05-31 23:16
 **/

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/list/{location}")
    public List<String> listUser(@PathVariable(value="location") String location){
        return userService.listUser(location);
    }

    @GetMapping("/change")
    public int change(){
        return userService.change();
    }

    @GetMapping("/change/{location}")
    public int change(@PathVariable(value = "location") String location ){
        return userService.change(location);
    }

    @GetMapping("/refresh/{location}")
    public void refresh(@PathVariable("location") String location){
        userService.refresh(location);
    }
}
