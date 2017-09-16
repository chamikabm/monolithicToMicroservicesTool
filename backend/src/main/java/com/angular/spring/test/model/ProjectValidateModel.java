package com.angular.spring.test.model;

import java.io.File;

public class ProjectValidateModel {

    private boolean isValid;
    private File projectFile;
    private String[] projectFilesList;
    private String message;

    public ProjectValidateModel(boolean isValid, File projectFile,
                                String[] projectFilesList, String message) {
        this.isValid = isValid;
        this.projectFile = projectFile;
        this.projectFilesList = projectFilesList;
        this.message = message;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public File getProjectFile() {
        return projectFile;
    }

    public void setProjectFile(File projectFile) {
        this.projectFile = projectFile;
    }

    public String[] getProjectFilesList() {
        return projectFilesList;
    }

    public void setProjectFilesList(String[] projectFilesList) {
        this.projectFilesList = projectFilesList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
