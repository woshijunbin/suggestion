package Commom.Filter;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
public class EncodeFilter implements Filter {
	private String encoding;
	private FilterConfig filterConfig = null;
	public void destroy() {
		this.encoding = null;
		this.filterConfig = null;
	}
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding(this.encoding);
		chain.doFilter(request, response);
	}
	public void init(FilterConfig fConfig) throws ServletException {
		this.filterConfig = fConfig;
		this.encoding = filterConfig.getInitParameter("encoding");
	}
}
