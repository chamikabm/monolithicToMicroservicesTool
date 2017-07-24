package com.angular.spring.test.controller;


import com.angular.spring.test.model.Hero;
import com.angular.spring.test.model.MicroService;
import com.angular.spring.test.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.IOUtils;

import java.io.*;
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
    public String fileUploader(@RequestParam("file") MultipartFile uploader) throws IOException {

        InputStream uploadedInputStream = uploader.getInputStream();


        // save it
        writeToFile(uploadedInputStream, "../../../../temp");


        return "file";
    }

    // save uploaded file to new location
    private void writeToFile(InputStream uploadedInputStream,
                             String uploadedFileLocation) {

        try {
            OutputStream out = new FileOutputStream(new File(
                    uploadedFileLocation));
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(uploadedFileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

}
