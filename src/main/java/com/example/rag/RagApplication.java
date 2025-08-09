package com.example.rag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RagApplication {

    public static void main(String[] args) {
        // هذا السطر سيقوم بتشغيل التطبيق كخادم ويب ويجعله يعمل باستمرار
        SpringApplication.run(RagApplication.class, args);
    }
}
