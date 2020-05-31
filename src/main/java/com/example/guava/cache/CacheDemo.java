package com.example.guava.cache;

import cn.hutool.core.thread.ThreadUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.junit.Test;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @program: guava->CacheDemo
 * @description:
 * @author: hunyiha
 * @create: 2020-05-31 22:13
 **/
public class CacheDemo {

    @Test
    public void testExpireAndRefresh1() {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(executor);

        AtomicInteger count = new AtomicInteger(0);
        LoadingCache<String, Integer> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.SECONDS)
                .refreshAfterWrite(1, TimeUnit.SECONDS)
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String key) throws Exception {
                        return count.getAndIncrement();
                    }

                    @Override
                    public ListenableFuture<Integer> reload(String key, Integer oldValue) throws Exception {
                        return listeningExecutorService.submit(()-> count.getAndIncrement()+10000);
                    }
                });
        IntStream.range(0, 10).forEach(i -> {
            System.out.println(cache.getUnchecked(""));
            ThreadUtil.safeSleep(1000);
        });
    }


    @Test
    public void testExpireAndRefresh2() {
        AtomicInteger count = new AtomicInteger(0);

        ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());

        LoadingCache<String, Integer> cache = CacheBuilder.newBuilder()
                .refreshAfterWrite(1, TimeUnit.SECONDS)
                .build(CacheLoader.asyncReloading(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String key) throws Exception {
                        return count.getAndIncrement();
                    }

                }, executor));

    }

}
