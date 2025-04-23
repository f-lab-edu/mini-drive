package dev.chan.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan(basePackages = "dev.chan.api.config")
@SpringBootApplication
public class MiniDriveApplication {
    public static void main(String[] args) {
        SpringApplication.run(MiniDriveApplication.class, args); // ✅ 필수!
        System.out.println("Hello drive!");
    }
}
