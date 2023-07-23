package uz.dachatop.service;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

//@Component
//public class CommandLineService implements CommandLineRunner {
//    @Autowired
//    private DataSource dataSource;
//
//    @Override
//    public void run(String... args) throws Exception {
//        Flyway.configure().baselineOnMigrate(true).dataSource(dataSource).load().migrate();
//    }
//}
