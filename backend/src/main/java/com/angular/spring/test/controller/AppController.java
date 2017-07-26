package com.angular.spring.test.controller;


import com.angular.spring.test.model.Hero;
import com.angular.spring.test.model.MicroService;
import com.angular.spring.test.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
        appService.saveProjectFiles(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value="/process")
    @ResponseBody
    public ResponseEntity process() {

        appService.process();
        List<MicroService> microServices = appService.getAllServices();

        return new ResponseEntity<>(microServices, HttpStatus.OK);
    }


}
