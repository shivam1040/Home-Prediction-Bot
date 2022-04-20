package com.sts;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.sts.entity.Api;
import com.sts.repository.ApiRepository;


@SpringBootApplication
@EnableEurekaClient
public class HomePricePredictionBotGatewayApplication {
	@Autowired
	private ApiRepository apiRepository;
	public static void main(String[] args) {
		SpringApplication.run(HomePricePredictionBotGatewayApplication.class, args);
	}
	
	@PostConstruct
	public void save() {
		Api api=new Api("7a8a2508-04fd-49c2-bbf8-74fb33abee5b");
		apiRepository.save(api);
	}
}