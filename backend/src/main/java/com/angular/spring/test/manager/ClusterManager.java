package com.angular.spring.test.manager;

import com.angular.spring.test.Algorithm.Clustering.Cluster;
import com.angular.spring.test.Algorithm.Clustering.ClusteringAlgorithm;
import com.angular.spring.test.Algorithm.Clustering.DefaultClusteringAlgorithm;
import com.angular.spring.test.model.MicroService;

import java.io.File;
import java.io.IOException;
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
        List<MicroService> microServices = clusteringAlgorithm.performClustering(servicesList, serviceFolder.getAbsolutePath());

        System.out.println("************ : " + microServices);

        return microServices;
    }
}
