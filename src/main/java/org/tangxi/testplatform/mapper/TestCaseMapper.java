package org.tangxi.testplatform.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.tangxi.testplatform.model.TestCase;

@Mapper
@Repository
public interface TestCaseMapper {
    //新增测试用例
    int createTestCase(TestCase testCase);
}
