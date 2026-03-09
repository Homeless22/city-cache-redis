package ru.javarush;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
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
        runMigrations();
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

    private static void runMigrations() {
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");
        String dbUrl = DatabaseSessionFactory.getSessionFactory().getProperties().get(Environment.URL).toString();

        Flyway flyway = Flyway.configure()
                .dataSource(dbUrl, dbUser, dbPassword)
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .load();
        flyway.migrate();
    }
}