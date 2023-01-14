package pjwstk.s20124.tin.services;

import jakarta.validation.Valid;
import pjwstk.s20124.tin.exception.BadRequestException;

import java.util.Collection;
import java.util.Optional;

public interface CrudApi<T> {

    T create(@Valid T entity);

    T update(@Valid T entity) throws Exception;

    void delete(Long id);

    Optional<T> getOne(Long id);

    Collection<T> getList();


}
