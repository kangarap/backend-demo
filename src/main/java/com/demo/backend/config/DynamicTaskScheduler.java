package com.demo.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Configuration
public class DynamicTaskScheduler {

    public static ConcurrentHashMap<String, ScheduledFuture> map = new ConcurrentHashMap<>();

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private ScheduledFuture future;

    public void startCron(String cron, String taskName, Runnable runnable) {

        if(map.get(taskName) != null)
        {
            this.stop(taskName);
            this.stop(taskName);
        }
        future = threadPoolTaskScheduler.schedule(runnable, new CronTrigger(cron));
        map.put(taskName, future);
    }

    public void stop(String taskName) {

        ScheduledFuture scheduledFuture = map.get(taskName);

        if(scheduledFuture != null)
        {
            scheduledFuture.cancel(true);

            boolean cancelled = scheduledFuture.isCancelled();
            while (!cancelled) {
                scheduledFuture.cancel(true);
            }
        }
    }
}
