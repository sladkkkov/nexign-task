package ru.sladkkov.brt.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.sladkkov.common.dto.CallDataRecordPlusDto;
import ru.sladkkov.common.dto.CallInfoDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
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
    public ConsumerFactory<String, CallInfoDto> consumerFactory() {
        Map<String, Object> config = new HashMap<>();

        var callDataRecordDtoJsonDeserializer = new JsonDeserializer<CallInfoDto>();
        callDataRecordDtoJsonDeserializer.addTrustedPackages("*");

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), callDataRecordDtoJsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CallInfoDto> kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, CallInfoDto> concurrentKafkaListenerContainerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();

        concurrentKafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
        concurrentKafkaListenerContainerFactory.setMissingTopicsFatal(false);
        return concurrentKafkaListenerContainerFactory;
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, CallInfoDto> repliesContainer(
            ConcurrentKafkaListenerContainerFactory<String, CallInfoDto> containerFactory) {

        ConcurrentMessageListenerContainer<String, CallInfoDto> repliesContainer =
                containerFactory.createContainer("brt-reply-topic");

        repliesContainer.getContainerProperties().setGroupId("brt-topic-reply-default");
        repliesContainer.setAutoStartup(false);
        return repliesContainer;
    }

    @Bean
    public KafkaTemplate<String, CallDataRecordPlusDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ReplyingKafkaTemplate<String, CallDataRecordPlusDto, CallInfoDto> replyingTemplate(
            ProducerFactory<String, CallDataRecordPlusDto> pf,
            ConcurrentMessageListenerContainer<String, CallInfoDto> repliesContainer) {

        return new ReplyingKafkaTemplate<>(pf, repliesContainer);
    }


}
