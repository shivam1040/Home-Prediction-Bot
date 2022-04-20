/*
 * Security configuration definition for IP based access
 */

package com.sts.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter {

    private static final String HAS_SONG_SERVICE_IPS = "hasIpAddress('127.0.0.1') or hasIpAddress('localhost')";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers(HttpMethod.GET,"/**").access(HAS_SONG_SERVICE_IPS);
    }
}
