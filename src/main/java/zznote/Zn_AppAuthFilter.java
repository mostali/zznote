package zznote;

import mpu.X;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zk_os.sec.AuthTokenSrv;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class Zn_AppAuthFilter implements Filter {

	private static final Logger L = LoggerFactory.getLogger(Zn_AppAuthFilter.class);

	public Zn_AppAuthFilter() {
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
		String n = request.getParameter("n");
//		String plane = ZKR.getPlaneFromRequest((HttpServletRequest) request);
//		NI.stop(plane);
		AuthTokenSrv.doAuthByParamToken(t, n);
	}

	@Override
	public void destroy() {

	}

}
