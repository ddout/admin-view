package com.adminview.ims.dao.security;

import java.util.List;
import java.util.Map;

public interface DepartMapper {

    /**
     * 分页查询
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @return
     */
    public List<Map<String, Object>> list(Map<String, Object> map);

    /**
     * 分页查询 总条数
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @param map
     * @return
     */
    public int listCount(Map<String, Object> map);

    /**
     * 新增
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @return
     */
    public int add(Map<String, Object> map);

    /**
     * 删除
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @param ids
     * @return
     */
    public int deleteByIds(Map<String, Object> map);

    /**
     * 修改
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @return
     */
    public int update(Map<String, Object> map);

    /**
     * 返回单个部门对象
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @param departid
     * @return
     */
    public Map<String, Object> getById(Map<String, Object> map);

    /** 获取tree
     * @author pjf
     * @Date 2015年10月29日
     * @param parm
     * @return
     */
    public List<Map<String, Object>> getTree(Map<String, Object> parm);
    /**
     * 获取所属部门tree
     * @param parm
     * @return
     */
    public List<Map<String, Object>> getTree1(Map<String, Object> parm);

    /**
     *  查看是否存在子部门
     * @author pjf
     * @Date 2015年11月2日
     * @param parm
     */
    public int hasChild(Map<String, Object> parm);
    /**
     *  查看是否用户
     * @author pjf
     * @Date 2015年11月2日
     * @param parm
     */
    public int hasUser(Map<String, Object> parm);
    /**
     * 查看是否配置了数据权限 
     * @param parm
     * @return
     */
    public int hasPrivilege(Map<String, Object> parm);

}