package com.stackroute.MusicServiceConfigServer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableConfigServer
public class MusicServiceConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicServiceConfigServerApplication.class, args);
	}
}
