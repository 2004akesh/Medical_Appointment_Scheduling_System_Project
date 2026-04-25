package com.healthcare.healthcaremanagement;

import java.util.List;

public interface ManagementService<T> {
    List<T> getAll();
    void save(T item);
    void update(String id, T item);
    void delete(String id);
    T getById(String id);
}