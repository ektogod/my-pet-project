package com.tinkoff_lab.dao;

import java.util.List;

public interface DAO<T, ID> {      // I decided its a good idea creating an interface for dao
    ID insert(T entity);

    T findByID(ID id);

    List<T> findAll();

    void update(ID id, T entity);

    void delete(ID id);

}
