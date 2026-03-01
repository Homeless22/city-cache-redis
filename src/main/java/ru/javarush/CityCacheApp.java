package ru.javarush;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.javarush.dao.CityDAO;
import ru.javarush.dao.CountryDAO;
import ru.javarush.db.DatabaseSessionFactory;
import ru.javarush.domain.City;
import ru.javarush.dto.CityCountryDto;
import ru.javarush.mapper.CityMapper;
import ru.javarush.redis.MyRedisClient;

import java.util.List;

@Slf4j
public class CityCacheApp {
    private final SessionFactory sessionFactory;
    private final MyRedisClient redisClient;
    private final CityDAO cityDAO;
    private final CountryDAO countryDAO;

    CityCacheApp() {
        sessionFactory = DatabaseSessionFactory.getSessionFactory();
        redisClient = new MyRedisClient();
        cityDAO = new CityDAO(sessionFactory);
        countryDAO = new CountryDAO(sessionFactory);
    }

    public static void main(String[] args) {
        CityCacheApp app = new CityCacheApp();
        List<CityCountryDto> preparedData = CityMapper.toDtos(app.fetchAllCities());
        app.pushToRedis(preparedData);
        log.info("Загружено городов: " + preparedData.size());
        app.shutdown();
    }

    private List<City> fetchAllCities() {
        try (Session session = sessionFactory.getCurrentSession()) {
            List<City> allCities;
            session.beginTransaction();
            allCities = cityDAO.getAll();
            session.getTransaction().commit();
            return allCities;
        }
    }

    private void pushToRedis(List<CityCountryDto> preparedData) {
        redisClient.push(preparedData);
    }

    private void fetchFromRedis (List<Long> ids) {
        redisClient.fetch(ids);
    }

    private void shutdown() {
        DatabaseSessionFactory.shutdown();
        redisClient.shutdown();
    }
}