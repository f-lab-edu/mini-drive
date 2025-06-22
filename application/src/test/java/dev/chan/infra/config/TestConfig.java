package dev.chan.infra.config;

import dev.chan.application.file.DriveItemAppService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {
    @Bean
    public DriveItemAppService driveItemService() {
        return Mockito.mock(DriveItemAppService.class);
    }
}
