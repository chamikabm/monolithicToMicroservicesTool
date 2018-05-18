package com.angular.spring.test.Algorithm.Clustering;

import java.io.IOException;
import java.util.List;

public interface ClusteringAlgorithm {
    Cluster performClustering(double[][] distances, String[] clusterNames, LinkageStrategy linkageStrategy);

    Cluster performWeightedClustering(double[][] distances, String[] clusterNames, double[] weights,
                                      LinkageStrategy linkageStrategy);

    List<Cluster> performFlatClustering(double[][] distances, String[] clusterNames, LinkageStrategy linkageStrategy,
                                        Double threshold);

    Object performClustering(String[] servicesNames, String filesPath) throws IOException;
}
