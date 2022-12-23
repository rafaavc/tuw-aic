package aic.g3t1.interfaceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SpringBootApplication
public class InterfaceServer {
    public static void main(String[] args) {
        SpringApplication.run(InterfaceServer.class, args);
    }
}
