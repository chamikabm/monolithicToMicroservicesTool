package com.angular.spring.test.service;

import com.angular.spring.test.model.MicroService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AppService {
    String getWelcomeMessage(String name);

    void saveProjectFiles(MultipartFile file);

    List<MicroService> process() throws IOException;
}
