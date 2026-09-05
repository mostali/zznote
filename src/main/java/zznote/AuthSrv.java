//package zznote;
//
//import mp.utl_odb.tree.ctxdb.CtxtDb;
//import mp.utl_odb.tree.UTree;
//import mpc.exception.StackTraceRuntimeException;
//import mpe.ftypes.core.FDate;
//import mpu.X;
//import mpe.core.P;
//import mpc.exception.FIllegalArgumentException;
//import mpc.json.GsonMap;
//import mpu.str.STR;
//import mpu.str.UST;
//import mpu.core.QDate;
//import mpu.pare.Pare;
//import mpu.pare.Pare3;
//import mpe.NT;
//import netv5.api.bot_example.SimpleLongPoolServer;
//import org.jetbrains.annotations.NotNull;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import zk_os.db.WebUsrService;
//import zk_os.db.net.WebUsr;
//import zk_os.sec.Sec;
//import zk_notes.AppNotesCore;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//
//public class AuthSrv {
//
//	public static final Logger L = LoggerFactory.getLogger(SimpleLongPoolServer.class);
//
//	public static Pare<String, QDate> doAuth_ProduceToken(long nid, String additionalyMsgData, NT nt) {
//		Integer hoursTimeout = UST.INT(additionalyMsgData, null);
//		if (hoursTimeout != null && (hoursTimeout > 9999)) {
//			hoursTimeout = null;//it name
//		}
//		UTree authTree = AppNotesCore.TREE_AUTH_USERS();
//		QDate expired = newExpired();
//		if (hoursTimeout != null) {
//			expired = expired.addHours(hoursTimeout);
//		}
//		String tk = UUID.randomUUID().toString();
//
//		String net = nt.name();
//
//
//		if (hoursTimeout == null) {
//
//			String alias = additionalyMsgData;
//
//			WebUsrService webUsrService = WebUsrService.get();
//			List<WebUsr> foundAllAliases = webUsrService.loadUserByAlias(alias, true);
//			if (X.empty(foundAllAliases)) {
//				//alias is free - ok
//			} else {
//				Optional<WebUsr> first = foundAllAliases.stream().filter(u -> X.equals(u.getNt(), net) && X.equals(u.getNid(), nid)).findFirst();
//				if (first.isPresent()) {
//					//ok found
//				} else {
//					throw new FIllegalArgumentException("alias '%s' busy", alias);
//				}
//			}
//		}
//
//		GsonMap outRslt = GsonMap.ofKV("exp", expired.epoch(), "nid", nid, "net", net, "alias", hoursTimeout == null ? additionalyMsgData : null);
//		CharSequence json = outRslt.toStringJson();
//
////		doAuth0(outRslt);
//
//		authTree.put(nid + "", tk, json);
//
//		if (L.isInfoEnabled()) {
//			L.info("Auth '{}' with token '{}'", nid, tk);
//		}
//
//		P.warnBig("append clean auth tree with token");
//
//		return Pare.of(tk, expired);
//	}
//
//	public static QDate newExpired() {
//		return QDate.now().addMinutes(AuthLPS.TIMEOUT_MIN);
//	}
//
//	//Pare3.of(true, "OK", extJsonString)
//	public static Pare3<Boolean, String, String> isAuth_OkStatusJson(String token) {
//		UTree authTree = AppNotesCore.TREE_AUTH_USERS();
//		CtxtDb.CtxTimeModel ctxTimeModelByValue0 = authTree.getCtxTimeModelByValue(token);
//		if (ctxTimeModelByValue0 == null) {
//			if (L.isWarnEnabled()) {
//				L.warn("Access denied:" + token);
//			}
//			return Pare3.of(false, "FAIL", null);
//		}
//		GsonMap extJson = ctxTimeModelByValue0.getExtAs(GsonMap.class);
//
//		QDate qDate = QDate.ofEpoch((Integer) extJson.getAs("exp", Integer.class));
//		boolean isExpired = QDate.now().isAfter(qDate);
//		long diffabs_sec = qDate.diffabs(QDate.now(), TimeUnit.SECONDS).longValue();
//		if (isExpired) {
//			if (L.isInfoEnabled()) {
//				L.info("expired:" + diffabs_sec + "sec");
//			}
//			return Pare3.of(false, "EXPIRED", ctxTimeModelByValue0.getExt());
//		}
//
//		String extJsonString = extJson.toStringJson().toString();
//
//		if (diffabs_sec > AuthLPS.TIMEOUT_MIN * 60) {
//			//set manually
//		} else {
//			//prolong timeout
//			extJson.put("exp", newExpired().epoch());
//			authTree.put(ctxTimeModelByValue0.getKey(), ctxTimeModelByValue0.getValue(), extJsonString);
//		}
//
//		if (L.isInfoEnabled()) {
//			L.info("Allowed:" + ctxTimeModelByValue0.getKey());
//		}
//		return Pare3.of(true, "OK", extJsonString);
//	}
//
//	public static void doAuthByParamT(String token) {
//		Pare3<Boolean, String, String> auth = AuthSrv.isAuth_OkStatusJson(token);
//		if (!auth.key()) {
//			return;
//		}
//		if (Sec.setAuthByToken(auth) != null) {
//			L.info("Auth is success:" + auth.ext());
//		} else {
//			L.error("Auth is wrong (after success):\n" + auth.ext(), new StackTraceRuntimeException());
//		}
//	}
//
//
//	@NotNull
//	public static String produceTokenAndBuildFreshMsgWithLinkToken(NT nt, String additionalyMsgData, Long fromId) {
//		Pare<String, QDate> auth = AuthSrv.doAuth_ProduceToken(fromId, additionalyMsgData, nt);
//		String msg = auth.key() + STR.NL + "http://" + nt.shortPfx() + fromId + "." + AppZzn.getA(null) + "?t=" + auth.key() + STR.NL + "Токен действителен до " + auth.val().f(FDate.YYYY_DB_ISO_STANDART);
//		return msg;
//	}
//}
