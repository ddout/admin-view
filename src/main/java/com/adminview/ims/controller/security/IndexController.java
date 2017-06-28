package com.adminview.ims.controller.security;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.adminview.ims.model.Result;
import com.adminview.ims.service.security.IIndexService;

@Controller
@RequestMapping("/security/index")
public class IndexController {
    @Autowired
    private IIndexService service;

    /**
     * 获取首页
     * 
     * @author java
     * @Date 2016年03月22日
     * @param userid,departid
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> parm) {
	Result rs = new Result();
	return rs;
    }
}
