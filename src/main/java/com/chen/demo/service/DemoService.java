package com.chen.demo.service;

import com.chen.common.component.QueryFilter;
import com.chen.common.service.BaseService;
import com.chen.demo.model.Demo;

import java.util.List;

/**
 * @user 子华
 * @created 2018/7/31
 */
public interface DemoService extends BaseService<Demo> {

    public void testTx(Demo demo);

    public void saveDemo(Demo demo);

    public List<Demo> listFilter(QueryFilter filter);

}
