package ro.mpp2024.Repo;

import ro.mpp2024.Domain.Entity;

public interface Repo<idType, T extends Entity<idType>> {
    Iterable<T> getAll();

    T getById(idType id);

    T save(T entity);

    T delete(idType id);

    T update(T entity);
}