package com.ebiz.services;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

/**
 * Created by ebiz on 05/02/2015.
 */
@EnableScheduling
@Configuration
public class RiverCron {

    @Scheduled(fixedRate = 300000)
    public void runTheRiver() {
       DataManagement dataManagement = new DataManagement();
        System.out.println("River begin");
        try {
            dataManagement.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("River end");
    }
}
