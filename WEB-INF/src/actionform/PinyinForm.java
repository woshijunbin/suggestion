package actionform;


import org.apache.struts.action.ActionForm;

public class PinyinForm extends ActionForm{

	private static final long serialVersionUID = -7424319305563483592L;

	private String firstChar;
	

	public String getFirstChar() {
		return firstChar;
	}
	public void setFirstChar(String firstChar) {
		this.firstChar = firstChar;
	}

	
	
}
