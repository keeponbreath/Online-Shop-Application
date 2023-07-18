package spring.cloud.itemservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {
    //to send a message to purchase service to create a purchase
    //to send a message to storage service to reserve item
    @Bean
    DirectExchange purchaseExchange() {
        return new DirectExchange("purchase_action", true, false);
    }


    //to receive a message from organization service to free all orgs items
    @Bean
    public Queue itemFreezeQueue() {
        return new Queue("item_freeze");
    }

    @Bean
    DirectExchange orgFreezeExchange() {
        return new DirectExchange("org_freeze", true, false);
    }

    @Bean
    Binding itemFreezeBinding(Queue itemFreezeQueue, DirectExchange orgFreezeExchange) {
        return BindingBuilder.bind(itemFreezeQueue).to(orgFreezeExchange).with("org_freeze");
    }
}