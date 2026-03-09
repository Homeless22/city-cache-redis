package ru.javarush;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import ru.javarush.dao.CityDAO;
import ru.javarush.db.DatabaseSessionFactory;
import ru.javarush.domain.City;
import ru.javarush.mapper.CityMapper;
import ru.javarush.redis.MyRedisClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PerformanceTest {
    private static SessionFactory sessionFactory;
    private static CityDAO cityDAO;
    private static MyRedisClient redisClient;
    private static final List<Long> testIds = List.of(1L, 20L, 45L, 100L, 250L, 300L, 400L, 500L, 600L, 700L);

    @BeforeAll
    static void setUp() {
        sessionFactory = DatabaseSessionFactory.getSessionFactory();
        redisClient = new MyRedisClient();
        cityDAO = new CityDAO(sessionFactory);
        redisClient.push(CityMapper.toDtos(cityDAO.getAll()));
    }

    @AfterAll
    static void tearDown() {
        DatabaseSessionFactory.shutdown();
        if (redisClient != null) {
            redisClient.shutdown();
        }
    }

    @Test
    void testRedisPerformance() {
        long millis = System.currentTimeMillis();
        redisClient.fetch(testIds);
        System.out.println("Time to fetch from redis: " + (System.currentTimeMillis() - millis));
    }

    @Test
    void testMysqlPerformance() {
        long millis = System.currentTimeMillis();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            for (Long id : testIds) {
                City city = cityDAO.getById(id);
                assertNotNull(city, "Город с id " + id + " не найден в MySQL");
                int size = city.getCountry().getLanguages().size();
            }
            session.getTransaction().commit();
        }
        System.out.println("Time to fetch from MySQL: " + (System.currentTimeMillis() - millis));
    }
}