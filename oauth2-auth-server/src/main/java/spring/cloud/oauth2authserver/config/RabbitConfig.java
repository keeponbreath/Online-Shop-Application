package spring.cloud.oauth2authserver.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {
    @Bean
    DirectExchange createExchange() {
        return new DirectExchange("user_create", true, false);
    }

    //todo user edit queue
//    @Bean
//    DirectExchange editExchange() {
//        return new DirectExchange("user_edit", true, false);
//    }
}
