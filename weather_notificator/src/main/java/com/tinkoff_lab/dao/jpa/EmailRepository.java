package com.tinkoff_lab.dao.jpa;

import com.tinkoff_lab.entity.City;
import com.tinkoff_lab.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<Email, String> {
    Optional<Email> findByCode(String code);

    @Modifying
    @Query(value = "delete from email_city e where e.emails_email = :email", nativeQuery = true)
    void deleteCitiesFromEmail(@Param("email") String email);

    @Query(value = "select c from City c join c.emails e where e.email = :email")  // JPQL
    List<City> getUserCities(@Param("email") String email);
}