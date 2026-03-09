- Оптимизация производительности с Hibernate и Redis
- демо проект на базе Sakila

- В реляционной БД данные хранятся в разных таблицах:
    - City
    - Country
    - CountryLanguage

- Технологический стек:
    - Java 17
    - Maven
    - Hibernate
    - MySQL
    - Redis
    - P6Spy
    - Docker

- Config
    - AppConfig - настройки приложения. Загружаются из файла application.properties
- Domain:
    - City
    - Country
    - CountryLanguage
- DAO - методы получения данных из MySQL
    - CityDAO
    - CountryDAO
- Redis DTO - класс CityCountryDto (плоская структура, готова для кэша)
- Mapper - класс CityMapper для трансформации данных

Хранение логина и пароля в переменных окружения. 
Требуется настроить значения DB_PASSWORD, DB_USER

Профессиональный бенчмаркинг с JMH

Система миграций Flyway 