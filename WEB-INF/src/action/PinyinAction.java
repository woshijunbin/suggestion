package action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import actionform.PinyinForm;
import entity.Fypyzd;
import service.FypyzdService;



/**
 * 
 * @author Administrator
 *
 */
public class PinyinAction extends Action{

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//获取后台传递数据
		PinyinForm pinyinForm = (PinyinForm)form;
		//pinyinForm.setSp(null);
		List<Fypyzd> fypyes = new FypyzdService().getSplitPinyin(pinyinForm);
		List<String> itemes = new ArrayList<String>(0);
		
		if (  !fypyes.isEmpty() ) {
			for (Fypyzd fypyzd : fypyes) {
				itemes.add(fypyzd.getContent());
			}
		}
		
		ObjectMapper mapper = new ObjectMapper();  
		String jsonlist = mapper.writeValueAsString(itemes);  
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().write( jsonlist   ); 
		return null;
	}
}