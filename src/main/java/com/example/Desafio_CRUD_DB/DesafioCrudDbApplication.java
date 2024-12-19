package com.example.Desafio_CRUD_DB;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@SpringBootApplication
@Configuration
public class DesafioCrudDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioCrudDbApplication.class, args);
	}

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(ctx-> LocalDate.parse(ctx.getSource(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")), String.class, LocalDate.class);
        return modelMapper;
    }
}
