package com.adminview.ims.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ParamsUtil {

    /**
     * 从map中获取String
     * 
     * @param map
     * @param key
     * @return
     */
    public static final String getString4Map(Map<String, Object> map, String key) {
	String value = "";
	Object obj = map.get(key);
	if (null != obj) {
	    value = obj.toString();
	}
	return value;
    }

    /**
     * 从map中获取int
     * 
     * @param map
     * @param key
     * @return
     */
    public static final int getInt4Map(Map<String, Object> map, String key) {
	int value = 0;
	Object obj = map.get(key);
	if (null != obj) {
	    value = Integer.parseInt(obj.toString());
	}
	return value;
    }

    /**
     * 从map中获取float
     * 
     * @param map
     * @param key
     * @return
     */
    public static final float getFloat4Map(Map<String, Object> map, String key) {
	float value = 0;
	Object obj = map.get(key);
	if (null != obj) {
	    value = Float.parseFloat(obj.toString());
	}
	return value;
    }

    /**
     * 将null转换为字符串""
     * 
     * @param map
     * @return
     */
    public static final Map<String, Object> conversMapNull2String(Map<String, Object> map) {
	Map<String, Object> result = new HashMap<String, Object>();
	for (Entry<String, Object> entry : map.entrySet()) {
	    result.put(entry.getKey(), (null == entry.getValue() ? "" : entry.getValue()));
	}
	return result;
    }
}
