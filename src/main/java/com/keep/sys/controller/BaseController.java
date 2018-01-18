package com.keep.sys.controller;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Maps;
import com.keep.sys.service.BaseService;
import com.keep.util.ReflectionUtils;
import com.keep.util.Servlets;

/**
 * Created by huiwu on 2014/10/19.
 */
public abstract class BaseController<Entity,PK extends Serializable,EntityService extends BaseService<Entity, PK>> {

    private Class<Entity> entityClass;

    protected static final String PAGESIZE = "10";

    protected Map<String, String> sortFields = Maps.newLinkedHashMap();

    protected EntityService entityService;

    public BaseController(){
        this.entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
    }

    @RequestMapping("/index.html")
    public String page(@RequestParam(value = "sortType", defaultValue = "auto") String sortType,String sortField,
                       @RequestParam(value = "page", defaultValue = "1") int pageNumber,@RequestParam
            (value = "pageSize", defaultValue = PAGESIZE) int pageSize, Model model,HttpServletRequest request){
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Page<Entity> page = entityService.search(searchParams, pageNumber, pageSize, sortType, sortField);
        model.addAttribute("page", page);
        model.addAttribute("sortType", sortType);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortFields", sortFields);
        String msg = (String) request.getAttribute("msg");
        if (StringUtils.isNotBlank(msg)) {
        	model.addAttribute("msg", msg);
		}
        return getView()+"/index";
    }

    @RequestMapping("/edit.html")
     public String edit(PK id,Model model){
        if (id!=null){
            Object entity  = entityService.get(id);
            model.addAttribute("entity",entity);
        }
        return getView()+"/edit";
    }

    private String getView(){
        return entityClass.getSimpleName().substring(0,1).toLowerCase()+entityClass.getSimpleName().substring(1);
    }

    public abstract void setEntityService(EntityService entityService);

}
