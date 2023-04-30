package ru.sladkkov.hrs.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.sladkkov.common.dto.CallDataRecordPlusDto;
import ru.sladkkov.common.dto.CallInfoDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${kafka.url}")
    private String kafkaUrl;
    @Bean
    public ProducerFactory<String, CallInfoDto> producerFactory() {

        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);


        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public ConsumerFactory<String, CallDataRecordPlusDto> consumerFactory() {
        Map<String, Object> config = new HashMap<>();

        var callDataRecordDtoJsonDeserializer = new JsonDeserializer<CallDataRecordPlusDto>();
        callDataRecordDtoJsonDeserializer.addTrustedPackages("*");

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), callDataRecordDtoJsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CallDataRecordPlusDto> kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, CallDataRecordPlusDto> concurrentKafkaListenerContainerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();

        concurrentKafkaListenerContainerFactory.setReplyTemplate(kafkaTemplate());
        concurrentKafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
        concurrentKafkaListenerContainerFactory.setMissingTopicsFatal(false);
        return concurrentKafkaListenerContainerFactory;
    }

    @Bean
    public KafkaTemplate<String, CallInfoDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
