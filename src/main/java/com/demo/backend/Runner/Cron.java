package com.demo.backend.Runner;

import com.alibaba.fastjson.JSON;
import com.demo.backend.config.DynamicTaskScheduler;
import com.demo.backend.model.Log;
import com.demo.backend.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Order(1)
public class Cron implements ApplicationRunner {

    @Resource
    private SiteService siteService;


    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    DynamicTaskScheduler dynamicTaskScheduler;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {


        String sync = "0 0 23 * * ?";

        dynamicTaskScheduler.startCron(sync, "syncTeacherScheduler", () -> {

            Log log = new Log();
            siteService.saveLog(log);

        });


        System.out.println("启动执行定时任务、");
    }
}
