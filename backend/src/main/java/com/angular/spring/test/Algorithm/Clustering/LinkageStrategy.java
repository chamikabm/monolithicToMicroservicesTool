package com.angular.spring.test.Algorithm.Clustering;

import java.util.Collection;

public interface LinkageStrategy {
    Distance calculateDistance(Collection<Distance> distances);
}
