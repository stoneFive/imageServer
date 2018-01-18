package com.keep.sys.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.google.common.collect.Lists;
import com.keep.sys.repository.BaseRepository;
import com.keep.sys.service.BaseService;
import com.keep.util.DynamicSpecifications;
import com.keep.util.ReflectionUtils;
import com.keep.util.SearchFilter;

/**
 * 
 * @author lance
 * @2016年3月27日
 * @下午12:50:59
 * @TODO
 */
public abstract class BaseServiceImpl<T,PK extends Serializable,EntityRepository extends BaseRepository<T, PK>> implements BaseService<T,PK> {

    private Class<T> entityClass;

    protected EntityRepository repository;

    public BaseServiceImpl(){
        this.entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
    }

    public abstract void setEntityRepository(EntityRepository repository);

    @Override
    public T get(PK id) {
        return repository.findOne(id);
    }

    @Override
    public T save(T model) {
        return repository.save(model);
    }

    @Override
    public void delete(PK id) {
        repository.delete(id);
    }

    @Override
    public T update(T model) {
        return repository.save(model);
    }

    @Override
    public List<T> listAll() {
        return Lists.newArrayList(repository.findAll());
    }
    
    @Override
    public List<T> find(Map<String, Object> searchParams) {
    	if (searchParams!=null) {
    		Specification<T> spec = buildSpecification(searchParams);
    		return repository.findAll(spec);
    	}
    	return listAll();
    }

    @Override
    public Page<T> search(Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType,String sortField) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType,sortField);
        if (searchParams!=null) {
            Specification<T> spec = buildSpecification(searchParams);
            return repository.findAll(spec, pageRequest);
        }
        return repository.findAll(pageRequest);
    }

    protected PageRequest buildPageRequest(int pageNo, int pagzSize, String sortType){
        return buildPageRequest(pageNo, pagzSize, sortType,null);
    }

    /**
     * 创建动态查询条件组合.
     */
    protected Specification<T> buildSpecification(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<T> spec = DynamicSpecifications.bySearchFilter(filters.values(), entityClass);
        return spec;
    }




    /**
     * 创建分页请求.
     */
    protected PageRequest buildPageRequest(int pageNo, int pagzSize, String sortType,String sortFiled) {
        Sort sort = null;
        Sort.Direction drection = Sort.Direction.ASC;
        if ("auto".equals(sortType)|| StringUtils.isBlank(sortType)) {
            drection = Sort.Direction.ASC;
        } else if ("DESC".equalsIgnoreCase(sortType)) {
            drection = Sort.Direction.DESC;
        }
        if (StringUtils.isNotBlank(sortFiled)) {
            sort = new Sort(drection,sortFiled);
        }else{
            sort = new Sort(drection,"id");
        }
        return new PageRequest(pageNo - 1, pagzSize, sort);
    }
}
