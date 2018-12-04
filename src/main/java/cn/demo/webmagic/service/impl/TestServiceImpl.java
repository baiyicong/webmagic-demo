package cn.demo.webmagic.service.impl;

import cn.demo.webmagic.mapper.TestMapper;
import cn.demo.webmagic.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestMapper testDao;

    @Override
    public Map<String, Object> getPojoById(String tableName, Long id) {
        return testDao.getPojoById(tableName, id);
    }

}
