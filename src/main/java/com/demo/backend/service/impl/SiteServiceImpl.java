package com.demo.backend.service.impl;

import com.demo.backend.model.Log;
import com.demo.backend.repository.LogRepository;
import com.demo.backend.service.SiteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SiteServiceImpl implements SiteService {

    @Resource
    LogRepository logRepository;

    @Override
    public void saveLog(Log log) {
        logRepository.save(log);
    }
}
