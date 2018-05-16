package com.angular.spring.test.Algorithm.Clustering;

import com.angular.spring.test.model.MicroService;
import com.angular.spring.test.model.RiskLevel;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static com.angular.spring.test.Algorithm.Clustering.Resources.clMap;

public class DefaultClusteringAlgorithm implements ClusteringAlgorithm{

    @Override
    public Cluster performClustering(double[][] distances,
                                     String[] clusterNames, LinkageStrategy linkageStrategy)
    {

    checkArguments(distances, clusterNames, linkageStrategy);
    /* Setup model */
    List<Cluster> clusters = createClusters(clusterNames);
    DistanceMap linkages = createLinkages(distances, clusters);

    /* Process */
    HierarchyBuilder builder = new HierarchyBuilder(clusters, linkages);
        while (!builder.isTreeComplete())
    {
        builder.agglomerate(linkageStrategy);
    }

        return builder.getRootCluster();
}

    @Override
    public List<Cluster> performFlatClustering(double[][] distances,
                                               String[] clusterNames, LinkageStrategy linkageStrategy, Double threshold)
    {

        checkArguments(distances, clusterNames, linkageStrategy);
    /* Setup model */
        List<Cluster> clusters = createClusters(clusterNames);
        DistanceMap linkages = createLinkages(distances, clusters);

    /* Process */
        HierarchyBuilder builder = new HierarchyBuilder(clusters, linkages);
        return builder.flatAgg(linkageStrategy, threshold);
    }

    @Override
    public List<MicroService> performClustering(String[] sourceClassesList, String filesPath) throws IOException {


        String implFilePath = filesPath + "/" + sourceClassesList[0];

        File implFolder  = new File(implFilePath);
        String [] servicesImplList = implFolder.list();
        System.out.println(Arrays.toString(servicesImplList));

        //Visit inside all the services and grab the data.
        CompilationUnit cu;
        assert servicesImplList != null;
        for(int i = 1; i<servicesImplList.length; i++)
            try (FileInputStream in = new FileInputStream(implFilePath + "/" + servicesImplList[1])) {
            // parse the file
            cu = JavaParser.parse(in);

            // visit and print the methods names
            new MethodVisitor().visit(cu, null);
        }

        Resources resources = new Resources();
        resources.init();

        //Pushing results to tree map to keep the order.
        TreeMap<String, Object> treeMap = new TreeMap<>(clMap);

        for (String cluster : treeMap.keySet()) {

            System.out.println("--------------" + cluster + "--------------");
            System.out.println(getMicroServicesFromCluster(treeMap.get(cluster)));
        }

        System.out.println("-----------------------------------");

        return getMicroServicesFromCluster(treeMap.get(treeMap.lastKey()));
    }

    private void checkArguments(double[][] distances, String[] clusterNames,
                                LinkageStrategy linkageStrategy)
    {
        if (distances == null || distances.length == 0
                || distances[0].length != distances.length)
        {
            throw new IllegalArgumentException("Invalid distance matrix");
        }
        if (distances.length != clusterNames.length)
        {
            throw new IllegalArgumentException("Invalid cluster name array");
        }
        if (linkageStrategy == null)
        {
            throw new IllegalArgumentException("Undefined linkage strategy");
        }
        int uniqueCount = new HashSet<>(Arrays.asList(clusterNames)).size();
        if (uniqueCount != clusterNames.length)
        {
            throw new IllegalArgumentException("Duplicate names");
        }
    }

    @Override
    public Cluster performWeightedClustering(double[][] distances, String[] clusterNames,
                                             double[] weights, LinkageStrategy linkageStrategy)
    {

        checkArguments(distances, clusterNames, linkageStrategy);

        if (weights.length != clusterNames.length)
        {
            throw new IllegalArgumentException("Invalid weights array");
        }

    /* Setup model */
        List<Cluster> clusters = createClusters(clusterNames, weights);
        DistanceMap linkages = createLinkages(distances, clusters);

    /* Process */
        HierarchyBuilder builder = new HierarchyBuilder(clusters, linkages);
        while (!builder.isTreeComplete())
        {
            builder.agglomerate(linkageStrategy);
        }

        return builder.getRootCluster();
    }

    private DistanceMap createLinkages(double[][] distances,
                                       List<Cluster> clusters)
    {
        DistanceMap linkages = new DistanceMap();
        for (int col = 0; col < clusters.size(); col++)
        {
            for (int row = col + 1; row < clusters.size(); row++)
            {
                ClusterPair link = new ClusterPair();
                Cluster lCluster = clusters.get(col);
                Cluster rCluster = clusters.get(row);
                link.setLinkageDistance(distances[col][row]);
                link.setlCluster(lCluster);
                link.setrCluster(rCluster);
                linkages.add(link);
            }
        }
        return linkages;
    }

    private List<Cluster> createClusters(String[] clusterNames)
    {
        List<Cluster> clusters = new ArrayList<Cluster>();
        for (String clusterName : clusterNames)
        {
            Cluster cluster = new Cluster(clusterName);
            clusters.add(cluster);
        }
        return clusters;
    }

    private List<Cluster> createClusters(String[] clusterNames, double[] weights)
    {
        List<Cluster> clusters = new ArrayList<Cluster>();
        for (int i = 0; i < weights.length; i++)
        {
            Cluster cluster = new Cluster(clusterNames[i]);
            cluster.setDistance(new Distance(0.0, weights[i]));
            clusters.add(cluster);
        }
        return clusters;
    }

    private static class MethodVisitor extends VoidVisitorAdapter {

        @Override
        public void visit(MethodDeclaration n, Object arg) {
            // here you can access the attributes of the method.
            // this method will be called for all methods in this
            // CompilationUnit, including inner class methods
            System.out.println(n.getName());
            System.out.println(n.getDeclarationAsString(
                    true,
                    true,
                    true));
            System.out.println(n.getBody());
        }
    }

    private List<MicroService> getMicroServicesFromCluster(Object clusteredServices) {

        List<MicroService> microServices = new ArrayList<>();
        JSONObject jsonObject = (JSONObject) clusteredServices;
        int i =1;

        HashMap<String, Object> serviceMap = (HashMap<String, Object>) jsonObject;

        for (String service : serviceMap.keySet()) {
            HashMap<String, Double> serviceData = (HashMap<String, Double>)serviceMap.get(service);
            System.out.println(serviceData.get("sValue"));
            microServices.add(new MicroService(i++, service, getRiskLevelForService(serviceData.get("sValue"))));
        }

        return microServices;
    }


    private RiskLevel getRiskLevelForService(Double fValue) {

        if (fValue >= 0.95) {
            return RiskLevel.NO_RISK;
        } else if (fValue >= 0.80) {
            return RiskLevel.LOW_RISK;
        } else if (fValue >= 0.75) {
            return RiskLevel.MEDIUM_RISK;
        } else {
            return RiskLevel.HIGH_RISK;
        }
    }
}
