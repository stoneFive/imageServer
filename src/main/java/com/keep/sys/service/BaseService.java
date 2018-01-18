package com.keep.sys.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

/**
 * Created by huiwu on 2014/9/21.
 */
public interface BaseService <T,PK extends Serializable>{

    public T get(PK id);

    public T save(T model);

    public void delete(PK id);

    public T update(T model);

    public List<T> listAll();
    public List<T> find(Map<String, Object> searchParams);
    /**
     * @param searchParams
     * @param pageNumber
     * @param pageSize
     * @param sortType
     * @return
     */
    Page<T> search(Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType, String sortField);
}
