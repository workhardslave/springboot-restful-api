package com.restful.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/*
 * Swagger
 * 문서 자동화 툴로, 간단한 설정만으로 테스트 가능한 Web UI를 지원하므로 API 테스트를 위한 부가적인 프로그램을 설치할 필요가 없다.
 * 최소한의 작업으로 API Document를 만들어주므로, 클라이언트 개발자에게 문서 내용을 전달하기 위해 추가 작업을 할 필요가 없다.
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket swaggerApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(swaggerInfo()).select()
                // 해당 패키지 아래의 controller 내용을 읽어 매핑된 리소스들을 문서화한다.
                .apis(RequestHandlerSelectors.basePackage("com.restful.api.controller"))
                .paths(PathSelectors.any()) // PathSelectors.ant("/v1/**") 이런식으로 문서화 할 리소스들을 필터링 할 수 있다.
                .build()
                .useDefaultResponseMessages(false); // 기본으로 세팅되는 200,401,403,404 메시지를 표시 하지 않음
    }

    // 문서에 대한 설명과 작성자 정보를 노출시킬 수 있다.
    private ApiInfo swaggerInfo() {
        return new ApiInfoBuilder().title("Spring API Documentation")
                .description("앱 개발시 사용되는 서버 API에 대한 연동 문서입니다")
                .license("workhardslave").licenseUrl("https://github.com/workhardslave/springboot-restful-api").version("1").build();
    }
}
