package Commom.Util;

import java.io.StringReader;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * dom解析工具
 * @author Administrator
 *
 */
public class DomUtils {

	@SuppressWarnings("unchecked")
	public static List<Node> saxParse(String code,String xpath){
		SAXReader reader=new SAXReader();
		try {
			Document doc = reader.read(new StringReader(code));
			return doc.selectNodes(xpath);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
