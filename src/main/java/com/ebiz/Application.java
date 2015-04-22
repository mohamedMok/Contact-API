package com.ebiz;

import com.ebiz.connection.ConfigSpringES;
import com.ebiz.services.RXServices;
import com.ebiz.services.RiverCron;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableAutoConfiguration(exclude = {ConfigSpringES.class})
@ComponentScan(basePackages = "com.ebiz")
public class Application {

	public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

}
