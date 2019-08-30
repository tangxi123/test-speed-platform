package org.tangxi.testplatform.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tangxi.testplatform.model.Url;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface UrlMapper {

    //创建基础url
    int createUrl(Url url);

    //更新基础url
    int updateUrl(Url url);

    //根据id获取基础url
    Url getUrlById(@Param("id") int id);

    //获取所有的url
    List<Url> getAllUrls();

    //根据id删除url
    int deleteUrlById(@Param("id") int id);

    //根据模块id删除Url
    int deleteUrlByModuleId(@Param("moduleId") int moduleId);

    //根据url或者descs关键字查询
    List<Url> getUrlsByUrlOrdescs(@Param("searchKey")String searchKey);

    //根据moduleId、url、descs进行分页查询
    List<Url> getUrlsByParams(Map<String,Object> params);
}
