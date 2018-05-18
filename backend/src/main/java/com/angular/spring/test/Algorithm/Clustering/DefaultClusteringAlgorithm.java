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

import static com.angular.spring.test.Algorithm.Clustering.Resources.clMap;

public class DefaultClusteringAlgorithm implements ClusteringAlgorithm {

    private static List<MicroService> initalmicroServicesClsuters = new ArrayList<>();

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
    public Object performClustering(String[] sourceClassesList, String filesPath) throws IOException {

        ClusterManager clusterManager = new ClusterManager();
        String implFilePath = filesPath + "/" + sourceClassesList[0];

        File implFolder  = new File(implFilePath);
        String [] servicesImplList = implFolder.list();

        //Visit inside all the services and grab the data.
        CompilationUnit cu;
        assert servicesImplList != null;
        for(int i = 1; i<2; i++) {
            File file = new File(implFilePath + "/" + servicesImplList[i]);
            try (FileInputStream in = new FileInputStream(file)) {
                System.out.println("********** : " + servicesImplList[i]);
                // parse the file
                cu = JavaParser.parse(in);

                initalmicroServicesClsuters.add(getInitialMicroServiceCluster(servicesImplList[i]));
                // visit and print the methods names
//            new MethodVisitor().visit(cu, null);
//            new ClassVisitor().visit(cu, null);
//                findAllServiceClassses(cu);
                getClassDeclaration(cu, file);

            }

        }

        Resources resources = new Resources();
        resources.init();

        TreeMap treeMap = dansMethod(this::methodToPass);

        // TO DO Enable after all.
        /*assert treeMap != null;
        for (Object cluster : treeMap.keySet()) {

            System.out.println("--------------" + cluster + "--------------");
            System.out.println(clusterManager.getMicroServicesFromCluster(treeMap.get(cluster)));
        }

        System.out.println("-----------------------------------");*/

        return treeMap.get(treeMap.lastKey());
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

    private TreeMap<String, Object> methodToPass() {
        return new TreeMap<>(clMap);
    }

    private TreeMap dansMethod(Callable<TreeMap<String, Object>> myFunc) {

        try {
            return myFunc.call();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private MicroService getInitialMicroServiceCluster(String name) {

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

    private static void getClassDeclaration(CompilationUnit compilationUnit, File file) {
        final String filename = file.getName();
        final String className = filename.replaceAll("(.*)\\.java$", "$1");

        if (className.length() == file.getName().length()) {
            throw new IllegalStateException("Couldn't extract [Java] class name from filename: " + filename);
        }

        Optional<ClassOrInterfaceDeclaration> classDeclaration = compilationUnit.getTypes().stream()
                .filter(ClassOrInterfaceDeclaration.class::isInstance)
                .map(ClassOrInterfaceDeclaration.class::cast)
                .findFirst();

         //Give all class details
         //System.out.println(classDeclaration.get());

        //Give all class members including methods
        //System.out.println(classDeclaration.get().getMembers());

        //Give all class members including methods
        System.out.println(classDeclaration.get().getFields());

        // Get Implemented File - Interface
        //System.out.println(classDeclaration.get().getImplementedTypes());
    }
}
