package com.angular.spring.test.manager;

import com.angular.spring.test.model.MicroService;
import com.angular.spring.test.model.ProjectValidateModel;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ProcessManager {

    public ProjectValidateModel validateProjectStructure() {

        String projectRootLocation = System.getProperty("user.dir");
        System.out.println("Project Root Location : " + projectRootLocation);

        String filesRoot = "/Users/Chamikabandara/Projects/MyProjects/monolithicToMicroservicesTool/uploadedProjectFiles";
        System.out.println("files root : " + filesRoot);

        File file = new File(filesRoot);
        String[] names = file.list();

        assert names != null;
        if (names.length == 1) {
            File rootFile = new File(filesRoot + "/" + names[0]);

            if (rootFile.isDirectory()) {
                System.out.println("Root file directory found!");

                File projectRoot = new File(rootFile.getAbsolutePath());
                String[] projectFolders = projectRoot.list();
                if (Arrays.asList(projectFolders).contains("src")) {
                    System.out.println("src file directory found!");
                    File src = new File(projectRoot +"/" + "src");
                    String [] srcFolders = src.list();

                    if (Arrays.asList(srcFolders).contains("main") &&
                            Arrays.asList(srcFolders).contains("test")) {
                        System.out.println("main file directory found!");

                        File main = new File(src.getAbsolutePath() +"/" + "main");
                        String [] mainFolders = main.list();
                        if (Arrays.asList(mainFolders).contains("java") &&
                                Arrays.asList(mainFolders).contains("resources")) {
                            System.out.println("java file directory found!");

                            boolean isApiFound = true;
                            File nextFolder = getNextFolder(main.getAbsolutePath(), "java");
                            String [] nextFiles = null;
                            while (isApiFound) {
                                nextFiles = nextFolder.list();
                                if (nextFiles.length == 1) {
                                    String nextFolderName = nextFiles[0];
                                    nextFolder = getNextFolder(nextFolder.getAbsolutePath(), nextFolderName);
                                } else {
                                    isApiFound = false;
                                    String nextFolderName = nextFiles[0];
                                }
                            }

                            assert nextFiles != null;
                            for (String fileName : nextFiles) {
                                System.out.println("File : " + fileName);
                            }

                            return new ProjectValidateModel(true, nextFolder, nextFiles, "Valid Project Structure Found.");
                        } else {
                            System.out.println("No java or resources folder found!");
                            return new ProjectValidateModel(false, null, null, "Invalid Project Structure Found.");
                        }
                    } else {
                        System.out.println("No main or test folder found!");
                        return new ProjectValidateModel(false, null, null, "Invalid Project Structure Found.");
                    }
                } else {
                    System.out.println("No src root found!");
                    return new ProjectValidateModel(false, null, null, "Invalid Project Structure Found.");
                }
            } else {
                System.out.println("Root file directory not found!");
                return new ProjectValidateModel(false, null, null, "Invalid Project Structure Found.");
            }
        } else {
            System.out.println("Invalid project directory!");
            return new ProjectValidateModel(false, null, null, "Invalid Project Structure Found.");
        }
    }

    private File getNextFolder(String absolutePath, String nextPath) {
        return new File(absolutePath + "/" + nextPath);
    }

    public List<MicroService> getAllMicroServices(File projectFiles) {
        if (projectFiles == null) {
            return null;
        } else {
            System.out.println("Project files location : " + projectFiles.getAbsolutePath());

            ClusterManager clusterManager = new ClusterManager();
            return clusterManager.getMicroServiceCluster(projectFiles);
        }
    }

    public boolean isContainServiceFolder(String[] filesList) {
        return Arrays.asList(filesList).contains("Service");
    }
}

