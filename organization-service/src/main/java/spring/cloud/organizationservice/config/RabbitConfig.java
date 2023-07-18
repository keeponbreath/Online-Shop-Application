package spring.cloud.organizationservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    //to send message to freeze all orgs items
    @Bean
    DirectExchange orgFreezeExchange() {
        return new DirectExchange("org_freeze", true, false);
    }

    //to receive a message to get/return money for item
    @Bean
    public Queue orgPaymentQueue() {
        return new Queue("org_payment");
    }

    @Bean
    DirectExchange purchaseExchange() {
        return new DirectExchange("purchase_action", true, false);
    }

    @Bean
    Binding orgIncomeBinding(Queue orgPaymentQueue, DirectExchange purchaseExchange) {
        return BindingBuilder.bind(orgPaymentQueue).to(purchaseExchange).with("purchase_pay");
    }

    @Bean
    Binding orgOutcomeBinding(Queue orgPaymentQueue, DirectExchange purchaseExchange) {
        return BindingBuilder.bind(orgPaymentQueue).to(purchaseExchange).with("purchase_return");
    }
}