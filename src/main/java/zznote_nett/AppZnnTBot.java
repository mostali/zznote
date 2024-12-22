package zznote_nett;

import lombok.Getter;
import lombok.SneakyThrows;
import mp.utl_odb.netapp.NetUsrSrvEE;
import mpc.env.boot.AppBoot;
import mpu.str.Rt;
import nett.appb.DefTgApp;
import nett.appb.TgApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zznote.ZznNetApp;

import java.io.IOException;

public class AppZnnTBot {
	public static final Logger L = LoggerFactory.getLogger(AppZnnTBot.class);

	@Getter
	private TgApp appBot;

	private static class Holder {
		private static final AppZnnTBot INSTANCE = new AppZnnTBot();
	}

	public static AppZnnTBot get() {
		return Holder.INSTANCE;
	}

	@SneakyThrows
	public static void boot_app() throws IOException, ClassNotFoundException, NetUsrSrvEE {
		boot_app(null);
	}

	public static void boot_app(String[] route_packages) throws IOException, ClassNotFoundException, NetUsrSrvEE {

		if (route_packages == null) {
			route_packages = new String[]{AppZnnTBot.class.getPackage().getName() + ".routes"};
		}

		ZznNetApp netApp = ZznNetApp.get();

		DefTgApp tgApp = DefTgApp.startTgApp(netApp, route_packages);

		get().appBot = tgApp;

		AppBoot.bootLog("TgApp was loaded with map routes:\n{}", Rt.buildReport(tgApp.getRootRoute().getMapRoutes()));
	}


}
