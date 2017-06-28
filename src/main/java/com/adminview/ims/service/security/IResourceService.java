package com.adminview.ims.service.security;

import java.util.Map;

import com.adminview.ims.model.Page;
import com.framework.exception.BizException;

public interface IResourceService {

    /**
     * 查询列表
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @return
     */
    public Page listType(Map<String, Object> parm);

    /**
     * 获取单条
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @param departid
     * @return
     */
    public Map<String, Object> getTypeById(Map<String, Object> parm);

    /**
     * 增加
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @return
     * @throws BizException
     */
    public void addType(Map<String, Object> parm);

    /**
     * 删除
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @param ids
     * @return
     */
    public int deleteType(Map<String, Object> parm);

    /**
     * 修改
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @return
     * @throws BizException
     */
    public void updateType(Map<String, Object> parm);
    /**
     * 查询列表(资源项目)
     * @param parm
     * @return
     */
    public Page listItems(Map<String, Object> parm);
    /**
     * 获取单条(资源项目)
     * @param parm
     * @return
     */
    public Map<String, Object> getItemsById(Map<String, Object> parm);
    /**
     * 增加（资源项目）
     * @param parm
     */
    public void addItems(Map<String, Object> parm);
    /**
     * 删除（资源项目）
     * @param parm
     * @return
     */
    public int deleteItems(Map<String, Object> parm);
    /**
     * 修改（资源项目）
     * @param parm
     */
    public void updateItems(Map<String, Object> parm);

}
