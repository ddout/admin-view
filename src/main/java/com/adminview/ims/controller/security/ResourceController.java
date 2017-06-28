package com.adminview.ims.controller.security;

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
import com.adminview.ims.service.security.IResourceService;
import com.framework.exception.BizException;

@Controller
@Scope("prototype")
@RequestMapping("/security/resource")
public class ResourceController {
	private static final Logger log = Logger.getLogger(DepartController.class);
	@Autowired
	private IResourceService service;
	/**
	 * 分页查询资源类型
	 * @param parm
	 * @return
	 */
	@RequestMapping("/listType")
    @ResponseBody
	public Result listType(@RequestParam Map<String, Object> parm){
		Page page=service.listType(parm);
		return new Result("查询成功", Result.RESULT_SUCCESS, page.getData(), page.getRowsCount());
	}
	/**
	 * 增加资源类型
	 * @param parm
	 * @return
	 */
	@RequestMapping("/addType")
    @ResponseBody
	public Result addType(@RequestParam Map<String, Object> parm){
		Result rs=new Result();
		try {
			service.addType(parm);
		} catch (BizException ex) {
			log.debug(ex);
		    ex.printStackTrace();
		    rs = Result.gerErrorResult(ex.getMessage());
		}catch(Exception e){
			log.error(e);
		    e.printStackTrace();
		    rs = Result.gerErrorResult("");
		}
		return rs;
	}
	/**
	 * 删除资源类型
	 * @param parm
	 * @return
	 */
	@RequestMapping("/deleteType")
    @ResponseBody
	public Result delete(@RequestParam Map<String, Object> parm){
		Result rs=new Result();
		try {
			int n=service.deleteType(parm);
			rs.setMsg("成功删除" + n + "条");
		}catch(BizException ex){
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
	 * 修改资源类型
	 * @param parm
	 * @return
	 */
	@RequestMapping("/updateType")
    @ResponseBody
    public Result updateType(@RequestParam Map<String, Object> parm){
		Result rs = new Result();
		try {
		    service.updateType(parm);
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
	 * 获取单条资源类型记录
	 * @param parm
	 * @return
	 */
	@RequestMapping("/getTypeById")
    @ResponseBody
    public Result getTypeById(@RequestParam Map<String, Object> parm){
		Result rs = new Result();
		Map<String, Object> map = service.getTypeById(parm);
		rs.setRows(map);
		return rs;
	}
	/**
	 * 分页查询资源项目
	 * @param parm
	 * @return
	 */
	@RequestMapping("/listItems")
    @ResponseBody
	public Result listItems(@RequestParam Map<String, Object> parm){
		Page page=service.listItems(parm);
		return new Result("查询成功", Result.RESULT_SUCCESS, page.getData(), page.getRowsCount());
	}
	/**
	 * 增加资源项目
	 * @param parm
	 * @return
	 */
	@RequestMapping("/addItems")
    @ResponseBody
	public Result addItems(@RequestParam Map<String, Object> parm){
		Result rs=new Result();
		try {
			service.addItems(parm);
		} catch (BizException ex) {
			log.debug(ex);
		    ex.printStackTrace();
		    rs = Result.gerErrorResult(ex.getMessage());
		}catch(Exception e){
			log.error(e);
		    e.printStackTrace();
		    rs = Result.gerErrorResult("");
		}
		return rs;
	}
	/**
	 * 删除资源类型
	 * @param parm
	 * @return
	 */
	@RequestMapping("/deleteItems")
    @ResponseBody
	public Result delItems(@RequestParam Map<String, Object> parm){
		Result rs=new Result();
		try {
			int n=service.deleteItems(parm);
			rs.setMsg("成功删除" + n + "条");
		}catch(BizException ex){
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
	 * 修改资源项目
	 * @param parm
	 * @return
	 */
	@RequestMapping("/updateItems")
    @ResponseBody
    public Result updateItems(@RequestParam Map<String, Object> parm){
		Result rs = new Result();
		try {
		    service.updateItems(parm);
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
	 * 获取单条资源项目记录
	 * @param parm
	 * @return
	 */
	@RequestMapping("/getItemsById")
    @ResponseBody
    public Result getItemsById(@RequestParam Map<String, Object> parm){
		Result rs = new Result();
		Map<String, Object> map = service.getItemsById(parm);
		rs.setRows(map);
		return rs;
	}
	
}
