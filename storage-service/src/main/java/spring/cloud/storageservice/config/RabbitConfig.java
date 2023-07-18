package spring.cloud.storageservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {
    @Bean
    public Queue storageCountQueue() {
        return new Queue("storage_count");
    }

    @Bean
    DirectExchange purchaseExchange() {
        return new DirectExchange("purchase_action", true, false);
    }

    //to receive a message from item service to reserve an item
    @Bean
    Binding storageCountPurchaseCreateBinding(Queue storageCountQueue, DirectExchange purchaseExchange) {
        return BindingBuilder.bind(storageCountQueue).to(purchaseExchange).with("purchase_create");
    }

    //to receive a message from purchase service to return an item
    @Bean
    Binding storageCountPurchaseReturnBinding(Queue storageCountQueue, DirectExchange purchaseExchange) {
        return BindingBuilder.bind(storageCountQueue).to(purchaseExchange).with("purchase_return");
    }

    //to receive a message from purchase service to return an item due to overdue
    @Bean
    Binding storageCountPurchaseOverdueBinding(Queue storageCountQueue, DirectExchange purchaseExchange) {
        return BindingBuilder.bind(storageCountQueue).to(purchaseExchange).with("purchase_overdue");
    }
}