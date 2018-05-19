package com.angular.spring.test.manager;

import com.angular.spring.test.manager.models.MicroService;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.jpeek.processors.CohesionCalculator;
import org.jpeek.processors.CouplingCalculator;
import org.jpeek.processors.SourceCrawler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FitnessFunctionManager {

    /**
     *
     * @param alpha - A constant values based on the Architect`s decisions.
     * @param beta - A constant values based on the Architect`s decisions.
     * @param gamma - A constant values based on the Architect`s decisions.
     * @param delta - A constant values based on the Architect`s decisions.
     * @param microService - MicroService to calculate the fitness function value.
     * @return - Fitness function values for the given MicroService
     *           [(alpha * F(E) + beta * C(E) + gamma * S(E) + delta * U(E) ) / (alpha + beta + gamma + delta)]
     */
    public Double calculateFitnessFunction(int alpha, int beta, int gamma, int delta, MicroService microService) {
        Double fitnessValue;
        int n = alpha + beta + gamma + delta;

        fitnessValue = (alpha*measureFunctionality(microService)+
                        beta*measureComposability(microService)+
                        gamma*measureSelfContainment(microService)+
                        delta*measureUsage(microService))/n;

        return fitnessValue;
    }

    /**
     *  1. A service that provides several interfaces may provide numerous functionalities,
     * thus the higher the number of interfaces is, the more the service provides functionality.
     *  -> Number of provided interfaces. [np(E)]
     *
     *  2.An interface whose services are highly cohesive probably provide single functionality.
     *  -> Average of service’s interface cohesion within the interface. [C[E]]
     *
     *  3. A group of interfaces with high cohesion are most favorable to provide single
     *  or limited number of functionality.
     *  -> Cohesion between interfaces. [LLC(I)]
     *
     *  4. When the extracted code of candidate service is highly coupled,
     *  this means that the service probably provides very few or single functionality.
     *  -> Coupling inside a service. [Couple(E)]
     *
     *  5. When the extracted code of candidate service is highly cohesive,
     *  this means that the service probably provides very few or single functionality.
     *  -> Cohesion inside a service. [LCC(E)]
     *
     * @param microService  - MicroService to be calculate the functionality.
     * @return - Calculated Functionality Value. [F(E)]
     *         [(1 + 2 + 3 + 4 + 5) / 5]
     */
    private Double measureFunctionality (MicroService microService) {
        Double functionalityValue;

        functionalityValue = (
                computeNumberOfProvidedInterfaces(microService)+
                measureComposability(microService)+
                computeCohesionBetweenInterfaces(microService)+
                computeInternalCoupling(microService)+
                computeCohesionInsideService(microService))
                /5;

        return functionalityValue;
    }

    /**
     * Composability refers to the average of service’s interface cohesion within the interface.
     *
     * @param microService - MicroService to be calculate the composability.
     * @return - Calculated Composability Value. [C(E)]
     */
    private Double measureComposability(MicroService microService) {
        Double comparabilityValue;

        int I = microService.getChildren() == null ? 1 : microService.getChildren().size();
        Double totalInternalCohesion= computeCohesionWithInInterface(microService);

        comparabilityValue = totalInternalCohesion/I;

        return comparabilityValue;
    }

    /**
     * Self - Containment also a measure of the External Coupling.
     *
     * @param microService - MicroService to be calculate the Self - Containment.
     * @return - Calculated Self - Containment Value. [S(E)]
     */
    private Double measureSelfContainment(MicroService microService) {
        Double selfContainmentValue;

        selfContainmentValue = computeExternalCoupling(microService);

        return selfContainmentValue;
    }

    /**
     * Measure the usage of MicroServices.
     *
     * @param microService - MicroService to be calculate the usage.
     * @return - Calculated usage value. [U(E)]
     */
    private Double measureUsage(MicroService microService) {
        Double usageValue;
        Double inheritanceFactor;
        Double compositionFactor;

        inheritanceFactor = getInheritanceFactor(microService);
        compositionFactor = getCompositionFactor(microService);

        usageValue = inheritanceFactor +  compositionFactor;

        return usageValue/2;
    }

    /**
     *
     * numberOfIndirectConnections = getNumberOfIndirectConnections(microService);
     * numberOfDirectConnections = getNumberOfDirectConnections(microService);
     * maxPossibleConnections = getNumberOfMaxPossibleConnections(microService);
     *
     * LCC  = (numberOfIndirectConnections+ numberOfDirectConnections)/ maxPossibleConnections;
     *
     *
     * @param microService - MicroService to be calculate the Loose Class Cohesion.
     * @return - Calculated Loose Class Cohesion Value. -> Cohesion inside a service. [LCC(E)]
     */
    private int computeLoseClassCohesion(MicroService microService) {
        CohesionCalculator cohesionCalculator = new CohesionCalculator();

//        return cohesionCalculator.calculateLackOfCohesion();

        return 0;
    }

    /**
     * Compute the internal coupling - Coupling inside a service.
     *
     * @param microService - MicroService to be calculate the internal coupling.
     * @return Calculated internal coupling. [Couple(E)]
     */
    private Double computeInternalCoupling(MicroService microService) {
        CouplingCalculator couplingCalculator = new CouplingCalculator();
        List<String> fileNames = new ArrayList<>();
        fileNames.add(microService.getName());

        if (microService.getChildren() != null && microService.getChildren().size() > 0) {
            fileNames.addAll(microService.getChildren());
        }

        List<File> filesList = getFilesList(fileNames);

        return  1 - couplingCalculator.calculateExternalCouplingOfAGroup(filesList);
    }

    /**
     * Compute the external coupling of a service
     *
     * @param microService - MicroService to be calculate the External Coupling.
     * @return - Calculated External Coupling.
     */
    private Double computeExternalCoupling(MicroService microService) {

        return 1 - computeInternalCoupling(microService);
    }

    /**
     * Compute the average of service’s interfaces cohesion within the interface.
     *
     * @param microService - MicroService to be calculate the average of service’s interface cohesion.
     * @return - Calculated average of service’s interface cohesion.
     */
    private Double computeCohesionWithInInterface(MicroService microService) {
        CohesionCalculator cohesionCalculator = new CohesionCalculator();

        return cohesionCalculator.calculateAvgInterfaceCohesion(microService.getInterfaces());
    }

    /**
     * Compute the cohesion between interfaces
     *
     * @param microService - MicroService to be calculate the cohesion between interfaces.
     * @return - Calculated cohesion between interfaces. [LLC(I)]
     */
    private Double computeCohesionBetweenInterfaces(MicroService microService) {
        CohesionCalculator cohesionCalculator = new CohesionCalculator();
        NodeList<ClassOrInterfaceType> interfaces = microService.getInterfaces();

        return cohesionCalculator.calculateCohesionBetweenInterfaces(interfaces);
    }

    /**
     * Compute the cohesion inside a service
     *
     * @param microService - MicroService to be calculate the cohesion inside the service.
     * @return - Calculated cohesion inside the service.
     */
    private Double computeCohesionInsideService(MicroService microService) {
        CohesionCalculator cohesionCalculator = new CohesionCalculator();

        return cohesionCalculator.calculateCohesionBetweenInterfaces(microService.getInterfaces());
    }

    /**
     * Compute the number of provided interfaces.
     *
     * @param microService - MicroService to be calculate the number of provided interfaces.
     * @return - Calculated number of interfaces. [np(E)]
     */
    private int computeNumberOfProvidedInterfaces(MicroService microService) {

        return microService.getInterfaces() == null ? 0 : microService.getInterfaces().size();
    }

    /**
     * Compute the composition factor of given class.
     *
     * @param microService - MicroService to be calculate the composition factor.
     * @return - Calculated number of interfaces. [P(E)]
     */
    private Double getCompositionFactor(MicroService microService) {
        SourceCrawler sourceCrawler = new SourceCrawler();
        File file = new File("/Users/Chamikabandara/Projects/MyProjects/StudentManagementRestApi/src/main/java/com/student/management/rest/api");

        return sourceCrawler.getCompositionFactor(file, microService.getName());
    }

    /**
     * Compute the inheritance factor of given class.
     *
     * @param microService - MicroService to be calculate the inheritance factor.
     * @return - Calculated number of interfaces. [H(E)]
     */
    private Double getInheritanceFactor(MicroService microService) {
        SourceCrawler sourceCrawler = new SourceCrawler();
        File file = new File("/Users/Chamikabandara/Projects/MyProjects/StudentManagementRestApi/src/main/java/com/student/management/rest/api");

        return sourceCrawler.getInheritanceFactorFromUsage(file, microService.getName());
    }

    public Double calculateNewFitnessFunctionValue (MicroService microService) {

        return microService == null ? new Double(0.0) : microService.getFValue();
    }

    private List<File> getFilesList(List<String> fileNames) {

        List<File> filesList = new ArrayList<>();

        for (String fileName : fileNames) {
            filesList.add(getFileWithPath(fileName));
        }

        return filesList;
    }

    private File getFileWithPath(String fileName) {

        String fileRoot = "/Users/Chamikabandara/Projects/MyProjects/StudentManagementRestApi/src/main/java/com/student/management/rest/api";

        File file = null;

        if (fileName.contains("Service")) {
            file = new File(fileRoot + "/Service/Impl/" + fileName );
        } else if (fileName.contains("Manager")) {
            file = new File(fileRoot + "/Manager/" + fileName );
        } else if (fileName.contains("Repository")) {
            file = new File(fileRoot + "/Repository/" + fileName );
        } else if (fileName.contains("Model")) {
            file = new File(fileRoot + "/Model/" + fileName );;
        } else if (fileName.contains("Entity")) {
            file = new File(fileRoot + "/Entity/" + fileName );
        }

        return file;
    }
}
