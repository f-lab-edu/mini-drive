package dev.chan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan({
        "dev.chan",
        "dev.chan.kafka",
})
@SpringBootApplication
public class MiniDriveApplication {
    public static void main(String[] args) {
        SpringApplication.run(MiniDriveApplication.class, args);
        System.out.println("Hello drive!");
    }
}
