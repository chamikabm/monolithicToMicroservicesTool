package com.angular.spring.test.Algorithm.Clustering;

import com.angular.spring.test.manager.ClusterManager;
import com.angular.spring.test.manager.models.MicroService;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

import static com.angular.spring.test.Algorithm.Clustering.Resources.clMap;

public class DefaultClusteringAlgorithm implements ClusteringAlgorithm {

    private static List<MicroService> initialMicroServicesClusters = new ArrayList<>();

    @Override
    public Cluster performClustering(double[][] distances,
                                     String[] clusterNames, LinkageStrategy linkageStrategy)
    {

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

    /* Setup model */
        List<Cluster> clusters = createClusters(clusterNames);
        DistanceMap linkages = createLinkages(distances, clusters);

    /* Process */
        HierarchyBuilder builder = new HierarchyBuilder(clusters, linkages);
        return builder.flatAgg(linkageStrategy, threshold);
    }

    @Override
    public Object performClustering(String[] sourceClassesList, String filesPath) throws IOException {

        ClusterManager clusterManager = new ClusterManager();
        String implFilePath = filesPath + "/" + sourceClassesList[0];

        File implFolder  = new File(implFilePath);
        String [] servicesImplList = implFolder.list();

        Resources resources = new Resources();
        resources.init();

        //Visit inside all the services and grab the data.
        CompilationUnit cu;
        assert servicesImplList != null;
        for(int i = 1; i < servicesImplList.length; i++) {
            File file = new File(implFilePath + "/" + servicesImplList[i]);
            try (FileInputStream in = new FileInputStream(file)) {
                cu = JavaParser.parse(in);
                updateInitialServiceCluster(cu, file);
            }
        }

        double[][] initialDistances = getUpdatedInitialDistances(initialMicroServicesClusters);
        System.out.println(Arrays.deepToString(initialDistances));
        LinkageStrategy linkageStrategy = new SingleLinkageStrategy();

        checkArguments(initialDistances, initialMicroServicesClusters, linkageStrategy);

        // Setup model
        List<Cluster> clusters = getClusters(initialMicroServicesClusters);
        DistanceMap linkages = createLinkagesForClusters(initialDistances, clusters);

        // Process
        HierarchyBuilder builder = new HierarchyBuilder(clusters, linkages);
        while (!builder.isTreeComplete())
        {
            builder.agglomerate(linkageStrategy);
        }

        TreeMap treeMap = builder.getAllClusters();

        assert treeMap != null;
        for (Object cluster : treeMap.keySet()) {

            System.out.println("--------------" + cluster + "--------------");
            System.out.println(clusterManager.getMicroServicesFromCluster(treeMap.get(cluster)));
        }

        System.out.println("-----------------------------------");

        return treeMap.get(treeMap.lastKey());
    }

    private double[][] getUpdatedInitialDistances(List<MicroService> initialMicroServicesClusters) {
        double[][]  initialDistances = new double[initialMicroServicesClusters.size()][2];

       for (int i = 0 ; i < initialDistances.length; i++) {
           initialDistances[i][1] = initialMicroServicesClusters.get(i).getFValue();
           initialDistances[i][0] = i + 1;
       }

        return initialDistances;
    }

    private void checkArguments(double[][] distances, List<MicroService> clusters, LinkageStrategy linkageStrategy)
    {

        if (distances[0].length != 2 &&  distances.length <= 2)
        {
            throw new IllegalArgumentException("Invalid distance matrix");
        }
        if (distances.length != clusters.size())
        {
            throw new IllegalArgumentException("Invalid cluster name array");
        }
        if (linkageStrategy == null)
        {
            throw new IllegalArgumentException("Undefined linkage strategy");
        }
    }

