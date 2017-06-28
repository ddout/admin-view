package com.adminview.ims.service.security.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adminview.ims.dao.security.ResourceMapper;
import com.adminview.ims.model.Page;
import com.adminview.ims.service.security.IResourceService;
import com.framework.exception.BizException;

@Service
public class ResourceServiceImpl implements IResourceService {

    @Autowired
    private ResourceMapper mapper;
    // 资源类型操作》开始

    @Override
    public Page listType(Map<String, Object> parm) {
	Object start = parm.get("start");
	Object limit = parm.get("limit");
	Page page = new Page(start, limit);
	parm.put("start", page.getStart());
	parm.put("end", page.getEnd());
	int size = mapper.listTypeCount(parm);
	page.setRowsCount(size);
	if (size > 0) {
	    List<Map<String, Object>> list = mapper.listType(parm);
	    page.setData(list);
	}
	return page;
    }

    @Override
    public Map<String, Object> getTypeById(Map<String, Object> parm) {
	return mapper.getTypeById(parm);
    }

    @Override
    public void addType(Map<String, Object> parm) {
	if (parm == null) {
	    throw new BizException("所添加的资源类型为空");
	}
	mapper.addType(parm);
    }

    @Override
    public int deleteType(Map<String, Object> parm) {
	Object obj = parm.get("ids");
	if (obj == null || obj == "") {
	    throw new BizException("选择为空");
	}
	try {
	    String ids[] = obj.toString().split(",");
	    parm.put("array", ids);
	} catch (Exception e) {
	    throw new BizException("删除失败");
	}
	int itm = mapper.hasItems(parm);
	if (itm > 0) {
	    throw new BizException("有子资源项目的类型不能删除");
	}
	int n = mapper.deleteTypeByIds(parm);
	return n;
    }

    @Override
    public void updateType(Map<String, Object> parm) {
	if (parm == null || null == parm.get("id") || "".equals(parm.get("id").toString())) {
	    throw new BizException("所要修改的信息不合法或为空");
	}
	mapper.updateType(parm);
    }
    // 资源类型操作》结束

    // 资源项目操作》开始
    @Override
    public Page listItems(Map<String, Object> parm) {
	Object start = parm.get("start");
	Object limit = parm.get("limit");
	Page page = new Page(start, limit);
	parm.put("start", page.getStart());
	parm.put("end", page.getEnd());
	int size = mapper.listItemsCount(parm);
	page.setRowsCount(size);
	if (size > 0) {
	    List<Map<String, Object>> list = mapper.listItems(parm);
	    page.setData(list);
	}
	return page;
    }

    @Override
    public Map<String, Object> getItemsById(Map<String, Object> parm) {
	return mapper.getItemsById(parm);
    }

    @Override
    public void addItems(Map<String, Object> parm) {
	if (parm == null) {
	    throw new BizException("所添加的资源类型为空");
	}
	try {
	    Object obj = parm.get("byorder");
	    Integer.parseInt(obj.toString());
	} catch (NumberFormatException e) {
	    e.printStackTrace();
	    throw new BizException("排序必须是数字", e);
	}
	mapper.addItems(parm);

    }

    @Override
    public int deleteItems(Map<String, Object> parm) {
	Object obj = parm.get("ids");
	if (obj == null || obj == "") {
	    throw new BizException("选择为空");
	}
	try {
	    String ids[] = obj.toString().split(",");
	    parm.put("array", ids);
	} catch (Exception e) {
	    throw new BizException("删除失败");
	}
	int n = mapper.deleteItemsByIds(parm);
	return n;
    }

    @Override
    public void updateItems(Map<String, Object> parm) {
	if (parm == null || null == parm.get("id") || "".equals(parm.get("id").toString())) {
	    throw new BizException("所要修改资源项目信息信息不合法或为空");
	}
	mapper.updateItems(parm);

    }
    // 资源项目操作结束

}
