package zznote;

import mpu.X;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AppAuthFilter implements Filter {

	private static final Logger L = LoggerFactory.getLogger(AppAuthFilter.class);

	public AppAuthFilter() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		X.nothing();
		//фильтр работает в контексте веб контейнера, и бины спринга ему не доступны.
		//поэтому, если хотим использовать здесь userDao, например, необходимо выполнить следующий метод:
//		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		checkAndDoAuth(request);

		chain.doFilter(request, response);
	}

	private void checkAndDoAuth(ServletRequest request) {
		String t = request.getParameter("t");
		if (X.empty(t)) {
			return;
		}
		AuthSrv.doAuthByParamT(t);
	}

	@Override
	public void destroy() {

	}

}
