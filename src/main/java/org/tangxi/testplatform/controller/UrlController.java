package org.tangxi.testplatform.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tangxi.testplatform.common.Response;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.model.Url;
import org.tangxi.testplatform.service.UrlService;

import java.util.Map;


@RestController
@RequestMapping("/url")
public class UrlController {
    private static final Logger LOG = LoggerFactory.getLogger(UrlController.class);

    @Autowired
    UrlService urlService;

    /**
     * 新增基础url
     * @param url
     * @return
     */
    @CrossOrigin
    @PostMapping(value="/create", produces = "application/json", consumes = "application/json")
    public Response<?> createUrl(@RequestBody Url url){
        LOG.info("传入的参数为：{}", JacksonUtil.toJson(url));
        return urlService.createUrl(url);

    }

    /**
     * 更新基础url
     * @param url
     * @return
     */
    @CrossOrigin
    @PutMapping(value="/update",produces = "application/json", consumes = "application/json")
    public Response<?> updateUrl(@RequestBody Url url){
        LOG.info("传入的参数为：{}",JacksonUtil.toJson(url));
        return urlService.updateUrl(url);
    }

    /**
     * 根据id获取url
     * @param id
     * @return
     */
    @CrossOrigin
    @GetMapping("/query/{id}")
    public Response<?> getUrlById(@PathVariable int id){
        LOG.info("传入的参数为：{}",id);
        return urlService.getUrlById(id);
    }

    /**
     * 获取所有的url
     * @return
     */
    @CrossOrigin
    @GetMapping("/query/all")
    public Response<?> getAllUrls(){
        return urlService.getAllUrls();
    }

    /**
     * 根据id删除url
     * @param id
     * @return
     */
    @CrossOrigin
    @DeleteMapping("/delete/{id}")
    public Response<?> deleteUrlById(@PathVariable int id){
        LOG.info("传入的参数为：{}",id);
        return urlService.deleteUrlById(id);
    }

    /**
     * 根据url或者descs进行查询
     * @param searchKey
     * @return
     */
//    @CrossOrigin
//    @GetMapping("/query/")
//    public Response<?> getUrlsByUrlOrdescs(@RequestParam String searchKey){
//        LOG.info("传入参数params: {}", JacksonUtil.toJson(searchKey));
//        return urlService.getUrlsByUrlOrdescs(searchKey);
//    }

    /**
     * 根据模块id、url或者descs进行分页查询
     * @param params
     * @return
     */
    @CrossOrigin
    @GetMapping("/query/")
    public Response<?> getUrlsByParams(@RequestParam Map<String,Object> params){
        LOG.info("传入参数params:{}",JacksonUtil.toJson(params));
        return urlService.getUrlsByParams(params);
    }
}
