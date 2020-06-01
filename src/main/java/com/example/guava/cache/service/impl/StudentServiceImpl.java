package com.example.guava.cache.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.example.guava.cache.pojo.Student;
import com.example.guava.cache.service.StudentService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: guava->StudentServiceImpl
 * @description:
 * @author: hunyiha
 * @create: 2020-06-01 21:28
 **/

@Service
public class StudentServiceImpl implements StudentService {

    ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(5));

    AtomicInteger atomic = new AtomicInteger(0);

    LoadingCache<String, List<Student>> cache = CacheBuilder.newBuilder()
            .refreshAfterWrite(5, TimeUnit.SECONDS)
            .build(CacheLoader.asyncReloading(new CacheLoader<String, List<Student>>() {
                @Override
                public List<Student> load(String key) throws Exception {
                    return getListStudent(key);
                }

            }, service));


    @Override
    public List<Student> listStudent(String location) throws ExecutionException {
        return cache.get(location);
    }

    @Override
    public boolean delete(String location, String uid) {
        try {
            List<Student> students = cache.get(location);
            for (int i = 0; i < students.size(); i++){
                if(students.get(i).getUid().equals(uid)){
                    students.remove(i);
                }
            }
            return true;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void refresh(String location) {
        cache.refresh(location);
    }

    private List<Student> getListStudent(String key){
        ArrayList<Student> studentList = Lists.newArrayList();
        for(int i = 0; i < 20; i++){
            studentList.add(new Student(i+"", "student" + i, i + 20, key, i +70.0));
        }
        ThreadUtil.safeSleep(20000);
        return studentList;
    }
}
