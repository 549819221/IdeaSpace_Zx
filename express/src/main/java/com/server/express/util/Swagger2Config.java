package com.server.express.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author kangjia@xx.com
 * @date 2019/10/14 9:27
 */
@Configuration
@EnableSwagger2
@Component
public class Swagger2Config {

    @Value("${spring.profiles.active}")
    private String active;

    @Bean
    public Docket swaggerPersonApi10() {
        return new Docket( DocumentationType.SWAGGER_2 )
                .enable(ParamEnum.properties.dev.getCode().equals( active ))
                .select()
                //.apis( RequestHandlerSelectors.basePackage( "com.unidata.cloud.logservice.api.controller" ) )
                .paths( PathSelectors.any() )
                .build()
                .apiInfo( apiInfo() );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .version("1.0")
                .title("寄递业务项目：数据上传平台 ")
                //.contact(new Contact("  xx团队", "https://www.xx.com/", "kangjia@xx.com"))
                .description("logservice platform API v1.0")
                .build();
    }
}