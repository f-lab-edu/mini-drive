package dev.chan.config;

import dev.chan.application.file.DriveItemService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {
    @Bean
    public DriveItemService driveItemService() {
        return Mockito.mock(DriveItemService.class);
    }
}
