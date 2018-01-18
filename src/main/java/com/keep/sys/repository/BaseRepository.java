package com.keep.sys.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

/**
 * Created by huiwu on 2014/9/21.
 */
@NoRepositoryBean
public interface BaseRepository<T,pk extends Serializable> extends PagingAndSortingRepository<T, Serializable>,
        JpaSpecificationExecutor<T> {
}
