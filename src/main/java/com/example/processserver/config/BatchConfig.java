package com.example.processserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class BatchConfig {

    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);    // Número básico de hilos
        executor.setMaxPoolSize(5);    // Número máximo de hilos
        executor.setQueueCapacity(500); // Aumentar la capacidad de la cola
        executor.setThreadNamePrefix("TaskExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // Para manejar tareas rechazadas
        executor.initialize();
        return executor;
    }
}