package com.angular.spring.test.model;

import java.util.List;

public class MicroService {

    private int id;
    private String serviceName;
    private RiskLevel riskLevel;
    private List<Class> classes;

    public MicroService() {
    }

    public MicroService(int id, String serviceName, RiskLevel riskLevel) {
        this.id = id;
        this.serviceName = serviceName;
        this.riskLevel = riskLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }


    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }

    @Override
    public String toString() {
        return "MicroService{" +
                "id=" + id +
                ", serviceName='" + serviceName + '\'' +
                ", riskLevel=" + riskLevel +
                '}';
    }
}
