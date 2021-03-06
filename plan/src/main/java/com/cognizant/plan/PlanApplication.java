package com.cognizant.plan;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.cognizant.kernel.EnableKernel;


@SpringBootApplication
@EnableScheduling
@EnableKernel
public class PlanApplication {
	

	
	public static void main(String[] args) {
		SpringApplication.run(PlanApplication.class, args);
	}
	
	@Bean
	TopicExchange planExchange(@Value("${sync.amqp.plan.exchange}") String name) {
		return new TopicExchange(name, true, false); 
	}
	
	
	@Bean
	RabbitTemplate rabbitTemplate(ConnectionFactory cf) {
		RabbitTemplate template = new RabbitTemplate(cf);
		template.setMessageConverter(new Jackson2JsonMessageConverter());
		return template;
	}

	@Bean
	public Jackson2JsonMessageConverter converter() {
		return new Jackson2JsonMessageConverter();
	}
	
}
