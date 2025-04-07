package com.mos.backend.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mos.backend.common.redis.RedisSubscriber;
import com.mos.backend.privatechatrooms.application.PrivateChatRoomService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Map;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private int redisPort;
    @Value("${spring.data.redis.private-chat-channel}")
    private String privateChatChannel;
    @Value("${spring.data.redis.study-chat-channel}")
    private String studyChatChannel;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();
        redisConfiguration.setHostName(redisHost);
        redisConfiguration.setPort(redisPort);
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfiguration);
        return lettuceConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));

        return template;
    }

    // refreshToken -> memberId
    // chatRoomId -> (userId1, userid2, ...)
    @Bean
    public RedisTemplate<String, Long> StringLongRedisTemplate() {
        RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Long.class));
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, Map<String, String>> StringMapRedisTemplate() {
        RedisTemplate<String, Map<String, String>> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Map.class));
        return redisTemplate;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListener(MessageListenerAdapter privateMessageListenerAdapter, MessageListenerAdapter studyMessageListenerAdapter, PrivateChatRoomService privateChatRoomService) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());

        container.addMessageListener(privateMessageListenerAdapter, new PatternTopic(privateChatChannel));
        container.addMessageListener(studyMessageListenerAdapter, new PatternTopic(studyChatChannel));

        return container;
    }

    @Bean
    public MessageListenerAdapter privateMessageListenerAdapter(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "onPrivateChatMessage");
    }

    @Bean
    public MessageListenerAdapter studyMessageListenerAdapter(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "onStudyChatMessage");
    }

}
