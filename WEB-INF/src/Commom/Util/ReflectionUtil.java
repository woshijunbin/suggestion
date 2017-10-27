package Commom.Util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtil {
	/**
	 * 映射类属性（存入list）
	 */
	public Map<String, Object> getFieldList(Object o){
		Class<?> clazz = o.getClass();
		Field[] fields = clazz.getDeclaredFields();
		Map<String, Object> map = new HashMap<String, Object>();
		for (Field field : fields) {
			try {
				PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
				Method getMethod = pd.getReadMethod();
				Object value = getMethod.invoke(o);
				if (value != null && value.toString().length() > 0) {
					map.put(field.getName().toUpperCase(), value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	/**
	 * 映射类属性（存入list）
	 */
	public Map<String, Object> getFieldList(Object o, String str){
		Class<?> clazz = o.getClass();
		Map<String, Object> map = new HashMap<String, Object>();
		for (String col : str.split(",")) {
			try {
				PropertyDescriptor pd = new PropertyDescriptor(col.toLowerCase(), clazz);
				Method getMethod = pd.getReadMethod();
				Object value = getMethod.invoke(o);
				if (value != null && value.toString().length() > 0) {
					map.put(col.toUpperCase(), value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}
}
