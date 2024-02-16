package com.ticketseller.backend.utils;

import com.ticketseller.backend.config.DatabaseConnectionChecker;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${spring.admin.password}")
    private String adminPassword;

    private static final String SUPER_ADMIN_BILKENT_ID = "00000000";
    private static final String SUPER_ADMIN_NAME = "Super Admin";

    private final DatabaseConnectionChecker connectionChecker;

    /**
     * This event is executed as late as conceivably possible to indicate 
     * that the application is ready to service requests.
     */
    @Override
    public void onApplicationEvent(final @NotNull ApplicationReadyEvent event) {
        System.out.println("Executing Database Connection Check after application startup...");
        connectionChecker.checkDatabaseConnection();

        this.createSuperAdmin();
    }

    private void createSuperAdmin() {
        // Create super admin
    }
}