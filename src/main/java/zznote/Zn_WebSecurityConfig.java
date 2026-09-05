package zznote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.stereotype.Component;
import utl_spring.AppContext;
import zk_os.AppZosFilter;
import zk_os.db.WebUsrService;
import zk_os.sec.ROLE;
import zk_os.sec.Sec;
import zk_page.ZKR;
import zk_page.core.PageSP;


@Configuration
@EnableWebSecurity
//@Profile("standalone")
//@EnableAutoConfiguration(exclude = AuthenticationManager.class)
//@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
//@ComponentScan("zkbae.security.neww")

//https://www.zkoss.org/wiki/ZK_Spring_Essentials/Working_with_ZK_Spring/Working_with_ZK_Spring_Security
public class Zn_WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private static final Logger L = LoggerFactory.getLogger(Zn_WebSecurityConfig.class);

	public static final String ZUL_FILES_DENY = "/zkau/web/**/*.zul";

	//	http://q.com:8080/zkau/web/b145fca4/ckez/html/browse.zul?Type=Images&dtid=z_0-EsmczFu2d-7TYbGhgwAA&uuid=nLOO3&CKEditor=nLOO3-cnt&CKEditorFuncNum=3&langCode=ru
	public static final String[] ZK_RESOURCES_PERMIT = {
			"/zkau/web/**/js/**",
			"/zkau/web/**/zul/css/**",
			"/zkau/web/**/img/**",
	};

	// allow desktop cleanup after logout or when reloading login page
	public static final String REMOVE_DESKTOP_REGEX = "/zkau\\?dtid=.*&cmd_0=rmDesktop&.*";

	@Autowired
	AppZosFilter appFilter;
	@Autowired
	Zn_AppAuthFilter appAuthFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		if (false) {
//			http.authorizeRequests().antMatchers("/").permitAll();
//		}
		if (true) {
			//https://forum.zkoss.org/question/97836/fileupload-problem-onupload-not-fired-progressbar-stucked-at-100/
			//http.headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));

//			http.addFilterAfter(appAuthFilter, RememberMeAuthenticationFilter.class);
//			http.addFilterAfter(appFilter, AppAuthFilter.class);
			http.addFilterAfter(appFilter, RememberMeAuthenticationFilter.class);

			http.headers().frameOptions().sameOrigin();
			http.csrf().disable();
			ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry cfg = http.authorizeRequests();
			cfg.antMatchers("/zkau/web/**/ckez/html/browse.zul**").hasAnyRole(ROLE.ROLE_ADMIN, ROLE.ROLE_OWNER, ROLE.ROLE_EDITOR);//TODO wth !ROLE_GA ?
			cfg.antMatchers(ZUL_FILES_DENY).denyAll() // block direct access to zul files
					.antMatchers(HttpMethod.GET, ZK_RESOURCES_PERMIT).permitAll() // allow zk resources
					.regexMatchers(HttpMethod.GET, REMOVE_DESKTOP_REGEX).permitAll() // allow desktop cleanup
					.requestMatchers(req -> "rmDesktop".equals(req.getParameter("cmd_0"))).permitAll() // allow desktop cleanup from ZATS
					.mvcMatchers("/", "/login", "/logout").permitAll()
					.mvcMatchers("/secure/**").hasAnyRole(ROLE.ROLE_ADMIN, ROLE.ROLE_OWNER);

			cfg = cfg
					.mvcMatchers("/admin**").hasAnyRole(ROLE.ROLE_ADMIN, ROLE.ROLE_OWNER)
					.mvcMatchers("/@@cfg**").hasAnyRole(ROLE.ROLE_ADMIN, ROLE.ROLE_OWNER)
					.mvcMatchers("/" + PageSP.PAGENAME_LOGS + "**").hasAnyRole(ROLE.ROLE_OWNER)
					.mvcMatchers("/@@status**").hasAnyRole(ROLE.ROLE_ADMIN, ROLE.ROLE_OWNER)
					.mvcMatchers("/@@assets**").hasAnyRole(ROLE.ROLE_ADMIN, ROLE.ROLE_OWNER, ROLE.ROLE_EDITOR)
					.mvcMatchers("/@assets**").hasAnyRole(ROLE.ROLE_ADMIN, ROLE.ROLE_OWNER)
					.mvcMatchers("/@@uploads**").hasAnyRole(ROLE.ROLE_ADMIN, ROLE.ROLE_OWNER, ROLE.ROLE_EDITOR)
					.mvcMatchers("/@uploads**").hasAnyRole(ROLE.ROLE_ADMIN, ROLE.ROLE_OWNER);

			cfg
					.mvcMatchers("/**").permitAll()
					.anyRequest().authenticated()
					.and().formLogin().loginPage("/login")
					.defaultSuccessUrl("/")///secure/main, admin
					//bycookie
					.and()
					.rememberMe()
					.alwaysRemember(true)
					.tokenValiditySeconds(ZKR.getCookieAuthTimeout())
					.rememberMeCookieName("udata")
					.key("123")
					//bycookie
					.and().logout().logoutUrl("/logout").logoutSuccessUrl("/");
		}
	}

	@Component
	public static class CustomAuthenticationProvider implements AuthenticationProvider {

//		public static final String AUTH_SERVICE = "authService2";

		@Override
		public Authentication authenticate(Authentication authentication) throws AuthenticationException {
			String name = authentication.getName();
			UserDetails ud = AppContext.getBean(UserDetailsService.class).loadUserByUsername(name);
//			String password = authentication.getCredentials().toString();
			return Sec.doAuthByLoginPass(name, ud.getPassword(), true);
//			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(ud.getUsername(), ud.getPassword(), AR.as(ROLE.GA_ROLE_USER, ROLE.GA_ROLE_ADMIN));
//			return auth;
		}

		@Override
		public boolean supports(Class<?> authentication) {
//			return authentication.equals(UsernamePasswordAuthenticationToken.class);
			return true;
		}
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new WebUsrService();
	}

}