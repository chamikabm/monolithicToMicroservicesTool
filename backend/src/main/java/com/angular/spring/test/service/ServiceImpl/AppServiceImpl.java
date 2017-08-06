package com.angular.spring.test.service.ServiceImpl;

import com.angular.spring.test.model.Hero;
import com.angular.spring.test.model.MicroService;
import com.angular.spring.test.model.RiskLevel;
import com.angular.spring.test.service.AppService;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AppServiceImpl implements AppService {

    @Override
    public String getWelcomeMessage(String name) {
        return "Hello!! " + name;
    }

    @Override
    public List<MicroService> getAllServices() {
        MicroService ms1 = new MicroService();
        ms1.setId(1);
        ms1.setServiceName("MS1");
        ms1.setRiskLevel(RiskLevel.HIGH_RISK);

        MicroService ms2 = new MicroService();
        ms2.setId(2);
        ms2.setServiceName("MS2");
        ms2.setRiskLevel(RiskLevel.LOW_RISK);

        MicroService ms3 = new MicroService();
        ms3.setId(3);
        ms3.setServiceName("MS3");
        ms3.setRiskLevel(RiskLevel.MEDIUM_RISK);

        MicroService ms4 = new MicroService();
        ms4.setId(4);
        ms4.setServiceName("MS4");
        ms4.setRiskLevel(RiskLevel.NO_RISK);

        return new ArrayList<MicroService>() {{
            add(ms1);
            add(ms2);
            add(ms3);
            add(ms4);
        }};
    }

    @Override
    public List<Hero> getAllHeroes() {
        Hero hero1 = new Hero();
        hero1.setId(1);
        hero1.setName("Chamika");

        Hero hero2 = new Hero();
        hero2.setId(1);
        hero2.setName("Chamika");

        Hero hero3 = new Hero();
        hero3.setId(1);
        hero3.setName("Chamika");

        Hero hero4 = new Hero();
        hero4.setId(1);
        hero4.setName("Chamika");

        Hero hero5 = new Hero();
        hero5.setId(1);
        hero5.setName("Chamika");

        Hero hero6 = new Hero();
        hero6.setId(1);
        hero6.setName("Chamika");

        Hero hero7 = new Hero();
        hero7.setId(1);
        hero7.setName("Chamika");

        Hero hero8 = new Hero();
        hero8.setId(1);
        hero8.setName("Chamika");

        return new ArrayList<Hero>() {{
            add(hero1);
            add(hero2);
            add(hero3);
            add(hero4);
            add(hero5);
            add(hero6);
            add(hero7);
            add(hero8);
        }};
    }

    @Override
    public void saveProjectFiles(MultipartFile file) {
        String destination = "/home/chamika/Projects/monolithicToMicroservicesTool/aa";

        /**
         * save file to temp
         */
        File zip = null;
        try {
            zip = File.createTempFile(UUID.randomUUID().toString(), "temp");
            FileOutputStream o = new FileOutputStream(zip);
            IOUtils.copy(file.getInputStream(), o);
            o.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



        /**
         * unizp file from temp by zip4j
         */

        try {
            ZipFile zipFile = new ZipFile(zip);
            zipFile.extractAll(destination);
        } catch (ZipException e) {
            e.printStackTrace();
        } finally {
            /**
             * delete temp file
             */
            zip.delete();
        }
    }

    @Override
    public void process() {
        //Test code to delay the response.
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        // Do Nothing
                    }
                },
                10000
        );
    }
}
