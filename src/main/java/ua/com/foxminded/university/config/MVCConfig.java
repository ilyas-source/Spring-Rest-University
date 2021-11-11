package ua.com.foxminded.university.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ua.com.foxminded.university.controller.formatter.GroupFormatter;
import ua.com.foxminded.university.controller.formatter.SubjectFormatter;

import java.util.List;

@Configuration
@ComponentScan("ua.com.foxminded.university")
@EnableWebMvc
@EnableSpringDataWebSupport
public class MVCConfig implements WebMvcConfigurer {

    @Value("${page.defaultsize}")
    private int defaultPageSize;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/sources/**")
                .addResourceLocations("/sources/css/", "/sources/img", "/sources/js");
    }



    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, defaultPageSize));
        argumentResolvers.add(resolver);
        WebMvcConfigurer.super.addArgumentResolvers(argumentResolvers);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        GroupFormatter groupFormatter = new GroupFormatter();
        SubjectFormatter subjectFormatter = new SubjectFormatter();
        registry.addFormatter(groupFormatter);
        registry.addFormatter(subjectFormatter);
    }
}
