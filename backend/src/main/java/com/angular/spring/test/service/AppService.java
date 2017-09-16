package com.angular.spring.test.service;

import com.angular.spring.test.model.Hero;
import com.angular.spring.test.model.MicroService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AppService {
    String getWelcomeMessage(String name);

    List<MicroService> getAllServices();

    List<Hero> getAllHeroes();

    void saveProjectFiles(MultipartFile file);

    List<MicroService> process();
}
