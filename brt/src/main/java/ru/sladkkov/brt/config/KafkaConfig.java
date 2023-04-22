package ru.sladkkov.brt.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.sladkkov.common.dto.CallDataRecordDto;
import ru.sladkkov.common.dto.CallDataRecordPlusDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {
    @Bean
    public ProducerFactory<String, CallDataRecordPlusDto> producerFactory() {

        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);


        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public ConsumerFactory<String, CallDataRecordDto> consumerFactory() {
        Map<String, Object> config = new HashMap<>();

        var callDataRecordDtoJsonDeserializer = new JsonDeserializer<CallDataRecordDto>();
        callDataRecordDtoJsonDeserializer.addTrustedPackages("*");
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), callDataRecordDtoJsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CallDataRecordDto> kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, CallDataRecordDto> concurrentKafkaListenerContainerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();

        concurrentKafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
        concurrentKafkaListenerContainerFactory.setMissingTopicsFatal(false);
        return concurrentKafkaListenerContainerFactory;
    }

    @Bean
    public KafkaTemplate<String, CallDataRecordPlusDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }


}
