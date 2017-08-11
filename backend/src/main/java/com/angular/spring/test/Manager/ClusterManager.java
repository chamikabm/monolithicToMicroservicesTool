package com.angular.spring.test.Manager;

import com.angular.spring.test.Algorithm.Clustering.Cluster;
import com.angular.spring.test.Algorithm.Clustering.ClusteringAlgorithm;
import com.angular.spring.test.Algorithm.Clustering.DefaultClusteringAlgorithm;
import com.angular.spring.test.model.MicroService;

import java.util.List;

public class ClusterManager {

    //This values should be change according to the Architects needs.
    private final int alpha = 2;
    private final int beta = 3;
    private final int gamma = 4;
    private final int delta = 5;

    public List<MicroService> getMicroServiceCluster(List<Class> sourceClassesList) {
        ClusteringAlgorithm clusteringAlgorithm = new DefaultClusteringAlgorithm();
        Cluster cluster =
                clusteringAlgorithm.performClustering(sourceClassesList, alpha, beta, gamma, delta);

        return cluster.getMicroServices();
    }
}
