package com.chen.demo.dao;

import com.chen.common.dao.BaseDao;
import com.chen.demo.model.Demo;
import org.springframework.stereotype.Repository;

/**
 * @user 子华
 * @created 2018/7/31
 */
@Repository
public class DemoDao extends BaseDao<Demo> {
    public void saveDemo(Demo demo){
        getSqlSessionTemplate().insert(getNamespace()+".saveDemo",demo);
    }

}
