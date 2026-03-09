package ru.javarush.dao;

import org.hibernate.query.Query;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.javarush.domain.City;

import java.util.List;

@RequiredArgsConstructor
public class CityDAO {
    private final SessionFactory sessionFactory;

    public City getById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Query<City> query = session.createQuery("select c from City c join fetch c.country ct left join fetch ct.languages where c.id = :ID", City.class);
            query.setParameter("ID", id);
            return query.getSingleResult();
        }
    }

    public List<City> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<City> query = session.createQuery("select c from City c left join fetch c.country ct left join fetch ct.languages", City.class);
            return query.list();
        }
    }

    public Long getTotalCount() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("select count(*) from City", Long.class);
            return query.uniqueResult();
        }
    }
}
