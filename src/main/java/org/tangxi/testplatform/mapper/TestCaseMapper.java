package org.tangxi.testplatform.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tangxi.testplatform.model.TestCase;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TestCaseMapper {
    //新增测试用例
    int createTestCase(TestCase testCase);

    //修改测试用例
    int updateTestcase(TestCase testCase);

    //根据id查询测试用例
    TestCase getTestCaseById(@Param("id") int id);

    //根据测试用例名称、测试用例描述查询用例
    List<TestCase> getTestCaseByFields(Map<String,Object> params);

    //根据id逻辑删除测试用例
    int deleteTestCaseById(@Param("id") int id);

    //根据id劲用测试用例
    int disableTestCaseById(@Param("id") int id);

}
