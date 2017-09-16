package com.angular.spring.test.manager;

import com.angular.spring.test.model.MicroService;

public class FitnessFunctionManager {

    public float calculateFitnessFunction(int alpha, int beta, int gamma, int delta, MicroService microService) {
        float fitnessValue;
        int n = alpha + beta + gamma + delta;

        fitnessValue = (alpha*measureFunctionality(microService)+
                        beta*measureComposability(microService)+
                        gamma*measureSelfContainment(microService)+
                        delta*measureUsage(microService))/n;

        return fitnessValue;
    }

    private float measureFunctionality (MicroService microService) {
        float functionalityValue;

        functionalityValue = (
                computeNumberOfProvidedInterfaces(microService)+
                measureComposability(microService)+
                computeCohesionBetweenInterfaces(microService)+
                computeInternalCoupling(microService)+
                computeLoseClassCohesion(microService))
                /5;

        return functionalityValue;
    }

    private float measureComposability(MicroService microService) {
        float comparabilityValue;

        int I = microService.getClasses().size();
        float totalInternalCohesion= computeCohesionWithInInterface(microService);

        comparabilityValue = totalInternalCohesion/I;

        return comparabilityValue;
    }

    private float measureSelfContainment(MicroService microService) {
        float selfContainmentValue;

        selfContainmentValue = computeExternalCoupling(microService);

        return selfContainmentValue;
    }

    private float measureUsage(MicroService microService) {
        float usageValue;
        float inheritanceFactor;
        float compositionFactor;

        inheritanceFactor = getInheritanceFactor(microService);
        compositionFactor = getCompositionFactor(microService);

        usageValue = inheritanceFactor +  compositionFactor;

        return usageValue;
    }

    private int computeLoseClassCohesion(MicroService microService) {
        int LCC;
        int numberOfIndirectConnections;
        int numberOfDirectConnections;
        int maxPossibleConnections;

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
    private float computeInternalCoupling(MicroService microService) {
        float couple = 0.0f;

        return couple;
    }

    /**
     * Compute the external coupling of a service
     * @return -extCouple
     */
    private float computeExternalCoupling(MicroService microService) {
        float extCouple = 0.0f;
        extCouple = 1 - computeInternalCoupling(microService);

        return extCouple;
    }

    /**
     * Compute the average of service’s interface cohesion within the interface,
     * @return LCC(i) of the service
     */
    private float computeCohesionWithInInterface(MicroService microService) {
        float LCCi = 0.0f;
        int totalClasses = microService.getClasses().size();

        for (int i=0; i< totalClasses; i++) {
            LCCi = LCCi + computeInterfaceLCC(microService.getClasses().get(i));
        }

        return LCCi;
    }

    private float computeInterfaceLCC(Class projectClass) {
        return 0;
    }

    /**
     * Compute the cohesion between interfaces
     * @return LCC(I)
     */
    private float computeCohesionBetweenInterfaces(MicroService microService) {
        float LCCI = 0.0f;

        return LCCI;
    }

    /**
     *Compute the cohesion inside a service
     * @return LLC(E)
     */
    private float computeCohesionInsideService(MicroService microService) {
        float LCCE = 0.0f;

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

    private float getCompositionFactor(MicroService microService) {
        return 0;
    }

    private float getInheritanceFactor(MicroService microService) {
        return 0;
    }
}
