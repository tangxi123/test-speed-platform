package org.tangxi.testplatform.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.tangxi.testplatform.common.Response;
import org.tangxi.testplatform.common.exception.UnexpectedUrlException;
import org.tangxi.testplatform.controller.UrlController;
import org.tangxi.testplatform.mapper.UrlMapper;
import org.tangxi.testplatform.model.Url;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class UrlService {
    private static final Logger LOG = LoggerFactory.getLogger(UrlController.class);

    @Autowired
    UrlMapper urlMapper;

    /**
     * 新增基础url
     * @param url
     * @return
     */
    public Response<?> createUrl(Url url) {
        try{
            url.setCreateAt(LocalDateTime.now());
            url.setUpdateAt(url.getCreateAt());
            urlMapper.createUrl(url);
            return new Response<>(200,null,"创建成功");
        }catch (Exception e){
            throw new UnexpectedUrlException(e);
        }
    }

    /**
     * 更新基础url
     * @param url
     * @return
     */
    public Response<?> updateUrl(Url url){
        try{
            url.setUpdateAt(LocalDateTime.now());
            urlMapper.updateUrl(url);
            return new Response<>(200,null,"更新成功");
        }catch (Exception e){
            throw new UnexpectedUrlException(e);
        }
    }

    /**
     * 根据id获取url
     * @param id
     * @return
     */
    public Response<?> getUrlById(int id){
        try{
            Url url = urlMapper.getUrlById(id);
            return new Response<>(200,url,"查询成功");
        }catch (Exception e){
            throw new UnexpectedUrlException(e);
        }
    }

    /**
     * 获取所有的url
     * @return
     */
    public Response<?> getAllUrls(){
        try {
            List<Url> urls = urlMapper.getAllUrls();
            return new Response<>(200, urls, "查询成功");
        }catch (Exception e){
            throw new UnexpectedUrlException(e);
        }
    }

    /**
     * 根据id删除url
     * @param id
     * @return
     */
    public Response<?> deleteUrlById(int id){
        try{
            urlMapper.deleteUrlById(id);
            return new Response<>(200,null,"删除成功");
        }catch (Exception e){
            throw new UnexpectedUrlException(e);
        }
    }

    /**
     * 根据url或者descs进行查询
     * @param searchKey
     * @return
     */
    public Response<?> getUrlsByUrlOrdescs(String searchKey){
        try{
            List<Url> urls = urlMapper.getUrlsByUrlOrdescs(searchKey);
            return new Response<>(200,urls,"查询成功");
        }catch (Exception e){
            throw new UnexpectedUrlException(e);
        }
    }

    public Response<?> getUrlsByParams(Map<String,Object> params){
        try{
            int pageNum = Integer.parseInt((String)params.get("pageNum"));
            int pageSize = Integer.parseInt((String)params.get("pageSize"));
            PageHelper.startPage(pageNum,pageSize);
            List<Url> urlsByParams = urlMapper.getUrlsByParams(params);
            PageInfo<Url> urls = new PageInfo<>(urlsByParams);
            return new Response<>(200,urls,"查询成功");
        }catch (Exception e){
            throw new UnexpectedUrlException(e);
        }
    }

}
