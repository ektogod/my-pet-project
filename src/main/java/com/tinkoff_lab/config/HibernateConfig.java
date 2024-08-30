package com.tinkoff_lab.config;

import com.tinkoff_lab.entity.City;
import com.tinkoff_lab.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;

public class HibernateConfig {
    @Bean
    public SessionFactory sessionFactory(){
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        return configuration
                .addAnnotatedClass(City.class)
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
    }
}
