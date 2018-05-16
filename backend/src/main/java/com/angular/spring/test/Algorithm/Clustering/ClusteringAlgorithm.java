package com.angular.spring.test.Algorithm.Clustering;

import com.angular.spring.test.model.MicroService;

import java.io.IOException;
import java.util.List;

public interface ClusteringAlgorithm {
    Cluster performClustering(double[][] distances, String[] clusterNames, LinkageStrategy linkageStrategy);

    Cluster performWeightedClustering(double[][] distances, String[] clusterNames, double[] weights,
                                      LinkageStrategy linkageStrategy);

    List<Cluster> performFlatClustering(double[][] distances, String[] clusterNames, LinkageStrategy linkageStrategy,
                                        Double threshold);

    List<MicroService> performClustering(String[] servicesNames, String filesPath) throws IOException;
}
