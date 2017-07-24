package com.angular.spring.test.service.ServiceImpl;

import com.angular.spring.test.model.MicroService;
import com.angular.spring.test.model.RiskLevel;
import com.angular.spring.test.service.AppService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppServiceImpl implements AppService {

    @Override
    public String getWelcomeMessage(String name) {
        return "Hello!! " + name;
    }

    @Override
    public List<MicroService> getAllServices() {
        MicroService ms1 = new MicroService();
        ms1.setServiceName("MS1");
        ms1.setRiskLevel(RiskLevel.HIGH_RISK);
        MicroService ms2 = new MicroService();
        ms2.setServiceName("MS2");
        ms2.setRiskLevel(RiskLevel.LOW_RISK);

        return new ArrayList<MicroService>() {{
            add(ms1);
            add(ms2);
        }};
    }
}
