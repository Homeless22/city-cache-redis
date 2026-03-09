package ru.javarush;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import ru.javarush.dao.CityDAO;
import ru.javarush.db.DatabaseSessionFactory;
import ru.javarush.domain.City;
import ru.javarush.mapper.CityMapper;
import ru.javarush.redis.MyRedisClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 5)
@Measurement(iterations = 3, time = 5)
@Fork(1)
public class BenchmarkTest {

    private static SessionFactory sessionFactory;
    private static CityDAO cityDAO;
    private static MyRedisClient redisClient;
    private static final List<Long> testIds = List.of(1L, 20L, 45L, 100L, 250L, 300L, 400L, 500L, 600L, 700L);

    public static void main(String[] args) {
        Options options = new OptionsBuilder()
                .include(BenchmarkTest.class.getSimpleName())
                .build();
        try {
            new Runner(options).run();
        } catch (RunnerException e) {
            throw new RuntimeException(e);
        }
    }

    @Setup(Level.Trial)
    public void setup() {
        sessionFactory = DatabaseSessionFactory.getSessionFactory();
        redisClient = new MyRedisClient();
        cityDAO = new CityDAO(sessionFactory);
        redisClient.push(CityMapper.toDtos(cityDAO.getAll()));
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        DatabaseSessionFactory.shutdown();
        if (redisClient != null) {
            redisClient.shutdown();
        }
    }

    @Benchmark
    public void readFromRedis() {
        long millis = System.currentTimeMillis();
        redisClient.fetch(testIds);
    }

    @Benchmark
    public void readFromMySQL() {
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
    }
}