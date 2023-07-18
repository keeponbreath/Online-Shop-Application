package spring.cloud.accountservice.config;

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
    //to receive a message from oauth2 server to create a new account
    @Bean
    public Queue accountCreateQueue() {
        return new Queue("account_create");
    }

    @Bean
    DirectExchange userCreateExchange() {
        return new DirectExchange("user_create", true, false);
    }

    @Bean
    Binding accountCreateBinding(Queue accountCreateQueue, DirectExchange userCreateExchange) {
        return BindingBuilder.bind(accountCreateQueue).to(userCreateExchange).with("user_create");
    }

    //todo user edit queue
    //to receive a message from oauth2 server to create a new account
    //    @Bean
//    public Queue accountEditQueue() {
//        return new Queue("account_edit");
//    }
//
//    @Bean
//    DirectExchange userEditExchange() {
//        return new DirectExchange("user_edit", true, false);
//    }
//
//    @Bean
//    Binding accountEditBinding(Queue accountEditQueue, DirectExchange userEditExchange) {
//        return BindingBuilder.bind(accountEditQueue).to(userEditExchange).with("user_edit");
//    }

    //to receive a message from purchase service to decrease/increase accounts balance
    @Bean
    public Queue accountPaymentQueue() {
        return new Queue("account_payment");
    }

    @Bean
    DirectExchange purchaseExchange() {
        return new DirectExchange("purchase_action", true, false);
    }

    @Bean
    Binding accountOutcomeBinding(Queue accountPaymentQueue, DirectExchange purchaseExchange) {
        return BindingBuilder.bind(accountPaymentQueue).to(purchaseExchange).with("purchase_pay");
    }

    @Bean
    Binding accountIncomeBinding(Queue accountPaymentQueue, DirectExchange purchaseExchange) {
        return BindingBuilder.bind(accountPaymentQueue).to(purchaseExchange).with("purchase_return");
    }
}
