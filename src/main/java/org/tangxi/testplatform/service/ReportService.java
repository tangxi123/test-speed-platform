package org.tangxi.testplatform.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.dialect.helper.HsqldbDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.tangxi.testplatform.common.Response;
import org.tangxi.testplatform.common.exception.UnexpectedReportException;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.mapper.LogMapper;
import org.tangxi.testplatform.mapper.ModuleMapper;
import org.tangxi.testplatform.mapper.ReportMapper;
import org.tangxi.testplatform.mapper.TestCaseMapper;
import org.tangxi.testplatform.model.Module;
import org.tangxi.testplatform.model.TestCase;
import org.tangxi.testplatform.model.log.ModuleMapLogTree;
import org.tangxi.testplatform.model.log.TestCaseLog;
import org.tangxi.testplatform.model.testReport.Report;
import org.tangxi.testplatform.model.testReport.ReportInfo;
import org.tangxi.testplatform.model.testReport.ReportTestCaseInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportService.class);

    private static final int RUN_FAIL_STATUS = 0; //测试失败
    private static final int RUN_SUCCESS_STATUS = 1; //测试成功
    private static final int NO_TESTCASE_STATUS = 2; //没有测试用例

    @Autowired
    ReportMapper reportMapper;
    
    @Autowired
    LogMapper logMapper;

    @Autowired
    ModuleMapper moduleMapper;

    @Autowired
    TestCaseMapper testCaseMapper;


    /**
     * 根据报告名字、模块id分页查询测试用例
     * @param params
     * @return
     */
    public Response<PageInfo<Report>> getReports(Map<String,Object> params){
        try {
            int pageNum = Integer.parseInt((String)params.get("pageNum"));
            int pageSize = Integer.parseInt((String)params.get("pageSize"));
            PageHelper.startPage(pageNum, pageSize);
            List<Report> reportsByFields = reportMapper.getReportsByFields(params);
            PageInfo<Report> reports = new PageInfo<>(reportsByFields);
            LOG.info("查询到的报告列表：{}", JacksonUtil.toJson(reportsByFields));
            LOG.info("返回的分页列表为：{}", JacksonUtil.toJson(reports));
            return new Response<>(200, reports, "查询成功");
        } catch (Exception e) {
            throw new UnexpectedReportException(e);
        }
    }

    /**
     * 根据id查询报告数据
     * @param id
     * @return
     */
    public Response<Report> getReportById(String id){
        try{
            Report report = reportMapper.getReportById(id);
            LOG.info("查询到的报告为：{}", JacksonUtil.toJson(report));
            return new Response<>(200,report,"查询成功");
        }catch (Exception e){
            throw new UnexpectedReportException(e);
        }
    }

    /**
     * 根据reportId查询下面每个suite的reportInfo数据
     * @param id
     * @return
     */
    public Response<?> getReportInfoByReportId(String id){
        try{
            List<ModuleMapLogTree> moduleMapLogTrees = logMapper.getModuleMapLogTreeByModuleIdAndReportId(0, id);
//            for(ModuleMapLogTree moduleMapLogTree : moduleMapLogTrees){
////                countPassFail(moduleMapLogTree);
//                System.out.println("通过的数为："+countPass(moduleMapLogTree));
//            }
            LOG.info("查询到的详细报告信息为：{}",JacksonUtil.toJson(moduleMapLogTrees));
            return new Response<>(200,moduleMapLogTrees,"查询成功");
        }catch  (Exception e){
            throw new UnexpectedReportException(e);
        }
    }

    private int getResultStatus(int pass_count, int fail_count){
        if(fail_count > 0){
            return RUN_FAIL_STATUS;
        }else if(fail_count == 0 && pass_count > 0){
            return RUN_SUCCESS_STATUS;
        }else{
            return NO_TESTCASE_STATUS;
        }
    }

    private int countPass(ModuleMapLogTree moduleMapLogTree){
        List<ModuleMapLogTree> children = moduleMapLogTree.getChildren();
        if(children.size() == 0){
            int passed_count = moduleMapLogTree.getPassedCount();
            return passed_count;
        }else{
            int passed_count = 0;
            for(ModuleMapLogTree child : children){
                int child_passed_count = child.getPassedCount() + countPass(child);
                passed_count = passed_count + child_passed_count;
            }
            return passed_count;
        }

    }

    /**
     * 根据infoId获取对应模块下的测试用例的测试结果
     * @param id
     * @return
     */
    public Response<List<ReportTestCaseInfo>> getReportTestCaseInfoByInfoId(String id){
        try{
            List<ReportTestCaseInfo> reportTestCaseInfos = reportMapper.getReportTestCaseInfoByInfoId(id);
            LOG.info("查询到的测试用例的报告信息为：{}",JacksonUtil.toJson(reportTestCaseInfos));
            return new Response<>(200,reportTestCaseInfos,"查询成功");
        }catch (Exception e){
            throw new UnexpectedReportException(e);
        }
    }

    /**
     * 根据reportId获取对应模块id下的测试用例与模块的测试结果
     * @param reportId
     * @return
     */
    public Response<?> getShowReportByModuleId(String reportId){

        Report report = reportMapper.getReportById(reportId);
        List<Integer> testCaseLogIds = report.getTestCaseLogIds();
        if(testCaseLogIds.size()<1){
            return new Response<>(400,null,"没有执行的测试用例");
        }

        List<ReportInfo> moduleReportInfos = reportMapper.getReportInfosByReportId(reportId);
        List<HashMap<String,Object>> showReports = new ArrayList<>();

        for(ReportInfo reportInfo : moduleReportInfos){
            HashMap<String,Object> showModuleReportInfo = new HashMap<>();
            showModuleReportInfo.put("name",reportInfo.getModuleName());
            showModuleReportInfo.put("passedCount",reportInfo.getPassedTcCount());
            showModuleReportInfo.put("failedCount",reportInfo.getFailedTcCount());
            showModuleReportInfo.put("testResultStatus",reportInfo.getTestResultStatus());
            showModuleReportInfo.put("type","module");
            int moduleId = reportInfo.getModuleId();
            List<Module> subModules = moduleMapper.getSubModulesById(moduleId);
//            for(Module module : subModules){
//                int subModuleId = module.getId();
////                testCaseMapper.get
//                for(int testCaseLogId : testCaseLogIds){
//                    TestCaseLog testCaseLog = logMapper.getTestCaseLogById("" + testCaseLogId);
//                    int testCaseId = testCaseLog.getTestCaseId();
//                    TestCase testCase = testCaseMapper.getTestCaseById(testCaseId);
//
//
//                }
//            }

            showReports.add(showModuleReportInfo);
        }

        for(int testCaseLogId : testCaseLogIds){
            TestCaseLog testCaseLog = logMapper.getTestCaseLogById("" + testCaseLogId);
            int testCaseId = testCaseLog.getTestCaseId();
            TestCase testCase = testCaseMapper.getTestCaseById(testCaseId);

            HashMap<String,Object> showTestCaseReportInfo = new HashMap<>();

            showTestCaseReportInfo.put("name",testCaseLog.getName());
            showTestCaseReportInfo.put("passedCount",testCaseLog.getPassedTcCount());
            showTestCaseReportInfo.put("failedCount",testCaseLog.getFailedTcCount());
            showTestCaseReportInfo.put("testResultStatus",testCaseLog.getTestResultStatus());
            showTestCaseReportInfo.put("type","testCase");
            showReports.add(showTestCaseReportInfo);
        }


        return new Response<>(200,showReports,"查询成功");

    }
}
