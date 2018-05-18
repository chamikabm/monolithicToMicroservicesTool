package com.angular.spring.test.manager;

import com.angular.spring.test.Algorithm.Clustering.ClusteringAlgorithm;
import com.angular.spring.test.Algorithm.Clustering.DefaultClusteringAlgorithm;
import com.angular.spring.test.model.MicroService;
import com.angular.spring.test.model.RiskLevel;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClusterManager {

    List<MicroService> getMicroServiceCluster(File sourceFiles) throws IOException {

        String [] projectFiles = sourceFiles.list();
        String [] servicesList = null;
        ProcessManager processManager = new ProcessManager();
        File serviceFolder = null;

        if (processManager.isContainServiceFolder(projectFiles)) {
            System.out.println("Service folder found");

            serviceFolder = new File(sourceFiles.getAbsolutePath() + "/" + "Service");
            servicesList = serviceFolder.list();
        }

        ClusteringAlgorithm clusteringAlgorithm = new DefaultClusteringAlgorithm();
        return getMicroServicesFromCluster(
                clusteringAlgorithm.performClustering(
                        servicesList,
                        serviceFolder.getAbsolutePath()
        ));
    }

    public List<MicroService> getMicroServicesFromCluster(Object clusteredServices) {

        List<MicroService> microServices = new ArrayList<>();
        JSONObject jsonObject = (JSONObject) clusteredServices;
        int i = 1;

        HashMap<String, Object> serviceMap = (HashMap<String, Object>) jsonObject;

        for (String service : serviceMap.keySet()) {
            HashMap<String, Double> serviceData = (HashMap<String, Double>)serviceMap.get(service);
            microServices.add(new MicroService(i++, service, getRiskLevelForService(serviceData.get("sValue"))));
        }

        return microServices;
    }

    private RiskLevel getRiskLevelForService(Double sValue) {

        if (sValue >= 0.95) {
            return RiskLevel.NO_RISK;
        } else if (sValue >= 0.80) {
            return RiskLevel.LOW_RISK;
        } else if (sValue >= 0.75) {
            return RiskLevel.MEDIUM_RISK;
        } else {
            return RiskLevel.HIGH_RISK;
        }
    }
}
