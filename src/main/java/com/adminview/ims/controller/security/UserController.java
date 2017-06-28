package com.adminview.ims.controller.security;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.adminview.ims.model.Page;
import com.adminview.ims.model.Result;
import com.adminview.ims.service.security.IUserService;
import com.framework.exception.BizException;

@Controller
@Scope("prototype")
@RequestMapping("/security/user")
public class UserController {
    private static final Logger log = Logger.getLogger(UserController.class);
    @Autowired
    private IUserService service;

    /**
     * 列表
     * 
     * @author pjf
     * @Date 2015年10月28日
     * @param parm
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> parm) {
	Page page = service.list(parm);
	return new Result("查询成功", Result.RESULT_SUCCESS, page.getData(),
		page.getRowsCount());
    }

    /**
     * 增加
     * 
     * @author pjf
     * @Date 2015年10月28日
     * @param parm
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Result add(@RequestParam Map<String, Object> parm) {
	Result rs = new Result();
	try {
	    service.add(parm);
	} catch (BizException ex) {
	    log.debug(ex);
	    ex.printStackTrace();
	    rs = Result.gerErrorResult(ex.getMessage());
	} catch (Exception e) {
	    log.error(e);
	    e.printStackTrace();
	    rs = Result.gerErrorResult("");
	}
	return rs;
    }

    /**
     * 禁用
     * 
     * @author pjf
     * @Date 2015年10月28日
     * @param parm
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Result delete(@RequestParam Map<String, Object> parm) {
	Result rs = new Result();
	try {
	    int n = service.delete(parm);
	    rs.setMsg("成功" + n + "条");
	} catch (BizException ex) {
	    log.debug(ex);
	    ex.printStackTrace();
	    rs = Result.gerErrorResult(ex.getMessage());
	} catch (Exception e) {
	    log.error(e);
	    e.printStackTrace();
	    rs = Result.gerErrorResult(Result.RESULT_ERROR_MSG);
	}
	return rs;

    }

    /**
     * 修改
     * 
     * @author pjf
     * @Date 2015年10月28日
     * @param parm
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public Result update(@RequestParam Map<String, Object> parm) {
	Result rs = new Result();
	try {
	    service.update(parm);
	} catch (BizException ex) {
	    log.debug(ex);
	    ex.printStackTrace();
	    rs = Result.gerErrorResult(ex.getMessage());
	} catch (Exception e) {
	    log.error(e);
	    e.printStackTrace();
	    rs = Result.gerErrorResult(Result.RESULT_ERROR_MSG);
	}
	return rs;

    }

    /**
     * 获取单条记录
     * 
     * @author pjf
     * @Date 2015年10月28日
     * @param parm
     * @return
     */
    @RequestMapping("/getById")
    @ResponseBody
    public Result getById(@RequestParam Map<String, Object> parm) {
	Result rs = new Result();
	Map<String, Object> map = service.getById(parm);
	rs.setRows(map);
	return rs;

    }
    
    /**
     * 获取所有的角色ID
     * 
     * @author pjf
     * @Date 2015年10月28日
     * @param parm
     * @return
     */
    @RequestMapping("/getAllRoleIdById")
    @ResponseBody
    public Result getAllRoleIdById(@RequestParam Map<String, Object> parm) {
	Result rs = new Result();
	List<Map<String, Object>> map = service.getAllRoleIdById(parm);
	rs.setRows(map);
	return rs;
	
    }
    
    /**
     * 设置用户角色
     * 
     * @author pjf
     * @Date 2015年10月28日
     * @param parm
     * @return
     */
    @RequestMapping("/saveRole")
    @ResponseBody
    public Result setRole(@RequestParam Map<String, Object> parm) {
	Result rs = new Result();
	try {
	    service.saveRole(parm);
	} catch (BizException ex) {
	    log.debug(ex);
	    ex.printStackTrace();
	    rs = Result.gerErrorResult(ex.getMessage());
	} catch (Exception e) {
	    log.error(e);
	    e.printStackTrace();
	    rs = Result.gerErrorResult(Result.RESULT_ERROR_MSG);
	}
	return rs;
	
    }
    
    /**
     * 启用
     * @author java
     * @Date 2016年03月03日
     * @param parm
     * @return
     */
    @RequestMapping("/invokeByStart")
    @ResponseBody
    public Result start(@RequestParam Map<String, Object> parm) {
		Result rs = new Result();
		try {
		    int n = service.invokeByStart(parm);
		    rs.setMsg("成功" + n + "条");
		} catch (BizException ex) {
		    log.debug(ex);
		    ex.printStackTrace();
		    rs = Result.gerErrorResult(ex.getMessage());
		} catch (Exception e) {
		    log.error(e);
		    e.printStackTrace();
		    rs = Result.gerErrorResult(Result.RESULT_ERROR_MSG);
		}
		return rs;
    }
    /**
     * 设置用户所属机构数状图
     * @author java
     * @Date 2016年03月03日
     * @return
     */
 
   /**
    * 设置管理机构
    * @author java
    * @Date 2016年03月03日
    * @return
    */
   @RequestMapping("/saveDepart")
   @ResponseBody
   public Result setDepart(@RequestParam Map<String, Object> parm){
   	Result rs=new Result();
   	try {
			service.saveDepart(parm);
		} catch (BizException ex) {
			 log.debug(ex);
			 ex.printStackTrace();
			 rs = Result.gerErrorResult(ex.getMessage());
		}catch (Exception e) {
			log.error(e);
		    e.printStackTrace();
		    rs = Result.gerErrorResult(Result.RESULT_ERROR_MSG);
		}
   	return rs;
   }
}
