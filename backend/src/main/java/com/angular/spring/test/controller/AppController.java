package com.angular.spring.test.controller;


import com.angular.spring.test.model.Hero;
import com.angular.spring.test.model.MicroService;
import com.angular.spring.test.service.AppService;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


@RestController
public class AppController {

    private final AppService appService;

    @Autowired
    public AppController(AppService appService) {
        this.appService = appService;
    }

    @RequestMapping("/welcome")
    public ResponseEntity<String> welcome(@RequestParam(value="name", required=false, defaultValue="World") String name) {
        String welcomeMessage = appService.getWelcomeMessage(name);

        return new ResponseEntity<>(welcomeMessage, HttpStatus.OK);
    }

    @RequestMapping("/services")
    public ResponseEntity services() {
        List<MicroService> microService = appService.getAllServices();

        return new ResponseEntity<>(microService, HttpStatus.OK);
    }

    @RequestMapping("/heroes")
    public ResponseEntity heroes() {
        List<Hero> heroes = appService.getAllHeroes();

        return new ResponseEntity<>(heroes, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value="/uploader")
    @ResponseBody
    public ResponseEntity fileUploader(@RequestParam("file") MultipartFile file) {



        return new ResponseEntity<>(HttpStatus.OK);
    }



}
