package com.angular.spring.test.manager.models;

import com.angular.spring.test.Algorithm.Clustering.Distance;
import com.angular.spring.test.manager.FitnessFunctionManager;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.List;
import static com.angular.spring.test.ProjectConstants.*;

public class MicroService {

    private String name;
    private Double FValue;
    private Double fValue;
    private Double cValue;
    private Double sValue;
    private Double uValue;
    private String parent;
    private List<String> children;
    private List<String> leafNames;
    private Distance distance = new Distance();
    private ClassOrInterfaceDeclaration  classOrInterfaceDeclarationInfo;
    private NodeList<BodyDeclaration<?>> members;
    private List<FieldDeclaration> fields;
    private NodeList<ClassOrInterfaceType> interfaces;

    public MicroService(String name) {
        if (name.contains("-")) {
            this.name = name;
            updateValues(this.name);
        } else {
            this.name = name.replace("ServiceImpl", " Service");
            if (!this.getName().equals(this.parent)) {
                this.parent = name;
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getFValue() {
        return FValue;
    }

    public void setFValue(Double FValue) {
        this.FValue = FValue;
    }

    public Double getfValue() {
        return fValue;
    }

    public void setfValue(Double fValue) {
        this.fValue = fValue;
    }

    public Double getcValue() {
        return cValue;
    }

    public void setcValue(Double cValue) {
        this.cValue = cValue;
    }

    public Double getsValue() {
        return sValue;
    }

    public void setsValue(Double sValue) {
        this.sValue = sValue;
    }

    public Double getuValue() {
        return uValue;
    }

    public void setuValue(Double uValue) {
        this.uValue = uValue;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }

    public List<String> getLeafNames() {
        return leafNames;
    }

    public void setLeafNames(List<String> leafNames) {
        this.leafNames = leafNames;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "MicroServiceCluster{" +
                "name='" + name + '\'' +
                '}';
    }

    private int getServiceNamesCount (String name) {
        String temp = name.replace("Service", "");
        return (name.length() - temp.length()) / name.length();
    }

    private String getServiceName(String name) {

        String temp =  name.replace("ServiceImpl.java", " Service");;
        String[] servicesNames = temp.split("Service");
        StringBuilder serviceName = new StringBuilder();
        for(String tempName : servicesNames) {
            serviceName.append(tempName).append("- Service ");
        }

        return serviceName.toString().trim();
    }

    private void updateValues(String name) {
        String[] servicesList = name.split("Service");

        if (servicesList.length == DEPTH) {
            updateChildrenNames();
        } else {
            updateChildrenList(name);
        }


        FitnessFunctionManager fitnessFunctionManager = new FitnessFunctionManager();


        this.FValue = fitnessFunctionManager.calculateFitnessFunction(ALPHA, BETA, GAMMA, DELTA, this);
        this.distance.setDistance(this.FValue);
    }

    private void updateChildrenNames () {
        if (this.children != null) {
            this.leafNames = this.children;
        }
    }

    private void updateChildrenList (String name) {
        //Do nothing.
        //This has been updated while the clusters are being populating.
    }

    private boolean containsService(List<String> list, String name) {
        return list.stream().anyMatch(str -> str.trim().equals(name));
    }

    public void setClassDeclarationInfo(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        this.classOrInterfaceDeclarationInfo = classOrInterfaceDeclaration;
    }

    public ClassOrInterfaceDeclaration getClassOrInterfaceDeclarationInfo() {
        return classOrInterfaceDeclarationInfo;
    }

    public void setMembers(NodeList<BodyDeclaration<?>> members) {
        this.members = members;
    }

    public NodeList<BodyDeclaration<?>> getMembers() {
        return members;
    }

    public void setFields(List<FieldDeclaration> fields) {
        this.fields = fields;
    }

    public List<FieldDeclaration> getFields() {
        return fields;
    }

    public void setInterfaces(NodeList<ClassOrInterfaceType> interfaces) {
        this.interfaces = interfaces;
    }

    public NodeList<ClassOrInterfaceType> getInterfaces() {
        return interfaces;
    }
}
