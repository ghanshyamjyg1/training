package com.example.producerRabbitmq.producerRabbitmq.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
	public static final String ROUTING_A = "routing.A";
	public static final String ROUTING_B = "routing.B";

	@Bean
	Queue queueA() {
//		return new Queue("queue.AA", false);
		return new AnonymousQueue();
	}

	@Bean
	Queue queueB() {
		return new Queue("queue.BB", false);
	}
	@Bean
	Queue allqueue() {
		return new Queue("queue.all", false);
	}

	@Bean
	HeadersExchange exchange() {
		return new HeadersExchange("exchange.headers");
	}

	@Bean
	Binding bindingA(Queue queueA, HeadersExchange exchange) {
		return BindingBuilder.bind(queueA).to(exchange)
				.where("colour")
				.matches("red");
//		.with(ROUTING_A);
	}

	@Bean
	Binding bindingB(Queue queueB, HeadersExchange exchange) {
		return BindingBuilder.bind(queueB).to(exchange)
				.where("colour")
				.matches("blue");
				
//				.with(ROUTING_B);
	}
	@Bean
	Binding bindingall(Queue allqueue, HeadersExchange exchange) {
		return BindingBuilder.bind(allqueue).to(exchange)
				.where("colour")
				.matches("green");
//				.with("routing.*");
	}

	@Bean
	MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	RabbitTemplate rabbitTemplate(ConnectionFactory factory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
		rabbitTemplate.setMessageConverter(messageConverter());
		return rabbitTemplate;
	}
}