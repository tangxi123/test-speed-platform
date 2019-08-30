package org.tangxi.testplatform.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


import org.tangxi.testplatform.model.TestCase;
import org.tangxi.testplatform.model.log.ModuleMapLog;
import org.tangxi.testplatform.model.log.ModuleMapLogTree;
import org.tangxi.testplatform.model.log.TestCaseLog;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface LogMapper {
    //插入测试用例的日志
    int insertLog(TestCaseLog log);

    //更新测试用例的日志
    int updateLog(TestCaseLog log);

    //插入模块的日志
    int insertModuleMapLog(List<ModuleMapLog> moduleMapLogs);

    //根据reportId获取模块日志树
    List<ModuleMapLogTree> getModuleMapLogTreeByModuleIdAndReportId(@Param("moduleId") int moduleId, @Param("reportId") String reportId);

    //根据测试用例日志名字查询测试用例
    TestCaseLog getTestCaseLogByName(@Param("name") String name);

    //根据测试用例id查询所有的测试结果
    List<TestCaseLog> getTestCaseLogsById(@Param("testCaseId") String testCaseId );

    //根据id查询测试日志明细
    String getTestCaseLogInfoById(@Param("id") String id);

    //根据id查询测试用例日志
    TestCaseLog getTestCaseLogById(@Param("id") String id);

    //根据测试用例名称、测试用例、模块id描述查询用例
    List<TestCaseLog> getTestCaseLogsByFields(Map<String, Object> params);
}

