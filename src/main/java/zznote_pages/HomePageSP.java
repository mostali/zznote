package zznote_pages;

import lombok.SneakyThrows;
import org.zkoss.zul.Window;
import zk_com.base.Bt;
import zk_com.sun_editor.IPerPage;
import zk_os.sec.ROLE;
import zk_page.ZKS;
import zk_page.core.PageRoute;
import zk_page.core.PageSP;
import zk_page.core.SpVM;
import zk_page.with_com.WithSearch;
import zklogapp.AppLogSettingsPanel;

@PageRoute(pagename = "home", role = ROLE.ANONIM)
public class HomePageSP extends PageSP implements IPerPage, WithSearch {//, WithLogo

	private AppLogSettingsPanel pageHeader = null;

	public HomePageSP(Window window, SpVM spVM) {
		super(window, spVM);
	}

	@SneakyThrows
	public void buildPageImpl() {

		ZKS.PADDING0(window);
		ZKS.MARGIN(window, "30px 0 0 0");

		window.appendChild(new Bt("hello"));

	}


}
