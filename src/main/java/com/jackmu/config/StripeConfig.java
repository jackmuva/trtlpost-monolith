package com.jackmu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class StripeConfig {
    final Map<String, String> env = System.getenv();
    private String stripeApiKey;

    @Bean
    public StripeConfig getStripeConfig(){
        setStripeApiKey();
        return this;
    }

    public void setStripeApiKey(){
        stripeApiKey = env.get("stripe_api_key");
    }

    public String getStripeApiKey(){
        return stripeApiKey;
    }
}
