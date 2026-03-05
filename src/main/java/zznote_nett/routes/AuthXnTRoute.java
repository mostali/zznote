package zznote_nett.routes;


import botcore.RouteAno;
import botcore.clb.BotCallback;
import lombok.SneakyThrows;
import mp.utl_odb.netapp.mdl.NetUsrId;
import mpc.str.condition.StringConditionType;
import mpe.NT;
import nett.appb.TgCallback;
import nett.appb.TgRoute;
import zk_os.sec.AuthTokenSrv;

//@RouteAno(key = "ls\\s+.+", eq = StringConditionType.REGEX)
@RouteAno(key = "+", eq = StringConditionType.STARTS)
public class AuthXnTRoute extends TgRoute {

	public AuthXnTRoute() {
		super();
	}

	public AuthXnTRoute(TgRoute route) {
		super(route);
	}

	@Override
	protected void beforeUpdate() {
//		super.beforeUpdate();
	}

	@Override
	protected NetUsrId createDefaultUsrId() {
		return NetUsrId.def();
	}

	public static final TgCallback _CALLBACK_CHOICE_ = TgCallback.of("?");

	@SneakyThrows
	@Override
	public Object doUpdateCallback(BotCallback clb, String data2) {
//		DataClbMap dq = new DataClbMap(data2);
//
//		String a = dq.VAL("a");
//
//		int linenum = US.charAtInt(a, 0);
//		int lineA = US.charAtInt(a, 1);
//
//		String lineval = dq.line(linenum);
//		lineval = lineval == null ? "" : lineval;
//
//		dq.line(linenum, lineval + lineA);
//
//		if (BotRoute.L.isDebugEnabled()) {
//			BotRoute.L.debug("DQ:" + dq);
//		}
//		return new LineBuilder(dq).lineA(new int[]{linenum, lineA}).send(this);
		return null;
	}


	@Override
	public Object doUpdateMessage(String msgIn) {
		if ("+".equals(msgIn)) {
			return doAuth(null);
		} else if (msgIn.startsWith("+")) {
			return doAuth(msgIn.substring(1));
		} else {
			return "отправьте + для получения токена аутентификации";
		}
	}

	private String doAuth(String additionalyMsgData) {
		Long fromId = getChatIdAny();
		String firstName = getChatAny().getFirstName();
		String msg = AuthTokenSrv.produceTokenAndBuildFreshMsgWithLinkToken(NT.TG, additionalyMsgData, fromId, firstName);
		sendMsg_HTML(msg);
		return null;
	}
}
