/*
 * Class defined to override default threads and timeout while straming the data
 */

package com.sts.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableAsync
public class Async {
	
	@Bean
    public WebMvcConfigurer webMvcConfigurer(ConcurrentTaskExecutor concurrentTaskExecutor) {
        return new WebMvcConfigurer() {
            @Override
            public void configureAsyncSupport(@NonNull AsyncSupportConfigurer configurer) {
                configurer.setDefaultTimeout(-1);
                configurer.setTaskExecutor(concurrentTaskExecutor);
            }
        };
    }

    @Bean
    public ConcurrentTaskExecutor concurrentTaskExecutor() {
        return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(5, new ThreadFactory() {
            private final AtomicInteger threadCounter = new AtomicInteger(0);

            @Override
            public Thread newThread(@NonNull Runnable runnable) {
                return new Thread(runnable, "asyncThread-" + threadCounter.incrementAndGet());
            }
        }));
    }
}
