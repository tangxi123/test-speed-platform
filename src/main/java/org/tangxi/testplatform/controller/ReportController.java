package org.tangxi.testplatform.controller;

import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tangxi.testplatform.common.Response;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.model.testReport.Report;
import org.tangxi.testplatform.model.testReport.ReportInfo;
import org.tangxi.testplatform.model.testReport.ReportTestCaseInfo;
import org.tangxi.testplatform.service.ReportService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private static final Logger LOG = LoggerFactory.getLogger(TestCaseController.class);

    @Autowired
    ReportService reportService;

    /**
     *  根据报告名字、模块id分页查询测试用例
     * @param params
     * @return
     */
    @CrossOrigin
    @GetMapping("/query/")
    public Response<PageInfo<Report>> getReports(@RequestParam Map<String,Object> params){
        LOG.info("传入参数params: {}", JacksonUtil.toJson(params));
        return reportService.getReports(params);
    }

    /**
     * 根据id获取report数据
     * @param id
     * @return
     */
    @CrossOrigin
    @GetMapping("/report/{id}")
    public Response<Report> getReportById(@PathVariable String id){
        LOG.info("传入的参数id为:{}",id);
        return reportService.getReportById(id);
    }

    /**
     * 根据reportId查询下面每个suite的reportInfo数据
     * @param id
     * @return
     */
    @CrossOrigin
    @GetMapping("/report/reportInfo/{id}")
    public Response<?> getReportInfoByReportId(@PathVariable String id){
        LOG.info("传入的参数id为:{}",id);
        return reportService.getReportInfoByReportId(id);
    }

    /**
     * 根据infoId获取对应模块下的测试用例的测试结果
     * @param id
     * @return
     */
    @CrossOrigin
    @GetMapping("/report/reportInfo/testCases/{id}")
    public Response<List<ReportTestCaseInfo>> getReportTestCaseInfoByInfoId(@PathVariable String id){
        LOG.info("传入的参数id为:{}",id);
        return reportService.getReportTestCaseInfoByInfoId(id);
    }

    /**
     * 根据reportId获取对应模块id下的测试用例与模块的测试结果
     * @param reportId
     * @return
     */
    @CrossOrigin
    @GetMapping("/report/showReportByModuleId/{reportId}")
    public Response<?> getShowReportByModuleId(@PathVariable String reportId){
        LOG.info("传入的参数moduleId为：{}",reportId);
        return reportService.getShowReportByModuleId(reportId);
    }

}
