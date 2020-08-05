package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AmazonConfig {

	@Bean
	public AmazonS3 s3() {

		AWSCredentials awsCredentials = new BasicAWSCredentials("AKIATYGIFGXUMRTFHIXJ",
				"gxm8UcmC/9Z3154tCkGRsbGrGGj/w2593VG+9JEt");

		return AmazonS3ClientBuilder
				.standard()
				.withRegion(Regions.US_EAST_2)
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
				.build();
	}
}
