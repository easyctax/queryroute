package com.lansoft.queryroute;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QueryrouteApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(QueryrouteApplication.class);
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);
	}
}
