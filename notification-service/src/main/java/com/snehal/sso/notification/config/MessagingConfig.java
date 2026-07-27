package com.snehal.sso.notification.config;

import com.snehal.sso.events.BaseEvent;
import org.apache.kafka.common.TopicPartition;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.backoff.FixedBackOff;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class MessagingConfig {
    public static final String QUEUE = "notification.email.queue";
    public static final String DLQ = "notification.email.dlq";
    public static final String EXCHANGE = "notification.exchange";
    public static final String DLX = "notification.dlx";
    public static final String ROUTING_KEY = "notification.email";
    public static final String DLQ_ROUTING_KEY = "notification.email.failed";

    @Bean
    Declarables notificationTopology() {
        DirectExchange exchange = new DirectExchange(EXCHANGE, true, false);
        DirectExchange dlx = new DirectExchange(DLX, true, false);
        Queue queue = QueueBuilder.durable(QUEUE)
                .deadLetterExchange(DLX)
                .deadLetterRoutingKey(DLQ_ROUTING_KEY)
                .build();
        Queue dlq = QueueBuilder.durable(DLQ).build();
        return new Declarables(
                exchange, dlx, queue, dlq,
                BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY),
                BindingBuilder.bind(dlq).to(dlx).with(DLQ_ROUTING_KEY));
    }

    @Bean
    DefaultErrorHandler kafkaErrorHandler(KafkaTemplate<Object, Object> template) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
                template,
                (record, error) -> new TopicPartition(record.topic() + ".DLT", record.partition()));
        return new DefaultErrorHandler(recoverer, new FixedBackOff(1_000L, 2L));
    }

    @Bean
    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            RabbitTemplate rabbitTemplate) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setPrefetchCount(10);
        factory.setConcurrentConsumers(2);
        factory.setMaxConcurrentConsumers(5);
        factory.setDefaultRequeueRejected(false);
        factory.setAdviceChain(org.springframework.amqp.rabbit.config.RetryInterceptorBuilder.stateless()
                .maxAttempts(3)
                .backOffOptions(1_000, 2.0, 5_000)
                .recoverer(new RepublishMessageRecoverer(rabbitTemplate, DLX, DLQ_ROUTING_KEY))
                .build());
        return factory;
    }

    @Bean(name = "notificationExecutor")
    Executor notificationExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("notification-worker-");
        executor.initialize();
        return executor;
    }
}
