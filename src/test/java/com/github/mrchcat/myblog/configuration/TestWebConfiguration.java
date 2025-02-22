package com.github.mrchcat.myblog.configuration;

import com.github.mrchcat.myblog.config.DataSourceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.github.mrchcat.myblog"})
public class TestWebConfiguration {
}
