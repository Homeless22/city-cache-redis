package ru.javarush.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;
import lombok.extern.slf4j.Slf4j;
import ru.javarush.config.AppConfig;
import ru.javarush.dto.CityCountryDto;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MyRedisClient {
    private final AppConfig redisConfig;
    private final RedisClient client;
    private final ObjectMapper mapper;
    private final static String fileProperties = "redis.properties";

    public MyRedisClient() {
        redisConfig = new AppConfig(fileProperties);
        mapper = new ObjectMapper();
        client = RedisClient.create(RedisURI.create(redisConfig.getPropertyValue("redis.host"), Integer.parseInt(redisConfig.getPropertyValue("redis.port"))));
        try (StatefulRedisConnection<String, String> connection = client.connect()) {
            log.info("Connected to Redis");
        }
    }

    public void shutdown() {
        if (client != null) {
            client.shutdown();
        }
    }

    public void push(List<CityCountryDto> dtos) {
        try (StatefulRedisConnection<String, String> connection = client.connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (CityCountryDto dto : dtos) {
                try {
                    sync.set(String.valueOf(dto.getId()), mapper.writeValueAsString(dto));
                } catch (JsonProcessingException e) {
                    log.error("Ошибка записи в Redis: {}", e);
                }
            }
        }
    }

    public List<CityCountryDto> fetch(List<Long> ids) {
        try (StatefulRedisConnection<String, String> connection = client.connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            List<CityCountryDto> dtos = new ArrayList<>();
            for (Long id : ids) {
                String value = sync.get(String.valueOf(id));
                try {
                    dtos.add(mapper.readValue(value, CityCountryDto.class));
                } catch (JsonProcessingException e) {
                    log.error("Ошибка чтения из Redis: {}", e);
                }
            }
            return dtos;
        }
    }
}