    @Override
    public Cluster performWeightedClustering(double[][] distances, String[] clusterNames,
                                             double[] weights, LinkageStrategy linkageStrategy)
    {


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

    private DistanceMap createLinkagesForClusters(double[][] distances,
                                                  List<Cluster> clusters) {

        double[][] initialEuclideanDistance = getEuclideanDistances(distances);
        return  createLinkages(initialEuclideanDistance, clusters);
    }

    private double[][] getEuclideanDistances(double[][] distances) {
        double[][] euclideanDistancesMatrix = new double[distances.length][distances.length];

        for (int i = 0; i < distances.length; i++) {
            for (int j = 0; j < distances.length; j++) {
                if (i == j) {
                    euclideanDistancesMatrix[i][j] = 0.0;
                } else {
                    euclideanDistancesMatrix[i][j] = euclideanDistancesForSingleCluster(distances[i], distances[j]);
                }
            }
        }

        return euclideanDistancesMatrix;
    }

    private double euclideanDistancesForSingleCluster(double[] pointOne, double[] pointTwo) {
        return Math.sqrt((pointOne[0]-pointTwo[0])*2 + (pointOne[1] - pointTwo[1])*2);
    }

    public List<Cluster> getClusters(List<MicroService> initialMicroServicesClusters) {
        List<Cluster> initialClusters = new ArrayList<>();

        for (MicroService microService : initialMicroServicesClusters){
            Cluster cluster = new Cluster(microService.getName());
            cluster.setDistance(new Distance(microService.getFValue()));
            initialClusters.add(cluster);
        }

        return  initialClusters;
    }

    private List<Cluster> createClusters(String[] clusterNames)
    {
        List<Cluster> clusters = new ArrayList<>();
        for (String clusterName : clusterNames)
        {
            Cluster cluster = new Cluster(clusterName);
            clusters.add(cluster);
        }
        return clusters;
    }

    private List<Cluster> createClusters(String[] clusterNames, double[] weights)
    {
        List<Cluster> clusters = new ArrayList<>();
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
//            System.out.println(n.getName());
//            System.out.println(n.getDeclarationAsString(
//                    true,
//                    true,
//                    true));
//            System.out.println(n.getBody());
        }
    }

    private static class ClassVisitor extends VoidVisitorAdapter {


        @Override
        public void visit(ClassExpr n, Object arg) {

            System.out.println(Arrays.toString(n.getClass().getInterfaces()));
            System.out.println(n.getType());
        }
    }

    private static MicroService getInitialMicroServiceCluster(String name) {

        return new MicroService(name);
    }

    private void findAllServiceClassses(CompilationUnit compilationUnit) {
        compilationUnit.findAll(ClassOrInterfaceDeclaration.class).stream()
                .filter(c -> !c.isInterface()
                        && c.isAbstract()
                        && !c.getNameAsString().endsWith("Service"))
                .forEach(c -> {
                    String className = c.getNameAsString();
                    System.out.println("Class Name " + className);
                });
    }

    private static void updateInitialServiceCluster(CompilationUnit compilationUnit, File file) {
        final String filename = file.getName();
        final String className = filename.replaceAll("(.*)\\.java$", "$1");

        if (className.length() == file.getName().length()) {
            throw new IllegalStateException("Couldn't extract [Java] class from filename: " + filename);
        }

        Optional<ClassOrInterfaceDeclaration> classDeclaration = compilationUnit.getTypes().stream()
                .filter(ClassOrInterfaceDeclaration.class::isInstance)
                .map(ClassOrInterfaceDeclaration.class::cast)
                .findFirst();

        MicroService microService = getInitialMicroServiceCluster(classDeclaration.get().getName().asString());
        microService.setClassDeclarationInfo(classDeclaration.get());
        microService.setFValue(ThreadLocalRandom.current().nextDouble(0.5, 0.97));
        microService.setMembers(classDeclaration.get().getMembers());
        microService.setFields(classDeclaration.get().getFields());
        microService.setInterfaces(classDeclaration.get().getImplementedTypes());

        initialMicroServicesClusters.add(microService);
    }
}
