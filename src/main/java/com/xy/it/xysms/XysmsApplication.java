package com.xy.it.xysms;

import com.xy.it.xysms.repository.config.impl.RedisRepositoryConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
public class XysmsApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(XysmsApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(XysmsApplication.class);
	}
}



//@SpringBootApplication
//public class XysmsApplication  {
//
//	public static void main(String[] args) {
//		SpringApplication.run(XysmsApplication.class, args);
//	}
//}

