package zznote_pages;

import lombok.SneakyThrows;
import mpc.fs.UF;
import mpc.fs.tmpfile.TmpFileOperation;
import mpe.rt.core.ExecRq;
import mpu.Sys;
import mpu.str.JOIN;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Window;
import zk_com.base.Bt;
import zk_com.sun_editor.IPerPage;
import zk_form.notify.ZKI;
import zk_os.sec.ROLE;
import zk_page.ZKR;
import zk_page.ZKS;
import zk_page.core.PageRoute;
import zk_page.core.PageSP;
import zk_page.core.SpVM;
import zklogapp.AppLogSettingsPanel;

import java.nio.file.Path;
import java.util.List;

@PageRoute(pagename = "home", role = ROLE.USER)
public class HomePageSP extends PageSP implements IPerPage {//, WithLogo

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
