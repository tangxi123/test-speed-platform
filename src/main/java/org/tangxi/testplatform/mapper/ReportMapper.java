package org.tangxi.testplatform.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tangxi.testplatform.common.Response;
import org.tangxi.testplatform.model.testReport.Report;
import org.tangxi.testplatform.model.testReport.ReportInfo;
import org.tangxi.testplatform.model.testReport.ReportTestCaseInfo;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ReportMapper {
    //向数据表插入report式布局
    int insertReport(Report report);

    //插入reportInfo数据
    int insertReportInfo(List<ReportInfo> reportInfos);

    //根据报告名字、模块id分页查询测试用例
    List<Report> getReportsByFields(Map<String,Object> params);

    //根据id查询report数据
    Report getReportById(@Param("id") String id);

    //根据reportId查询reportInfo数据
    List<ReportInfo> getReportInfosByReportId(@Param("reportId") String reportId);

    //根据infoId获取对应模块下的测试用例的测试结果
    List<ReportTestCaseInfo> getReportTestCaseInfoByInfoId(@Param("reportInfoId") String reportInfoId);
}
