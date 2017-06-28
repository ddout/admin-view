package com.adminview.ims.dao.security;

import java.util.List;
import java.util.Map;

public interface ResourceMapper {

    /**
     * 分页查询
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @return
     */
    public List<Map<String, Object>> listType(Map<String, Object> map);

    /**
     * 分页查询 总条数
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @param map
     * @return
     */
    public int listTypeCount(Map<String, Object> map);

    /**
     * 新增
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @return
     */
    public int addType(Map<String, Object> map);

    /**
     * 删除
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @param ids
     * @return
     */
    public int deleteTypeByIds(Map<String, Object> map);

    /**
     * 修改
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @return
     */
    public int updateType(Map<String, Object> map);

    /**
     * 返回单个类型对象
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @param departid
     * @return
     */
    public Map<String, Object> getTypeById(Map<String, Object> map);
    /**
     * 查看是否有资源项目
     * @param parm
     * @return
     */
    public int hasItems (Map<String, Object> parm);
    
    
    /**
     * 分页查询 查看指定类型的资源项目
     * @param map
     * @return
     */
    public List<Map<String, Object>> listItems(Map<String, Object> map);
    /**
     * 分页查询 资源项目总条数
     * @param map
     * @return
     */
    public int listItemsCount(Map<String, Object> map);
    /**
     * 新增  资源项目
     * @param map
     * @return
     */
    public int addItems(Map<String, Object> map);
    /**
     * 删除 资源项目
     * @param map
     * @return
     */
    public int deleteItemsByIds(Map<String, Object> map);
    /**
     * 修改 资源项目
     * @param map
     * @return
     */
    public int updateItems(Map<String, Object> map);
    /**
     * 返回单个资源项目对象
     * @param map
     * @return
     */
    public Map<String, Object> getItemsById(Map<String, Object> map);
    

}