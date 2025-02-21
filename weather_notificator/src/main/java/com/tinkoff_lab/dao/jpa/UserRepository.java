package com.tinkoff_lab.dao.jpa;

import com.tinkoff_lab.entity.City;
import com.tinkoff_lab.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query(value = "delete from user_city u where u.users_chat_id = :chatId", nativeQuery = true)
    void deleteCitiesFromUser(@Param("chatId") long chatId);

    @Modifying
    @Query(value = "delete from user_email u where u.users_chat_id = :chatId", nativeQuery = true)
    void deleteEmailsFromUser(@Param("chatId") long chatId);

    @Query(value = "select * from user_city u where u.users_chat_id = :chatId", nativeQuery = true)
    List<City> getUserCities(@Param("chatId") long chatId);
}
