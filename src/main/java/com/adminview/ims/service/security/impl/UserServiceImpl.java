package com.adminview.ims.service.security.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adminview.ims.dao.security.UserMapper;
import com.adminview.ims.model.Page;
import com.adminview.ims.service.security.IUserService;
import com.framework.exception.BizException;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper mapper;

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
    	    throw new BizException("所添加的用户为空");
    	}
    	parm.put("password",
    		LoginServiceImpl.buildPassword(parm.get("username").toString(), LoginServiceImpl.DEFAULT_PASSWORD));
    	parm.put("enabled", 0);
    	mapper.add(parm);
    }

    @Override
    public int delete(Map<String, Object> parm) {
    	Object obj = parm.get("ids");
    	if (obj == null || obj == "") {
    	    throw new BizException("选择为空");
    	}
    	try {
    	    String ids[] = obj.toString().split(",");
    	    parm.put("array", ids);
    	} catch (Exception e) {
    	    e.printStackTrace();
    	    throw new BizException("禁用失败");
    	}
    	parm.put("enabled", "-1");
    	int n = mapper.deleteByIds(parm);
    	return n;
    }

    @Override
    public void update(Map<String, Object> parm) {
    	if (parm == null || null == parm.get("id") || "".equals(parm.get("id").toString())) {
    	    throw new BizException("所要修改的用户信息不合法或为空");
    	}
    	mapper.update(parm);
    }

    @Override
    public Map<String, Object> getById(Map<String, Object> parm) {

	return mapper.getById(parm);
    }

    @Override
    public void saveRole(Map<String, Object> parm) {
	if (parm == null || null == parm.get("userId") || "".equals(parm.get("userId").toString())) {
	    throw new BizException("所要设置的用户信息不合法或为空");
	}
	Object obj = parm.get("roleIds");
	if (obj == null) {
	    throw new BizException("选择为空");
	}
	String ids[] = obj.toString().split(",");
	parm.put("roleIds", ids);
	mapper.deleteUserRole(parm);
	mapper.setRole(parm);
    }

    @Override
    public List<Map<String, Object>> getAllRoleIdById(Map<String, Object> parm) {
	return mapper.getAllRoleIdById(parm);
    }
    
    @Override
	public int invokeByStart(Map<String, Object> parm) {
		Object obj = parm.get("ids");
		if (obj == null || obj == "") {
		    throw new BizException("选择为空");
		}
		try {
		    String ids[] = obj.toString().split(",");
		    parm.put("array", ids);
		} catch (Exception e) {
		    e.printStackTrace();
		    throw new BizException("启用失败");
		}
		parm.put("enabled", "0");
		int n = mapper.startByIds(parm);
		return n;
	}

	@Override
	public List<Map<String, Object>> saveUserDepartTree(Map<String, Object> parm) {
		List<Map<String, Object>> list = mapper.setUserDepartTree(parm);
		return list;
	}

	@Override
	public void saveDepart(Map<String, Object> parm) {
		if (parm == null || null == parm.get("userId") || "".equals(parm.get("userId").toString())) {
		    throw new BizException("所要设置的用户信息不合法或为空");
		}
		Object obj =parm.get("departIds");
		if(obj==null){
			throw new BizException("选择为空");
		}
		String ids[]=obj.toString().split(",");
		parm.put("array", ids);
		mapper.deleteUserDepart(parm);
		mapper.setDepart(parm);
	}

	
}
