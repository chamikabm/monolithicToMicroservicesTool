package com.angular.spring.test.controller;

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

    @RequestMapping(method = RequestMethod.POST, value="/uploader")
    @ResponseBody
    public ResponseEntity fileUploader(@RequestParam("file") MultipartFile file) {
        appService.saveProjectFiles(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value="/process")
    @ResponseBody
    public ResponseEntity process() {

        try {
            List<MicroService> microServices = appService.process();

            return new ResponseEntity<>(microServices, HttpStatus.OK);
        } catch (RuntimeException e){

            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
