package com.mouensis.server.identity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhuyuan
 */
@SpringBootApplication(scanBasePackages = "com.mouensis")
public class MouensisIdentityServerApplication {

    public static void main(String[] args) {
        System.setProperty("hibernate.dialect.storage_engine", "innodb");
        SpringApplication.run(MouensisIdentityServerApplication.class, args);
    }

}
