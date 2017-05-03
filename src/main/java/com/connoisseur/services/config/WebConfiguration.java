package com.connoisseur.services.config;

import com.connoisseur.services.filter.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import javax.servlet.Filter;

/**
 * Created by rayxiao on 5/2/17.
 */
@Configuration

public class WebConfiguration extends RepositoryRestMvcConfiguration {


    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

        config.setPageParamName("p")
                .setLimitParamName("l")
                .setSortParamName("q");
    }


    @Bean
    public Filter authenticationFilter() {
        return new AuthenticationFilter();
    }
}
