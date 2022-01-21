package com.devthink.devthink_server;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DevThinkServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevThinkServerApplication.class, args);
    }

    @Bean
    public Mapper dozerMapper(){
        return DozerBeanMapperBuilder.buildDefault();
    }
}
