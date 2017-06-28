package com.framework.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class ExportExcel {
    private static final Log logger = LogFactory.getLog(ExportExcel.class);

    // 导出excel
    public static void exportWord(String templatePath, Map<String, Object> dataMap, String buildFile) {
	Writer writer = null;
	try {
	    Configuration configuration = new Configuration();
	    configuration.setDefaultEncoding("utf-8");
	    configuration.setClassForTemplateLoading(ExportExcel.class, "/conf/ftl/");
	    File outFile = new File(buildFile);

	    if (!new File(outFile.getParent()).exists()) {
		new File(outFile.getParent()).mkdirs();
	    }

	    Template template = configuration.getTemplate(templatePath);
	    template.setEncoding("utf-8");
	    writer = new BufferedWriter(
		    new OutputStreamWriter(new FileOutputStream(outFile), Charset.forName("utf-8")));// 此处为输
												     // 出文档编码
	    template.process(dataMap, writer);

	} catch (Exception e) {
	    logger.error(e);
	    throw new RuntimeException(e);
	} finally {
	    if (null != writer) {
		try {
		    writer.flush();
		    writer.close();
		} catch (IOException e) {
		    logger.error(e);
		}
	    }
	}
    }

    public static void downloadFile(HttpServletResponse response, String path) {
	BufferedInputStream in = null;
	BufferedOutputStream out = null;
	File f = null;
	try {
	    response.setContentType("text/html;charset=UTF-8");
	    f = new File(path);
	    response.setContentType("application/x-excel");
	    response.setCharacterEncoding("UTF-8");
	    String ss = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	    int random = (int) (Math.random() * 10000);
	    String fileName = ss + "_" + random + ".xls";
	    response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
	    response.setHeader("Content-Length", String.valueOf(f.length()));
	    in = new BufferedInputStream(new FileInputStream(f));
	    out = new BufferedOutputStream(response.getOutputStream());
	    byte[] data = new byte[1024];
	    int len = 0;
	    while (-1 != (len = in.read(data, 0, data.length))) {
		out.write(data, 0, len);
	    }
	} catch (Exception e) {
	    logger.error(e);
	} finally {
	    try {
		if (in != null) {
		    in.close();
		}
		if (out != null) {
		    out.close();
		}
	    } catch (IOException e) {
		logger.error(e);
	    }
	    try {
		if (null != f && f.exists()) {
		    f.delete();
		}
	    } catch (Exception e) {
		logger.error(e);
	    }
	}
    }
}
