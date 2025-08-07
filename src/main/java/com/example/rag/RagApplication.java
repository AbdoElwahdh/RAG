package com.example.rag;

import com.example.rag.service.RagService;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RagApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(RagApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.setBannerMode(Banner.Mode.OFF);

        ConfigurableApplicationContext context = app.run(args);


        RagService ragService = context.getBean(RagService.class);
        ragService.ingestAndChunkData();


        context.close();
    }
}
