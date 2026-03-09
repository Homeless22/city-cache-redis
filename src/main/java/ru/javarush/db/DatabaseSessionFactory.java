package ru.javarush.db;


import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import ru.javarush.domain.City;
import ru.javarush.domain.Country;
import ru.javarush.domain.CountryLanguage;

import java.util.Properties;

@Slf4j
public class DatabaseSessionFactory {
    private static DatabaseSessionFactory instance;
    private final SessionFactory sessionFactory;

    private DatabaseSessionFactory() {
        Properties properties = new Properties();
        String dbUser = System.getenv("DB_USER");
        String  dbPassword = System.getenv("DB_PASSWORD");
        properties.put(Environment.USER, dbUser);
        properties.put(Environment.PASS, dbPassword);

        sessionFactory = new Configuration()
                .addProperties(properties)
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(City.class)
                .addAnnotatedClass(Country.class)
                .addAnnotatedClass(CountryLanguage.class)
                .buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        if (instance == null) {
            instance = new DatabaseSessionFactory();
        }
        return instance.sessionFactory;
    }

    public static void shutdown() {
        if (instance != null && !instance.sessionFactory.isClosed()) {
            instance.sessionFactory.close();
        }
    }
}
