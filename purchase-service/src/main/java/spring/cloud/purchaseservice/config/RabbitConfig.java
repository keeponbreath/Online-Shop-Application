package spring.cloud.purchaseservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {
    //to receive a message to create a purchase
    @Bean
    public Queue purchaseCreateQueue() {
        return new Queue("purchase_create");
    }

    //to send a message to storage service to return an item
    //to send a message to account service to pay/return for item
    //to send a message to organization service to get/give money for item
    @Bean
    DirectExchange purchaseExchange() {
        return new DirectExchange("purchase_action", true, false);
    }

    @Bean
    Binding purchaseCreateBinding(Queue purchaseCreateQueue, DirectExchange purchaseCreateExchange) {
        return BindingBuilder.bind(purchaseCreateQueue).to(purchaseCreateExchange).with("purchase_create");
    }


}