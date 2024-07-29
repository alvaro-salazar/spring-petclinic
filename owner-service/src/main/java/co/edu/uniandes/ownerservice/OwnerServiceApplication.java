package co.edu.uniandes.ownerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication
@EnableSpringDataWebSupport
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class OwnerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OwnerServiceApplication.class, args);
    }

}
