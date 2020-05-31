package com.example.guava.cache.service;

import java.util.List;

public interface UserService {

    List<String> listUser(String location);

    int change();

    int change(String location);

    void refresh(String location);
}
