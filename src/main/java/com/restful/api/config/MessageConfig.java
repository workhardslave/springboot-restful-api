package com.restful.api.config;

import net.rakugakibox.util.YamlResourceBundle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/*
 * 스프링에서는 다국어 처리를 위해 i18n 세팅을 지원한다.
 * i18n은 국제화(Internationalization)의 약자이다. => (I + 가운데 남은 글자 수 + n)
 * 해당 세팅을 통해 "안녕하세요." => "Hello"로 표시할 수 있다.
 *
 * 스프링에서 제공하는 LocaleChangeInterceptor를 사용하여 lang이라는 RequestParameter가 요청에 있으면 해당 값을 읽어 로케일 정보를 변경한다.
 * 로케일 정보는 기본으로 Session에서 읽어오고 저장하도록 SessionLocalResolver를 사용하는데, 다른 리졸버도 있으므로 상황에 따라 적절한 리졸버를 설정하여 사용한다.
 * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/i18n/package-summary.html
 *
 */

@Configuration
public class MessageConfig implements WebMvcConfigurer {

    // 4. 세션에 지역 설정, default는 KOREAN = 'ko'
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.KOREAN);
        return slr;
    }

    // 3. 지역 설정을 변경하는 인터셉터, 요청시 파라미터에 lang 정보를 지정하면 언어가 변경된다.
   @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
   }

   // 2. 인터셉터를 시스템 레지스트리에 등록한다.
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    // 1. yml 파일을 참조하는 MessageSource를 선언한다.
    @Bean
    public MessageSource messageSource(@Value("${spring.messages.basename}") String basename,
                                       @Value("${spring.messages.encoding}") String encoding)
    {
        YamlMessageSource ms = new YamlMessageSource();
        ms.setBasename(basename);
        ms.setDefaultEncoding(encoding);
        ms.setAlwaysUseMessageFormat(true);
        ms.setUseCodeAsDefaultMessage(true);
        ms.setFallbackToSystemLocale(true);
        return ms;
    }

    // local 정보에 따라 다른 yml 파일을 읽도록 처리 (exception 발생 시 작동)
    private static class YamlMessageSource extends ResourceBundleMessageSource {
        @Override
        protected ResourceBundle doGetBundle(String basename, Locale locale) throws MissingResourceException {
            return ResourceBundle.getBundle(basename, locale, YamlResourceBundle.Control.INSTANCE);
        }
    }
}
