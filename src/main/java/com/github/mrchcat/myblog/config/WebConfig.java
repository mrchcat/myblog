package com.github.mrchcat.myblog.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan(basePackages = {"com.github.mrchcat.myblog"})
@PropertySource("classpath:application.properties")
@EnableWebMvc
public class WebConfig {
}
