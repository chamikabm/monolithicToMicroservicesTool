package com.angular.spring.test.Algorithm.Clustering;

import com.angular.spring.test.manager.FitnessFunctionManager;
import com.angular.spring.test.manager.models.MicroService;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.Callable;

import static com.angular.spring.test.Algorithm.Clustering.Resources.clustersMap;

public class HierarchyBuilder {

    private DistanceMap distances;
    private List<Cluster> clusters;

    public DistanceMap getDistances() {
        return distances;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public HierarchyBuilder(List<Cluster> clusters, DistanceMap distances) {
        this.clusters = clusters;
        this.distances = distances;
    }

    /**
     * Returns Flattened clusters, i.e. clusters that are at least apart by a given threshold
     * @param linkageStrategy
     * @param threshold
     * @return
     */
    public List<Cluster> flatAgg(LinkageStrategy linkageStrategy, Double threshold)
    {
        while((!isTreeComplete()) && (distances.minDist() != null) && (distances.minDist() <= threshold))
        {
            agglomerate(linkageStrategy);
        }

        return clusters;
    }

    public void agglomerate(LinkageStrategy linkageStrategy) {
        ClusterPair minDistLink = distances.removeFirst();
        if (minDistLink != null) {
            clusters.remove(minDistLink.getrCluster());
            clusters.remove(minDistLink.getlCluster());

            Cluster oldClusterL = minDistLink.getlCluster();
            Cluster oldClusterR = minDistLink.getrCluster();
            Cluster newCluster = minDistLink.agglomerate(null);

            for (Cluster iClust : clusters) {
                ClusterPair link1 = findByClusters(iClust, oldClusterL);
                ClusterPair link2 = findByClusters(iClust, oldClusterR);
                ClusterPair newLinkage = new ClusterPair();
                newLinkage.setlCluster(iClust);
                newLinkage.setrCluster(newCluster);
                Collection<Distance> distanceValues = new ArrayList<Distance>();

                if (link1 != null) {
                    Double distVal = link1.getLinkageDistance();
                    Double weightVal = link1.getOtherCluster(iClust).getWeightValue();
                    distanceValues.add(new Distance(distVal, weightVal));
                    distances.remove(link1);
                }
                if (link2 != null) {
                    Double distVal = link2.getLinkageDistance();
                    Double weightVal = link2.getOtherCluster(iClust).getWeightValue();
                    distanceValues.add(new Distance(distVal, weightVal));
                    distances.remove(link2);
                }

                Distance newDistance = linkageStrategy.calculateDistance(distanceValues);

                newLinkage.setLinkageDistance(newDistance.getDistance());
                distances.add(newLinkage);

            }

            if (oldClusterL  != null && oldClusterR != null) {
                MicroService updatedMicroService = getUpdatedMicroService(oldClusterL.getMicroService(),
                        oldClusterR.getMicroService());
                if (updatedMicroService != null) {
                    newCluster.setMicroService(updatedMicroService);
                }
            }

            clusters.add(newCluster);
        }
    }

    private MicroService getUpdatedMicroService(MicroService leftMicroService, MicroService rightMicroService) {

        if (leftMicroService != null && rightMicroService != null) {

            String newName = leftMicroService.getName() + " - " + rightMicroService.getName();
            MicroService newMicroService = new MicroService(newName);

            if (leftMicroService.getChildren() == null && rightMicroService.getChildren() == null) {
                newMicroService.setParent(newName);
                List<String> newChildrens = new ArrayList<>();
                newChildrens.add(leftMicroService.getName());
                newChildrens.add(rightMicroService.getName());
                newMicroService.setChildren(newChildrens);

            } else {

                List<String> newChildes = new ArrayList<>();
                if (leftMicroService.getChildren() != null) {
                    newChildes.addAll(leftMicroService.getChildren());
                } else if (rightMicroService.getChildren() != null) {
                    newChildes.addAll(rightMicroService.getChildren());
                }

                newMicroService.setChildren(newChildes);
            }

            updateInterfacesList(leftMicroService, rightMicroService, newMicroService);
            updateMembersList(leftMicroService, rightMicroService, newMicroService);
            updateFieldsList(leftMicroService, rightMicroService, newMicroService);

            System.out.println(newMicroService.getName());
            return newMicroService;
        }

        return null;
    }

    private void updateInterfacesList(MicroService leftMicroService, MicroService rightMicroService,
                                      MicroService newMicroService) {

        NodeList<ClassOrInterfaceType> interfaces = new NodeList<>();
        if (leftMicroService.getInterfaces() != null) {
            interfaces.addAll(leftMicroService.getInterfaces());
        }

        if (rightMicroService.getInterfaces() != null) {
            interfaces.addAll(rightMicroService.getInterfaces());
        }

        newMicroService.setInterfaces(interfaces);
    }

    private void updateMembersList(MicroService leftMicroService, MicroService rightMicroService,
                                  MicroService newMicroService) {

        NodeList<BodyDeclaration<?>> members = new NodeList<>();
        if (leftMicroService.getMembers() != null) {
            members.addAll(leftMicroService.getMembers());
        }

        if (rightMicroService.getInterfaces() != null) {
            members.addAll(rightMicroService.getMembers());
        }

        newMicroService.setMembers(members);
    }

    private void updateFieldsList(MicroService leftMicroService, MicroService rightMicroService,
                                      MicroService newMicroService) {

        List<FieldDeclaration> fields = new ArrayList<>();
        if (leftMicroService.getFields() != null) {
            fields.addAll(leftMicroService.getFields());
        }

        if (rightMicroService.getInterfaces() != null) {
            fields.addAll(rightMicroService.getFields());
        }

        newMicroService.setFields(fields);
    }

    private Double getNewFvalue(MicroService microService) {
        FitnessFunctionManager fitnessFunctionManager = new FitnessFunctionManager();
        return  fitnessFunctionManager.calculateNewFitnessFunctionValue(microService);
    }

    private ClusterPair findByClusters(Cluster c1, Cluster c2) {
        return distances.findByCodePair(c1, c2);
    }

    public boolean isTreeComplete() {
        return clusters.size() == 1;
    }

    public Cluster getRootCluster() {
        if (!isTreeComplete()) {
            throw new RuntimeException("No root available");
        }
        return clusters.get(0);
    }

    public TreeMap getAllClusters() {
        return filterClusters(this::getClustersTree);
    }

    private TreeMap<String, Object> getClustersTree() {
        return new TreeMap<>(clustersMap);
    }

    private static TreeMap filterClusters(Callable<TreeMap<String, Object>> clusterEvaluation) {

        try {
            return clusterEvaluation.call();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
