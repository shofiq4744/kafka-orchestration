package com.egen.hsms.orchestration.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.egen.hsms.orchestration.payload.PaymentPayload;

@EnableKafka
@Configuration
public class CustomSerializerConfig {
	
	@Autowired
    private KafkaProperties kafkaProperties;
	
	@Value("${spring.kafka.consumer.group-id}")
    private String serviceGroupId;
		
	@Bean
    public ConsumerFactory<String, PaymentPayload> consumerFactory() {
        final JsonDeserializer<PaymentPayload> jsonDeserializer = new JsonDeserializer<>(PaymentPayload.class,false);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties(), new StringDeserializer(), jsonDeserializer
        );
    }
	
	@Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentPayload> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentPayload> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
	

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>(
                kafkaProperties.buildConsumerProperties()
        );
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,
        		serviceGroupId);

        return props;
    }

}
