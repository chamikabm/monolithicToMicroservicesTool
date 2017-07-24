package com.angular.spring.test.service;

import com.angular.spring.test.model.MicroService;

import java.util.List;

public interface AppService {
    String getWelcomeMessage(String name);

    List<MicroService> getAllServices();
}
