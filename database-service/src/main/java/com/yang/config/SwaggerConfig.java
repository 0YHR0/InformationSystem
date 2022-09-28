package com.yang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * @author YHR
 * @date 2022/8/5 14:15:18
 * @description
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket(Environment environment){
        Profiles profiles = Profiles.of("dev", "test");
        Boolean flag = environment.acceptsProfiles(profiles);
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .enable(flag)
                .groupName("postgresql")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yang.web"))
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("Yang Haoran", " ", "97868579@qq.com");
        return new ApiInfo(
                "Postgresql",
                "API for Postgresql",
                "v1.0",
                " ",
                contact,
                "Apach 2.0",
                "http://apache.org/license/LICENSE2.0 ",
                new ArrayList<>()
        );
    }
}
