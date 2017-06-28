package com.adminview.ims.service.security.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adminview.ims.dao.security.DepartMapper;
import com.adminview.ims.model.Page;
import com.adminview.ims.service.security.IDepartService;
import com.framework.exception.BizException;

@Service
public class DepartServiceImpl implements IDepartService {
    @Autowired
    private DepartMapper mapper;

    @Override
    public Page list(Map<String, Object> parm) {
	Object start = parm.get("start");
	Object limit = parm.get("limit");
	Page page = new Page(start, limit);
	parm.put("start", page.getStart());
	parm.put("end", page.getEnd());
	int size = mapper.listCount(parm);
	page.setRowsCount(size);
	if (size > 0) {
	    List<Map<String, Object>> list = mapper.list(parm);
	    page.setData(list);
	}
	return page;
    }

    @Override
    public void add(Map<String, Object> parm) {
	if (parm == null) {
	    throw new BizException("所添加的部门为空");
	}
	try {
	    Object ob = parm.get("orderby");
	    Integer.parseInt(ob.toString());
	} catch (NumberFormatException e) {
	    e.printStackTrace();
	    throw new BizException("排序必须是数字", e);
	}
	Object pid = parm.get("pid");
	if (null == pid || "".equals(pid.toString())) {
	    parm.put("pid", "-1");
	}
	mapper.add(parm);
    }

    /**
     * 1、有子部门不能删 2、部门中有人不能删
     */
    @Override
    public int delete(Map<String, Object> parm) {
	Object obj = parm.get("ids");
	if (obj == null||obj=="") {
	    throw new BizException("选择为空");
	}
	try {
	    String ids[] = obj.toString().split(",");
	    parm.put("array", ids);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new BizException("删除失败");
	}
	// 1、有子部门不能删
	int dps = mapper.hasChild(parm);
	if (dps > 0) {
	    throw new BizException("有子部门的部门不能删除");
	}
	// 2、部门中有人不能删
	int users = mapper.hasUser(parm);
	if (users > 0) {
	    throw new BizException("部门中有用户的不能删除");
	}

	int n = mapper.deleteByIds(parm);
	return n;
    }

    @Override
    public void update(Map<String, Object> parm) {
	if (parm == null || null == parm.get("id") || "".equals(parm.get("id").toString())) {
	    throw new BizException("所要修改的部门信息不合法或为空");
	}
	try {
	    Object ob = parm.get("orderby");
	    Integer.parseInt(ob.toString());
	} catch (NumberFormatException e) {
	    e.printStackTrace();
	    throw new BizException("排序必须是数字", e);
	}
	Object pid = parm.get("pid");
	if (null == pid || "".equals(pid.toString())) {
	    parm.put("pid", "-1");
	}
	mapper.update(parm);
    }

    @Override
    public Map<String, Object> getById(Map<String, Object> parm) {
	return mapper.getById(parm);
    }

    @Override
    public List<Map<String, Object>> getTree(Map<String, Object> parm) {
    Object obj = parm.get("type");
    List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
    if(obj.equals("tree")){
    list = mapper.getTree(parm);
    }else if(obj.equals("ntree")){
    list = mapper.getTree1(parm);
    }
	return list;
    }

}
