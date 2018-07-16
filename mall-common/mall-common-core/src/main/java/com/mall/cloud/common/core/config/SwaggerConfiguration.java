package com.mall.cloud.common.core.config;

import com.mall.cloud.common.config.properties.MallCloudProperties;
import com.mall.cloud.common.config.properties.SwaggerProperties;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;

@EnableSwagger2
public class SwaggerConfiguration {

    @Resource
    private MallCloudProperties mallCloudProperties;

    /**
     * Reservation api docket.
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build().enable(true);
    }

    private ApiInfo apiInfo() {
        SwaggerProperties swagger = mallCloudProperties.getSwagger();
        return new ApiInfoBuilder()
                .title(swagger.getTitle())
                .description(swagger.getDescription())
                .version(swagger.getVersion())
                .license(swagger.getLicense())
                .licenseUrl(swagger.getLicenseUrl())
                .contact(new Contact(swagger.getContactName(), swagger.getContactUrl(), swagger.getContactEmail()))
                .build();
    }
}
