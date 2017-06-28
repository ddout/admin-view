package com.adminview.ims.service.security.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adminview.ims.dao.security.LoginMapper;
import com.adminview.ims.service.security.ILoginService;
import com.adminview.ims.util.ParamsUtil;
import com.framework.exception.BizException;
import com.framework.util.MD5;
import com.framework.util.ResourceUtil;

@Service
public class LoginServiceImpl implements ILoginService {
    public static final String DEFAULT_PASSWORD = ResourceUtil.getDefaultPassword();
    @Autowired
    private LoginMapper mapper;

    @Override
    public Map<String, Object> login(Map<String, Object> parm) {
	Map<String, Object> map = new HashMap<String, Object>();
	// 验证用户口令
	
	Object username = parm.get("username");
	Object password = parm.get("password");
	if (null == username || null == password) {
	    throw new BizException("用户名或密码必填");
	}
	String md5Psw = buildPassword(username.toString(), password.toString());
	parm.put("md5Psw", md5Psw);
	final Map<String, Object> user = mapper.login(parm);
	if (null == user) {
	    throw new BizException("用户名或密码不正确");
	}
	// 判断该登录用户是否被禁用
	if(!(null == user.get("ENABLED"))){ //因为admin 帐号存在未配置该ENABLED字段,会出现null情况
		if("-1".equals(user.get("ENABLED").toString()) ){
			throw new BizException("该登录用户被禁用，请联系管理员");
		}
	}
	// 获取功能权限
	List<Map<String, Object>> rolePrivilege = null;
	if("admin".equals(username)){
	    rolePrivilege = mapper.queryAllRolePrivilege();
	} else {
	    rolePrivilege = mapper.queryRolePrivilege(user);
	}
	
	
	//
	Set<String> rolePrivilegeSet = new HashSet<String>();
	for (Map<String, Object> pmap : rolePrivilege) {
	    if (null != pmap.get("URL") && !"".equals(pmap.get("URL").toString().trim())) {
		rolePrivilegeSet.add(pmap.get("URL").toString().trim());
	    }
	}
	//
	map.put("user", user);
	map.put("rolePrivilege", rolePrivilege);
	map.put("rolePrivilegeSet", rolePrivilegeSet);
	//判断用户的密码是否为系统初始化密码
	final String defaultPsw = buildPassword(user.get("USERNAME").toString(),
		LoginServiceImpl.DEFAULT_PASSWORD);
	//user
	@SuppressWarnings("serial")
	int flag = mapper.queryDefaultPasswordFlag(new HashMap<String, Object>(){{
	    put("id", ParamsUtil.getString4Map(user, "ID"));
	    put("newPsw", defaultPsw);
	}});
	map.put("defaultPasswordFlag", flag);
	return map;
    }

    public static final String buildPassword(String username, String password) {
	String md5Psw = MD5.MD5Encode(password + username).toLowerCase();
	return md5Psw;
    }

    public static void main(String[] args) {
	System.out.println(buildPassword("admin", "123456").toLowerCase());
	String a = "[0-9a-zA-Z]+";
	System.out.println("1a1X2j3k1j23lj123j@".matches(a));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void updatePassword(Map<String, Object> parm) {
	Object oldPassword = parm.get("oldPassword");
	Object newPassword = parm.get("newPassword");
	Object newPassword2 = parm.get("newPassword2");
	if(null == oldPassword || null == newPassword || null == newPassword2){
	    throw new BizException("密码信息不能为空!" );
	}
	Map<String, Object> loginUser = (Map<String, Object>) parm.get("loginUser");
	String username=(String) ((Map<String, Object>) loginUser.get("user")).get("USERNAME");
	String id=(String) ((Map<String, Object>) loginUser.get("user")).get("ID");
	
	
	parm.put("id", id);
	String oldPsw = mapper.getOldPassword(parm);
	String oldpwd=LoginServiceImpl.buildPassword(username, oldPassword.toString());
	if (null == oldPsw || !oldPsw.equals(oldpwd)) {
	    throw new BizException("原始密码不正确" );
	}
	if (!newPassword.equals(newPassword2)) {
	    throw new BizException("两次密码不一致");
	}
	if (newPassword.toString().trim().length()<6 || newPassword.toString().trim().length()>40) {
	    throw new BizException("密码长度只能在6个至40个字符之间");
	}
	if(newPassword.toString().trim().matches("[0-9a-zA-Z]+")){
	    throw new BizException("您的新密码过于简单,为了账户安全考虑,请增加密码的复杂度!");
	}
	String newPsw = LoginServiceImpl.buildPassword(username, newPassword.toString());
	parm.put("newPsw", newPsw);

	mapper.updatePassword(parm);
    }

    @Override
    public void resetPassword(Map<String, Object> parm) {

	Map<String, Object> user = mapper.getUser(parm);
	if (null == user) {
	    throw new BizException("用户不存在");
	}

	String newPsw = buildPassword(user.get("USERNAME").toString(),
		LoginServiceImpl.DEFAULT_PASSWORD);
	parm.put("newPsw", newPsw);

	mapper.updatePassword(parm);
    }
}
