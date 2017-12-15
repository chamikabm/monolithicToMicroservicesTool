package com.angular.spring.test.service.ServiceImpl;

import com.angular.spring.test.manager.ProcessManager;
import com.angular.spring.test.model.MicroService;
import com.angular.spring.test.model.ProjectValidateModel;
import com.angular.spring.test.service.AppService;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AppServiceImpl implements AppService {

    @Override
    public String getWelcomeMessage(String name) {
        return "Hello!! " + name;
    }

    @Override
    public void saveProjectFiles(MultipartFile file) {
        String destination = "/home/chamika/Projects/monolithicToMicroservicesTool/uploadedProjectFiles";

        cleanFileDirectory(new File(destination));

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
    public List<MicroService> process() throws RuntimeException {
        System.out.println("Analyzing started..." + new Date());


            ProcessManager processManager = new ProcessManager();
            ProjectValidateModel projectValidateModel = processManager.validateProjectStructure();
            System.out.println("Is Valid : " + projectValidateModel.isValid());
            if (projectValidateModel.isValid()) {
                List<MicroService> microServices = processManager.getAllMicroServices(projectValidateModel.getProjectFile());

                if (microServices == null) {
                    throw new RuntimeException("Zero Micro Services Found!");
                } else {
                    return microServices;
                }

            } else {
                throw new RuntimeException(projectValidateModel.getMessage());
            }
    }

    private void cleanFileDirectory (File directory) {
        if (directory.isDirectory()) {
            try {
                System.out.println("Directory cleaned up started..");
                FileUtils.cleanDirectory(directory);
                System.out.println("Directory cleaned up completed..");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
