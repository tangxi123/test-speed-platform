package org.tangxi.testplatform.mapper;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.model.TestCase;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCaseMapperTest {

    @Autowired
    TestCaseMapper testCaseMapper;

//    @Test
//    public void getParamCountByParameter() {
//        System.out.println(testCaseMapper.getParamCountByParameter("${testCaseId}"));
//    }
//
//    @Test
//    public void getActionCountByActionName(){
//        System.out.println(testCaseMapper.getActionCountByActionName("\"插入一条数据测试\""));
//    }
//
//    @Test
//    public void getTestCasesByGroups(){
//        List<TestCase> testCases = testCaseMapper.getTestCasesByGroups("新增parameter");
//        System.out.println(JacksonUtil.toJson(testCases));
//    }
}
