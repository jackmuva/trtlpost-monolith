package com.jackmu.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Map;

@Configuration
@Profile("!local-profile")
public class AwsS3Config {
    final Map<String, String> env = System.getenv();
    public AWSCredentials credentials() {
        AWSCredentials credentials = new BasicAWSCredentials(
                env.get("aws_access_key"),
                env.get("aws_secret_key")
        );
        return credentials;
    }

    @Bean
    public AmazonS3 amazonS3(){
        AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials()))
                .withRegion(Regions.US_EAST_1)
                .build();
        return s3client;
    }
}
