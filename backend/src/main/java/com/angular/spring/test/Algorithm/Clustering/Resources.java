package com.angular.spring.test.Algorithm.Clustering;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import static com.angular.spring.test.ProjectConstants.*;

public class Resources {

    static HashMap<String, Object> clMap;

    static void init() {

        JSONParser parser = new JSONParser();
        Object obj;

        try {
            obj = parser.parse(new FileReader("/Users/Chamikabandara/Projects/MyProjects/ProjectResources/LogData/init.json"));

            JSONObject jsonObject = (JSONObject) obj;

            String kfName  = "StudentManagementRestApi";

            String kcValue = DEPTH + "-" + ALPHA + "-" + BETA + "-" + GAMMA + "-" + DELTA;

            Object serviceObject = jsonObject.get(kfName);
            HashMap<String, Object> resultMap = (HashMap<String, Object>) serviceObject;

            if (resultMap.get(kcValue) != null) {
                clMap = (HashMap<String, Object>) resultMap.get(kcValue);
            } else {

                throw new RuntimeException("Invalid Architectural Constrains!");
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
