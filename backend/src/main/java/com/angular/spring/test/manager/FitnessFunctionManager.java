package com.angular.spring.test.manager;

import com.angular.spring.test.manager.models.MicroService;

public class FitnessFunctionManager {

    public Double calculateFitnessFunction(int alpha, int beta, int gamma, int delta, MicroService microService) {
        Double fitnessValue = 0.0;
        int n = alpha + beta + gamma + delta;

        fitnessValue = (alpha*measureFunctionality(microService)+
                        beta*measureComposability(microService)+
                        gamma*measureSelfContainment(microService)+
                        delta*measureUsage(microService))/n;

        return fitnessValue;
    }

    public Double measureFunctionality (MicroService microService) {
        Double functionalityValue = 0.0;

        functionalityValue = (
                computeNumberOfProvidedInterfaces(microService)+
                measureComposability(microService)+
                computeCohesionBetweenInterfaces(microService)+
                computeInternalCoupling(microService)+
                computeLoseClassCohesion(microService))
                /5;

        return functionalityValue;
    }

    public Double measureComposability(MicroService microService) {
        Double comparabilityValue = 0.0;

        int I = microService.getChildren().size();
        Double totalInternalCohesion= computeCohesionWithInInterface(microService);

        comparabilityValue = totalInternalCohesion/I;

        return comparabilityValue;
    }

    public Double measureSelfContainment(MicroService microService) {
        Double selfContainmentValue = 0.0;

        selfContainmentValue = computeExternalCoupling(microService);

        return selfContainmentValue;
    }

    public Double measureUsage(MicroService microService) {
        Double usageValue = 0.0;
        Double inheritanceFactor = 0.0;
        Double compositionFactor = 0.0;

        inheritanceFactor = getInheritanceFactor(microService);
        compositionFactor = getCompositionFactor(microService);

        usageValue = inheritanceFactor +  compositionFactor;

        return usageValue;
    }

    private int computeLoseClassCohesion(MicroService microService) {
        int LCC = 0;
        int numberOfIndirectConnections = 0;
        int numberOfDirectConnections = 0;
        int maxPossibleConnections = 0;

        numberOfIndirectConnections = getNumberOfIndirectConnections(microService);
        numberOfDirectConnections = getNumberOfDirectConnections(microService);
        maxPossibleConnections = getNumberOfMaxPossibleConnections(microService);

        LCC  = (numberOfIndirectConnections+ numberOfDirectConnections)/ maxPossibleConnections;

        return LCC;
    }

    private int getNumberOfIndirectConnections(MicroService microService) {
        return 0;
    }

    private int getNumberOfDirectConnections(MicroService microService) {
        return 0;
    }

    private int getNumberOfMaxPossibleConnections(MicroService microService) {
        return 0;
    }

    /**
     * Compute the internal coupling
     * @return - couple - internal coupling
     */
    private Double computeInternalCoupling(MicroService microService) {
        Double couple = 0.0;

        return couple;
    }

    /**
     * Compute the external coupling of a service
     * @return -extCouple
     */
    private Double computeExternalCoupling(MicroService microService) {
        Double extCouple = 0.0;
        extCouple = 1 - computeInternalCoupling(microService);

        return extCouple;
    }

    /**
     * Compute the average of service’s interface cohesion within the interface,
     * @return LCC(i) of the service
     */
    private Double computeCohesionWithInInterface(MicroService microService) {
        Double LCCi = 0.0;
        int totalClasses = microService.getChildren().size();

        for (int i=0; i< totalClasses; i++) {
           // LCCi = LCCi + computeInterfaceLCC(microService.getChildren().get(i));
        }

        return LCCi;
    }

    private Double computeInterfaceLCC(MicroService microService) {
        return 0.0;
    }

    /**
     * Compute the cohesion between interfaces
     * @return LCC(I)
     */
    private Double computeCohesionBetweenInterfaces(MicroService microService) {
        Double LCCI = 0.0;

        return LCCI;
    }

    /**
     *Compute the cohesion inside a service
     * @return LLC(E)
     */
    private Double computeCohesionInsideService(MicroService microService) {
        Double LCCE = 0.0;

        return LCCE;
    }

    /**
     *Compute the number of provided interfaces
     * @return  - np(E) - number of provided interface
     */
    private int computeNumberOfProvidedInterfaces(MicroService microService) {
        int np = 0;

        return np;
    }

    private Double getCompositionFactor(MicroService microService) {
        return 0.0;
    }

    private Double getInheritanceFactor(MicroService microService) {
        return 0.0;
    }

    public Double calculateNewFitnessFunctionValue (MicroService microService) {

        return microService == null ? new Double(0.0) : microService.getFValue();
    }
}
