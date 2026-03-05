package zznote;

import mpc.env.AP;
import mpc.env.APP;
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

	public static void runned() {

	}
}
