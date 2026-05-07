package com.zhixinsiwei.evaluation_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName ThreadPoolConfig.java
 * @Description TODO
 * @createTime 2026年01月23日 22:51:00
 */
@Configuration
public class ThreadPoolConfig {

    @Bean("reportExecutor")
    public ThreadPoolExecutor reportExecutor() {
        return new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),        // 核心线程数
                Runtime.getRuntime().availableProcessors() * 2,    // 最大线程数
                60L,                                               // 空闲存活时间
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(200),                    // 有界队列，防止OOM
                new ThreadFactory() {
                    private final AtomicInteger index = new AtomicInteger(1);

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r);
                        t.setName("report-worker-" + index.getAndIncrement());
                        t.setDaemon(false);
                        return t;
                    }
                },
                new ThreadPoolExecutor.CallerRunsPolicy()          // 拒绝策略：降级执行
        );
    }
}

