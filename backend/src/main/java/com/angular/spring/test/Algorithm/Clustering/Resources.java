package com.angular.spring.test.Algorithm.Clustering;

import com.angular.spring.test.manager.ClusterManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;

import static com.angular.spring.test.ProjectConstants.*;

public class Resources {

    static HashMap<String, Object> clustersMap;
    static HashMap<String, HashMap<String, Double>> rawValuesMap;


    static void init() {

        JSONParser parser = new JSONParser();
        Object obj;
        TreeMap<String, Object> initialValuesMap;
        Object initialValueWallet;

        try {
            obj = parser.parse(new FileReader("/Users/Chamikabandara/Projects/MyProjects/ProjectResources/LogData/init.json"));
            JSONObject jsonObject = (JSONObject) obj;
            String kfName  = "StudentManagementRestApi";
            String kcValue = DEPTH + "-" + ALPHA + "-" + BETA + "-" + GAMMA + "-" + DELTA;

            Object serviceObject = jsonObject.get(kfName);
            HashMap<String, Object> resultMap = (HashMap<String, Object>) serviceObject;

            if (resultMap.get(kcValue) != null) {
                clustersMap = (HashMap<String, Object>) resultMap.get(kcValue);
                initialValuesMap = new TreeMap<>(clustersMap);

                initialValueWallet = initialValuesMap.get(initialValuesMap.firstKey());
                rawValuesMap = (HashMap<String, HashMap<String, Double>>)initialValueWallet;
            } else {

                throw new RuntimeException("Invalid Architectural Constrains!");
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
