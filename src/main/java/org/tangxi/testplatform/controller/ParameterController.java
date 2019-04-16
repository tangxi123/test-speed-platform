package org.tangxi.testplatform.controller;

import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tangxi.testplatform.common.Response;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.model.parameter.ParameterWrapper;
import org.tangxi.testplatform.service.ParameterService;

import java.util.Map;

@RestController
@RequestMapping("/paramters")
public class ParameterController {
    private static final Logger LOG = LoggerFactory.getLogger(ParameterController.class);

    @Autowired
    ParameterService parameterService;

    /**
     * 新增参数
     *
     * @param parameterWrapper
     * @return
     */
    @PostMapping("/create")
    public Response<String> createParam(@RequestBody ParameterWrapper parameterWrapper) {
        LOG.info("请求的参数为：{}", parameterWrapper);
        return parameterService.createParam(parameterWrapper);
    }

    /**
     * 更新参数
     *
     * @param parameterWrapper
     * @return
     */
    @PutMapping("/update")
    public Response<String> updateParam(@RequestBody ParameterWrapper parameterWrapper) {
        LOG.info("请求的参数为：{}", parameterWrapper);
        return parameterService.updateParam(parameterWrapper);
    }

    /**
     * 根据id获取参数
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Response<ParameterWrapper> getParamById(@PathVariable int id) {
        LOG.info("请求的参数未：{}", id);
        return parameterService.getParamById(id);
    }

    /**
     * 根据参数名字或描述分页获取参数列表
     *
     * @param params
     * @return
     */
    @GetMapping("/query")
    public Response<PageInfo<ParameterWrapper>> getParams(@RequestBody Map<String, Object> params) {
        LOG.info(JacksonUtil.toJson(params));
        return parameterService.getParams(params);
    }

}
