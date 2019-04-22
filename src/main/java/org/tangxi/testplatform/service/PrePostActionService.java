package org.tangxi.testplatform.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.tangxi.testplatform.common.Response;
import org.tangxi.testplatform.common.exception.ActionDuplicateException;
import org.tangxi.testplatform.common.exception.UnexpectedActionException;
import org.tangxi.testplatform.mapper.PrePostActionMapper;
import org.tangxi.testplatform.mapper.TestCaseMapper;
import org.tangxi.testplatform.model.prePostAction.PrePostAction;
import org.tangxi.testplatform.model.prePostAction.PrePostActionType;
import org.tangxi.testplatform.model.prePostAction.PrePostActionWrapper;

import java.util.List;
import java.util.Map;

@Service
public class PrePostActionService {
    private static final Logger LOG = LoggerFactory.getLogger(PrePostActionService.class);

    @Autowired
    PrePostActionMapper actionMapper;

    @Autowired
    TestCaseMapper testCaseMapper;

    /**
     * 创建前后置动作
     *
     * @param actionWrapper
     * @return
     */
    @Transactional
    public Response<String> createAction(@RequestBody PrePostActionWrapper actionWrapper) {
        LOG.info("请求的参数为：{}", actionWrapper);
        try {
            PrePostActionType type = actionWrapper.getActionType();
            PrePostAction action = actionWrapper.getAction();
            switch (type) {
                case SQL:
                    actionMapper.createAction(actionWrapper);
                    action.setActionId(actionWrapper.getId());
                    actionMapper.createSqlAction(action);
                    break;
            }
            return new Response<String>(200, null, "前后置动作创建成功");
        } catch (DuplicateKeyException e) {
            throw new ActionDuplicateException(actionWrapper.getName());
        } catch (Exception e) {
            throw new UnexpectedActionException(e);
        }
    }

    /**
     * 更新前后置动作actionWrapper
     *
     * @param actionWrapper
     * @return
     */
    public Response<String> updateAction(@RequestBody PrePostActionWrapper actionWrapper) {
        LOG.info("请求的参数为：{}", actionWrapper);
        try {
            PrePostActionType type = actionWrapper.getActionType();
            PrePostAction action = actionWrapper.getAction();
            switch (type) {
                case SQL:
                    actionMapper.updateAction(actionWrapper);
                    action.setActionId(actionWrapper.getId());
                    actionMapper.updateSqlAction(action);
                    break;
            }
            return new Response<String>(200, null, "前后置动作更新成功");
        } catch (DuplicateKeyException e) {
            throw new ActionDuplicateException(actionWrapper.getName());
        } catch (Exception e) {
            throw new UnexpectedActionException(e);
        }
    }

    /**
     * 根据id获取前后置动作
     *
     * @param id
     * @return
     */
    public Response<PrePostActionWrapper> getActionById(int id) {
        try {
            PrePostActionWrapper actionWrapper = actionMapper.getActionById(id);
            LOG.info("查询到的前后置动作为{}", actionWrapper);
            return new Response<>(200, actionWrapper, "获取测试用例成功");
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnexpectedActionException(e);
        }
    }

    /**
     * 根据动作名称、描述分页查询前后置动作
     *
     * @param params
     * @return
     */
    public Response<PageInfo<PrePostActionWrapper>> getActions(@RequestBody Map<String, Object> params) {
        try{
            int pageNum = (int) params.get("pageNum");
            int pageSize = (int) params.get("pageSize");
            PageHelper.startPage(pageNum, pageSize);
            List<PrePostActionWrapper> actionWrappers = actionMapper.getActionsByFields(params);
            PageInfo<PrePostActionWrapper> actions = new PageInfo<>(actionWrappers);
            return new Response<>(200,actions,"查询成功");
        }catch (Exception e){
            throw new UnexpectedActionException(e);
        }
    }

    /**
     * 根据id删除前后置动作
     * @param id
     * @return
     */
    public Response<String> deleteActionById(int id){
        try{
            PrePostActionWrapper actionWrapper = actionMapper.getActionById(id);
            String name = actionWrapper.getName();
            int actionCount = testCaseMapper.getActionCountByActionName('"'+name+'"');
            if(actionCount > 0){
                return new Response<>(400,null,"存在已被使用的前后置动作:"+name+"，删除失败");
            }
            int delActionCount = actionMapper.deleteActionById(id);
            if(delActionCount == 1){
                return new Response<>(200,null,"删除成功");
            }
            return new Response<>(500,null,"服务器错误");
        }catch (Exception e){
            throw new UnexpectedActionException(e);
        }
    }
}
