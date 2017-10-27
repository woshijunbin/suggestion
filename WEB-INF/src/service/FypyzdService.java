package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang3.StringUtils;

import Commom.DaoUtil.HibernateUtil;
import actionform.PinyinForm;
import entity.Fypyzd;

public class FypyzdService {

	
	/**
	 * 分页。不分页置分页对象ShowPage为null<br />
	 * @param form
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Fypyzd> getSplitPinyin(PinyinForm form) {
		Map<String, Object> where =new HashMap<String, Object>(0); 
		String char_Pattern = "[a-zA-Z]+|[\u4e00-\u9fa5]+";//全拼音或全中文
		
		String firstChar = form.getFirstChar();
		if ( StringUtils.isNotBlank(firstChar) 
				&& checkPattern(firstChar.trim(),char_Pattern)) {
			where.put("[PY] LIKE ? OR  [CONTENT] LIKE ?",new Object[]{firstChar.trim()+"%","%"+firstChar.trim()+"%"}) ;
		}else{
			//firstChar=""，null， 或拼音中文混输情况
			return new ArrayList<Fypyzd>(0);
		}
		
		
		
		String order = " ORDER BY id DESC ";
		
		return HibernateUtil.listData(Fypyzd.class, where, null, order);
	}

	/**
	 * 检验传入数据是否正确。
	 * @return
	 */
	private boolean checkPattern(String valiStr,String regex)throws PatternSyntaxException {
		if ( StringUtils.isNotBlank(valiStr) ) {
			return valiStr.matches(regex);
		}
		return false;
	}
	

	public static void main(String[] args) {
		boolean b = new FypyzdService().checkPattern("中文a", "[a-zA-Z ]+|[\u4e00-\u9fa5]+");
		System.err.println(b);
		
		
		
	}
}
