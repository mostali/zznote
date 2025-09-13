package zznote;

import mpc.env.AP;
import mpc.env.APP;
import mpc.fs.UF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Service;
import utl_spring.AppContext;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author dav 09.01.2022   18:02
 */
@Service
public class AppZzn {

	public static final Logger L = LoggerFactory.getLogger(AppZzn.class);
//	public static final String APP_NS = ".data/" + AP.getAppName();

	//	public static void regDb(Class<? extends AModel> existModelClass, Class<? extends AModel> newModelClass, boolean... createDbIfNotExist) {
//		TypeDbEE.regDb(existModelClass, newModelClass, ARG.isPredicatEqTrue(createDbIfNotExist));
//	}


	private final static AtomicReference<AppZzn> INSTANCE = new AtomicReference<AppZzn>();

	private AppZzn() {
		final AppZzn previous = INSTANCE.getAndSet(this);
		if (previous != null) {
			L.warn("ZznApp singleton " + this + " created after " + previous);
		}
	}

	public static AppZzn get() {
		return INSTANCE.get();
	}

	public static BeanFactory beanFactory() {
		return AppContext.CONTEXT;
	}

	@Autowired
	public ApplicationArguments applicationArguments;

	public static org.springframework.boot.ApplicationArguments appArgs() {
		return get().applicationArguments;
	}

	public static Boolean isEnableBotTg() {
		return AP.getAs(APP.APK_TG_BT_ENABLE, Boolean.class, false);
	}

	public static Boolean isEnableBotVkLPS() {
		return APP.isEnableBotVkLPS();
	}

//	public static String getAppSiteName(String... defRq) {
//		return APP.getAppDomain(defRq);
//	}

	public static String getAppSiteNameWithSd3(String sd3, String... defRq) {
		return sd3 + "." + APP.MAIN.getAppHost(defRq);
	}

	public static String getAppSiteNameWithSd3Page(String sd3, String pagename, String... defRq) {
		return getAppSiteNameWithSd3(sd3, defRq) + UF.normDir(pagename);
	}

	public static String getAppSiteNameWithSd3PageToken(String sd3, String pagename, String token, String... defRq) {
		return getAppSiteNameWithSd3Page(sd3, pagename, defRq) + "?t=" + token;
	}

	public static void runned() {

	}
}
