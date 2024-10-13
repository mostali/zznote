package zznote;

import org.zkoss.zul.Window;
import zk_os.AppZosWeb;
import zk_page.core.SpVM;

public class AppZznWeb {

	public static void regZznPages() {

		AppZosWeb.regPageEntity("zznsi_pages", "zklogapp", "zk_os", "zk_pages");

//		AppZosWeb.regPageEntity(TristoreSP.class, AdminPageSP.class);
//		AppZosWeb.regPageEntity(PageReadVkSP.class, PageReadBookSP.class);
	}

	private static boolean isAdminOrOtherPage(SpVM spVM, Window window, String subdomain3, String fromPath) {
		switch (subdomain3) {
			case "rv":
//				new PageReadVkSP(this).create(window);
				return true;
			case "rb":
//				new PageReadBookSP(this).create(window);
				return true;
		}
		return false;
	}
}
