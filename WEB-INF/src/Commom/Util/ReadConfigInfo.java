package Commom.Util;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
public class ReadConfigInfo {
	private static Properties properties = new Properties();
	static {
		InputStream iStream = null;
		try {
			iStream = ReadConfigInfo.class.getResourceAsStream("../../configure/config.properties");
			properties.load(iStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (iStream != null) {
				try {
					iStream.close();
				} catch (IOException e) { }
			}
		}
	}
	/**
	 * 根据键值取值
	 */
	public static String getProperty(String key) {
		return properties.getProperty(key);
	}
//	public static void setProperty(String key, Object value){
//		properties.setProperty(key, value.toString());
//	}
}
