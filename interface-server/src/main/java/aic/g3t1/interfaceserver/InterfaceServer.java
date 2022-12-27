package aic.g3t1.interfaceserver;

import aic.g3t1.interfaceserver.controller.TaxiDataController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InterfaceServer {
    public static void main(String[] args) {
        SpringApplication.run(InterfaceServer.class, args);
    }
}
