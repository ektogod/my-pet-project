package com.tinkoff_lab.dao.jpa;

import com.tinkoff_lab.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<Email, String> {
    Optional<Email> findByCode(String code);
}
