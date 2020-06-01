package com.example.guava.cache.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.example.guava.cache.pojo.Student;
import com.example.guava.cache.service.UserService;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import jdk.nashorn.internal.ir.CatchNode;
import org.apache.tomcat.jni.Time;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: guava->UserServiceImpl
 * @description:
 * @author: hunyiha
 * @create: 2020-05-31 23:25
 **/

@Service
public class UserServiceImpl implements UserService {

    ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(5));

    AtomicInteger atomic = new AtomicInteger(0);

    LoadingCache<String, List<String>> cache = CacheBuilder.newBuilder()
            .refreshAfterWrite(5, TimeUnit.SECONDS)
            .build(CacheLoader.asyncReloading(new CacheLoader<String, List<String>>() {
                @Override
                public List<String> load(String key) throws Exception {
                    return getUserList(key);
                }

            }, service));


    @Override
    public List<String> listUser(String location) {
        try {
            return cache.get(location);
        } catch (Throwable e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public int change() {
        return atomic.getAndIncrement();
    }

    public int change(String location){
        int increment = atomic.getAndIncrement();
        cache.refresh(location);
        return increment;
    }

    @Override
    public void refresh(String location) {
        cache.refresh(location);
    }

    private List<String> getUserList(String location){

        ArrayList<String> list = Lists.newArrayList();
        for (int i = atomic.get(); i < 50; i++) {
            list.add("list: " + location + i);
        }
 //       ThreadUtil.safeSleep(10000);
        return list;
    }


}
