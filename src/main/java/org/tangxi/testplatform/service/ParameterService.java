package org.tangxi.testplatform.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.tangxi.testplatform.common.Response;
import org.tangxi.testplatform.common.exception.ParamDuplicateException;
import org.tangxi.testplatform.common.exception.UnexpectedParamException;
import org.tangxi.testplatform.controller.ParameterController;
import org.tangxi.testplatform.mapper.ParameterMapper;
import org.tangxi.testplatform.model.parameter.Parameter;
import org.tangxi.testplatform.model.parameter.ParameterType;
import org.tangxi.testplatform.model.parameter.ParameterWrapper;

import java.util.List;
import java.util.Map;

@Service
public class ParameterService {
    private static final Logger LOG = LoggerFactory.getLogger(ParameterController.class);

    @Autowired
    ParameterMapper parameterMapper;

    /**
     * 新增参数
     *
     * @param parameterWrapper
     * @return
     */
    @Transactional
    public Response<String> createParam(ParameterWrapper parameterWrapper) {
        try {
            ParameterType type = parameterWrapper.getType();
            Parameter parameter = parameterWrapper.getParameter();
            switch (type) {
                case SQL:
                    parameterMapper.createParam(parameterWrapper);
                    parameter.setParamId(parameterWrapper.getId());
                    parameterMapper.createSqlParam(parameter);
                    break;
            }
            return new Response<>(200,null,"参数创建成功");
        } catch (DuplicateKeyException e) {
            throw new ParamDuplicateException(parameterWrapper.getName());
        } catch (Exception e) {
            throw new UnexpectedParamException(e);
        }
    }

    /**
     * 更新参数
     * @param parameterWrapper
     * @return
     */
    public Response<String> updateParam(@RequestBody ParameterWrapper parameterWrapper){
        try {
            ParameterType type = parameterWrapper.getType();
            Parameter parameter = parameterWrapper.getParameter();
            switch (type) {
                case SQL:
                    parameterMapper.updateParam(parameterWrapper);
                    parameterMapper.updateSqlParam(parameter);
                    break;
            }
            return new Response<>(200,null,"参数更新成功");
        } catch (DuplicateKeyException e) {
            throw new ParamDuplicateException(parameterWrapper.getName());
        } catch (Exception e) {
            throw new UnexpectedParamException(e);
        }
    }

    /**
     * 根据id获取参数
     * @param id
     * @return
     */
    public Response<ParameterWrapper> getParamById(@PathVariable  int id) {
        try{
            ParameterWrapper parameterWrapper = parameterMapper.getParamById(id);
            return new Response<>(200,parameterWrapper,"查询成功");
        }catch (Exception e){
            throw new UnexpectedParamException(e);
        }

    }

    @GetMapping("/query")
    public Response<PageInfo<ParameterWrapper>> getParams(Map<String, Object> params) {
        try{
            int pageNum = (int) params.get("pageNum");
            int pageSize = (int) params.get("pageSize");
            PageHelper.startPage(pageNum, pageSize);
            List<ParameterWrapper> parameterWrappers = parameterMapper.getParamsByFields(params);
            PageInfo<ParameterWrapper> parameters = new PageInfo<>(parameterWrappers);
            return new Response<>(200,parameters,"查询成功");
        }catch (Exception e){
            throw new UnexpectedParamException(e);
        }
    }
}
