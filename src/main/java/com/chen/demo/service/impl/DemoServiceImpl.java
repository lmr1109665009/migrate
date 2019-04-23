package com.chen.demo.service.impl;

import com.chen.common.component.QueryFilter;
import com.chen.common.service.impl.BaseServiceImpl;
import com.chen.demo.dao.DemoDao;
import com.chen.demo.model.Demo;
import com.chen.demo.service.DemoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @user 子华
 * @created 2018/7/31
 */
@Service
public class DemoServiceImpl extends BaseServiceImpl<Demo> implements DemoService {
    private static final Logger log= LogManager.getLogger(DemoServiceImpl.class);
    private DemoDao demoDao;

    @Autowired
    public void setDemoDao(DemoDao demoDao) {
        this.demoDao = demoDao;
        setBaseDao(demoDao);
    }

    @Override
    public void testTx(Demo demo) {
        demoDao.save(demo);
//        int a=12/0;
        log.debug("事务测试方法。。。。。。。");
    }

    @Override
    public void saveDemo(Demo demo) {
        demoDao.saveDemo(demo);
    }

    @Override
    public List<Demo> listFilter(QueryFilter filter) {
        return demoDao.getListBySqlKey(filter);
    }
}
