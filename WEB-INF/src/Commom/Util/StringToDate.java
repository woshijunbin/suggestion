package Commom.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

/**
 * 用于支持将页面String参数自动转换为java.util.Date
 * @author Administrator
 *
 */
public class StringToDate implements Converter,PlugIn {
	 static{
	      ConvertUtils.register(new StringToDate(), Date.class);
	      ConvertUtils.register(new StringToDate(), java.sql.Date.class);
	  }

	@Override
	public void init(ActionServlet arg0, ModuleConfig arg1) throws ServletException {
		System.out.println("初始化plugIn----DateUtil");
	}

	@Override
	public void destroy() {
		
	}
	
	
	
	
	
	 /**
     * 日期格式化对象.
     */
    private static SimpleDateFormat df=new SimpleDateFormat();
    
    /**
     * 模式集合.
     */
    private static Set<String> patterns =new HashSet<String>();
    //注册一下日期的转换格式
    static{
        StringToDate.patterns.add("yyyy-MM-dd");
        StringToDate.patterns.add("yyyy-MM-dd HH:mm");
        StringToDate.patterns.add("yyyy-MM-dd HH:mm:ss");
        StringToDate.patterns.add("yyyy/MM/dd HH:mm:ss");
    }
    
    /**
     * 日期转换器.
     * @param type Class
     * @param value Object
     * return Date Object.
     */
    @SuppressWarnings("rawtypes")
	@Override
    public Object convert(Class type,Object value){
        if(value== null){
            return null;
        }else if(value instanceof String){
            Object dateObj = null;
            Iterator<String> it = patterns.iterator();
            while(it.hasNext()){
                try{
                    String pattern =(String)it.next();
                    df.applyPattern(pattern);
                    dateObj = df.parse((String)value);
                    break;//正确匹配string与pattern格式
                }catch(ParseException ex){
                    //do iterator continue
                }
            }
            return dateObj;
        }else{
            return null;
        }
    }
}
