package com.angular.spring.test.manager;

import com.angular.spring.test.Algorithm.Clustering.Cluster;
import com.angular.spring.test.Algorithm.Clustering.ClusteringAlgorithm;
import com.angular.spring.test.Algorithm.Clustering.DefaultClusteringAlgorithm;
import com.angular.spring.test.model.MicroService;

import java.io.File;
import java.util.List;

public class ClusterManager {

    //This values should be change according to the Architects needs.
    private final int alpha = 2;
    private final int beta = 3;
    private final int gamma = 4;
    private final int delta = 5;

    public List<MicroService> getMicroServiceCluster(File sourceFiles) {

        String [] projectFiles = sourceFiles.list();
        String [] servicesList = null;
        ProcessManager processManager = new ProcessManager();
        if (processManager.isContainServiceFolder(projectFiles)) {
            System.out.println("Service folder found");

            File serviceFolder = new File(sourceFiles.getAbsolutePath() + "/" + "Service");
            servicesList = serviceFolder.list();

        }

        List<Class> sourceClassesList = null;
        ClusteringAlgorithm clusteringAlgorithm = new DefaultClusteringAlgorithm();
        Cluster cluster =
                clusteringAlgorithm.performClustering(sourceClassesList, alpha, beta, gamma, delta);

        return cluster.getMicroServices(servicesList);
    }
}
