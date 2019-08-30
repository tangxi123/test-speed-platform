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
import org.springframework.web.bind.annotation.RequestParam;
import org.tangxi.testplatform.common.Response;
import org.tangxi.testplatform.common.exception.ParamDuplicateException;
import org.tangxi.testplatform.common.exception.UnexpectedActionException;
import org.tangxi.testplatform.common.exception.UnexpectedParamException;
import org.tangxi.testplatform.controller.ParameterController;
import org.tangxi.testplatform.mapper.ParameterMapper;
import org.tangxi.testplatform.mapper.TestCaseMapper;
import org.tangxi.testplatform.model.parameter.Parameter;
import org.tangxi.testplatform.model.parameter.ParameterType;
import org.tangxi.testplatform.model.parameter.ParameterWrapper;
import org.tangxi.testplatform.model.prePostAction.PrePostActionWrapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ParameterService {
    private static final Logger LOG = LoggerFactory.getLogger(ParameterController.class);

    @Autowired
    ParameterMapper parameterMapper;

    @Autowired
    TestCaseMapper testCaseMapper;

    /**
     * 新增参数
     *
     * @param parameterWrapper
     * @return
     */
    @Transactional
    public Response<String> createParam(ParameterWrapper parameterWrapper) {
        try {
            parameterWrapper.setCreateAt(LocalDateTime.now());
            parameterWrapper.setUpdateAt(parameterWrapper.getCreateAt());
            ParameterType type = parameterWrapper.getType();
            Parameter parameter = parameterWrapper.getParameter();
            switch (type) {
                case SQL:
                    parameterMapper.createParam(parameterWrapper);
                    parameter.setParamId(parameterWrapper.getId());
                    parameterMapper.createSqlParam(parameter);
                    break;
                case TOKEN:
                    parameterMapper.createParam(parameterWrapper);
                    parameter.setParamId(parameterWrapper.getId());
                    parameterMapper.createTokenParam(parameter);
                    break;
                case KEYVALUE:
                    parameterMapper.createParam(parameterWrapper);
                    parameter.setParamId(parameterWrapper.getId());
                    parameterMapper.createKeyValueParam(parameter);
                    break;
            }
            return new Response<>(200, null, "参数创建成功");
        } catch (DuplicateKeyException e) {
            throw new ParamDuplicateException(parameterWrapper.getName());
        } catch (Exception e) {
            throw new UnexpectedParamException(e);
        }
    }

    /**
     * 更新参数
     *
     * @param parameterWrapper
     * @return
     */
    public Response<String> updateParam(@RequestBody ParameterWrapper parameterWrapper) {
        try {
            parameterWrapper.setUpdateAt(LocalDateTime.now());
            int paramId = parameterWrapper.getId();
            ParameterWrapper paramWrapper = parameterMapper.getParamWrapperById(paramId);
            if(paramWrapper.getType() == parameterWrapper.getType()){
                ParameterType type = parameterWrapper.getType();
                Parameter parameter = parameterWrapper.getParameter();
                switch (type) {
                    case SQL:
                        parameterMapper.updateParam(parameterWrapper);
                        parameter.setParamId(parameterWrapper.getId());
                        parameterMapper.updateSqlParam(parameter);
                        break;
                    case TOKEN:
                        parameterMapper.updateParam(parameterWrapper);
                        parameter.setParamId(parameterWrapper.getId());
                        parameterMapper.updateTokenParam(parameter);
                        break;
                    case KEYVALUE:
                        parameterMapper.updateParam(parameterWrapper);
                        parameter.setParamId(parameterWrapper.getId());
                        parameterMapper.updateKeyValueParam(parameter);
                        break;
                }
            }else{
                parameterWrapper.setCreateAt(LocalDateTime.now());
                parameterMapper.deleteParamById(paramId);
                ParameterType type = parameterWrapper.getType();
                Parameter parameter = parameterWrapper.getParameter();
                switch (type) {
                    case SQL:
                        parameterMapper.createParam(parameterWrapper);
                        parameter.setParamId(parameterWrapper.getId());
                        parameterMapper.createSqlParam(parameter);
                        break;
                    case TOKEN:
                        parameterMapper.createParam(parameterWrapper);
                        parameter.setParamId(parameterWrapper.getId());
                        parameterMapper.createTokenParam(parameter);
                        break;
                    case KEYVALUE:
                        parameterMapper.createParam(parameterWrapper);
                        parameter.setParamId(parameterWrapper.getId());
                        parameterMapper.createKeyValueParam(parameter);
                        break;
                }
            }

            return new Response<>(200, null, "参数更新成功");
        } catch (DuplicateKeyException e) {
            throw new ParamDuplicateException(parameterWrapper.getName());
        } catch (Exception e) {
            throw new UnexpectedParamException(e);
        }
    }

//    /**
//     * 查询所有的parameter主表数据
//     * @return
//     */
//    public Response<?> getAllParameters(){
//        try{
//            List<ParameterWrapper> parameterWrappers = parameterMapper.getAllParameters();
//            return new Response<>(200,parameterWrappers,"查询成功");
//        }catch (Exception e){
//            throw new UnexpectedParamException(e);
//        }
//    }

    /**
     * 根据参数名称、描述分页查询前后置动作不包括明细
     * @return
     */
    public Response<?> getParamWrappers(@RequestParam Map<String,Object> params){
        try{
            int pageNum = Integer.parseInt((String)params.get("pageNum"));
            int pageSize = Integer.parseInt((String)params.get("pageSize"));
            PageHelper.startPage(pageNum,pageSize);
            List<ParameterWrapper> paramWrappers = parameterMapper.getParamWrappers(params);
            PageInfo<ParameterWrapper> parameters = new PageInfo<>(paramWrappers);
            return new Response<>(200,parameters,"查询成功");
        }catch (Exception e){
            throw new UnexpectedParamException(e);
        }
    }

    /**
     * 根据id获取参数
     *
     * @param id
     * @return
     */
    public Response<ParameterWrapper> getParamById(@PathVariable int id) {
        try {
            ParameterWrapper param = new ParameterWrapper();
            ParameterWrapper paramWrapper = parameterMapper.getParamWrapperById(id);
            ParameterType type = paramWrapper.getType();
            switch (type){
                case SQL:
                    param = parameterMapper.getSqlParamById(id);
                    break;
                case TOKEN:
                    param = parameterMapper.getTokenParamById(id);
                    break;
                case KEYVALUE:
                    param = parameterMapper.getKeyValueParamById(id);
                    break;

            }
            return new Response<>(200, param, "查询成功");
        } catch (Exception e) {
            throw new UnexpectedParamException(e);
        }

    }

    /**
     * 根据参数名字或描述分页获取参数列表
     *
     * @param params
     * @return
     */
    @GetMapping("/query")
    public Response<PageInfo<ParameterWrapper>> getParams(Map<String, Object> params) {
        try {
            int pageNum = (int) params.get("pageNum");
            int pageSize = (int) params.get("pageSize");
            PageHelper.startPage(pageNum, pageSize);
            List<ParameterWrapper> parameterWrappers = parameterMapper.getParamsByFields(params);
            PageInfo<ParameterWrapper> parameters = new PageInfo<>(parameterWrappers);
            return new Response<>(200, parameters, "查询成功");
        } catch (Exception e) {
            throw new UnexpectedParamException(e);
        }
    }

    /**
     * 根据id删除参数
     *
     * @param id
     * @return
     */
    public Response<String> deleteParamById(int id) {
        try{
            ParameterWrapper parameterWrapper = parameterMapper.getParamWrapperById(id);
            String name = parameterWrapper.getName();
            int paramCount = testCaseMapper.getParamCountByParameter("${"+name+"}");
            if(paramCount > 0){
                return new Response<>(400,null,"存在已被使用的参数:"+name+"，删除失败");
            }
            parameterMapper.deleteParamById(id);
            return new Response<>(200,null,"删除成功");
        }catch (Exception e){
            throw new UnexpectedParamException(e);
        }
    }
}
