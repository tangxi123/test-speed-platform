package org.tangxi.testplatform.controller;

import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tangxi.testplatform.common.Response;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.model.prePostAction.PrePostAction;
import org.tangxi.testplatform.model.prePostAction.PrePostActionWrapper;
import org.tangxi.testplatform.service.PrePostActionService;

import java.util.Map;

@RestController
@RequestMapping("actions/")
public class PrePostActionController {
    private static final Logger LOG = LoggerFactory.getLogger(PrePostActionController.class);

    @Autowired
    PrePostActionService prePostActionService;

    /**
     * 创建前后置动作
     *
     * @param actionWrapper
     * @return
     */
    @PostMapping("/create")
    public Response<String> createAction(@RequestBody PrePostActionWrapper actionWrapper) {
        LOG.info("请求的参数为：{}", actionWrapper);
        return prePostActionService.createAction(actionWrapper);
    }

    /**
     * 更新前后置动作
     *
     * @param actionWrapper
     * @return
     */
    @PutMapping("/update")
    public Response<String> updateAction(@RequestBody PrePostActionWrapper actionWrapper) {
        LOG.info("请求的参数为{}", actionWrapper);
        return prePostActionService.updateAction(actionWrapper);
    }

    /**
     * 根据id获取前后置动作
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Response<PrePostActionWrapper> getActionById(@PathVariable int id) {
        LOG.info("请求的参数为：{}", id);
        return prePostActionService.getActionById(id);
    }

    /**
     * 根据动作名称、描述分页查询前后置动作
     *
     * @param params
     * @return
     */
    @GetMapping("/query")
    public Response<PageInfo<PrePostActionWrapper>> getActions(@RequestBody Map<String, Object> params) {
        LOG.info("请求的参数为：{}", JacksonUtil.toJson(params));
        return prePostActionService.getActions(params);
    }

}
