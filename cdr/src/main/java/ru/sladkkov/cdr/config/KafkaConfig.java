package ru.sladkkov.cdr.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.sladkkov.cdr.dto.CallDataRecordDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    // Annotation
    @Bean

    // Method
    public ProducerFactory<String, CallDataRecordDto> producerFactory() {

        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, CallDataRecordDto> kafkaTemplate() {

        return new KafkaTemplate<>(producerFactory());
    }


    @Bean
    public NewTopic topic() {
        return TopicBuilder
                .name("randomCdr")
                .partitions(10)
                .replicas(1)
                .build();
    }
}
