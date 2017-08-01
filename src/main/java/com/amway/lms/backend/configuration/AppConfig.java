package com.amway.lms.backend.configuration;

import java.io.File;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.amway.lms.backend.common.Common;

@Configuration
@EnableWebMvc
@EnableAsync
@ComponentScan(basePackages = "com.amway.lms.backend")
@PropertySource(value = { "classpath:application.properties", "classpath:/configuration.properties" })
public class AppConfig extends WebMvcConfigurerAdapter {

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigIn() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**", "/files/**", "/amway-file/**").addResourceLocations("/resources/",
                "/files/", getRootPathQuestionFile());
    }

    private String getRootPathQuestionFile() {
        //String homePath = System.getProperty("user.home");
        String homePath = "";
        String rootPathQuestionFile = "file:" + homePath + Common.PATH_FOLDER_AMWAY_RESOURCES_EXTERNAL + File.separator;
        System.err.println("Appconfig - Path: " + rootPathQuestionFile);

        File root = File.listRoots()[0];
        for (int i = 0; i < root.listFiles().length; i++) {
            System.err.println("File in root " + (i+1) + ": " + root.listFiles()[i].getName());
        }
        return rootPathQuestionFile;
    }

//    @Override
//    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
//        configurer.enable();
//    }
}
