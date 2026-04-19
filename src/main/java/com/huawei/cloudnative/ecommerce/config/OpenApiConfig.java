package com.huawei.cloudnative.ecommerce.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ecommerceOpenApi() {
        return new OpenAPI().info(new Info()
                .title("E-commerce Cloud-Native Demo API")
                .description("OpenAPI documentation for auth, product, and order endpoints")
                .version("v1")
                .contact(new Contact().name("Project Maintainer").email("maintainer@example.com")));
    }
}
