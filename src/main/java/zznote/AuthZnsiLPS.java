package zznote;

import botcore.DefMsg;
import com.google.gson.JsonObject;
import com.vk.api.sdk.client.actors.GroupActor;
import lombok.SneakyThrows;
import mpc.env.boot.AppBoot;
import mpe.NT;
import mpu.str.STR;
import netv5.api.bot_example.SimpleLongPoolServer;
import zk_os.sec.AuthTokenSrv;

public class AuthZnsiLPS extends SimpleLongPoolServer {

	@SneakyThrows
	public static void runAsDefaultLPS() {
		GroupActor defaultGroupActor = getDefaultGroupActor(null);
		if (defaultGroupActor != null) {
			AppBoot.bootLog("Running Bot {}:{}:{}", AuthZnsiLPS.class.getSimpleName(), STR.toStringSE(defaultGroupActor.getGroupId() + "", 2), STR.toStringSE(defaultGroupActor.getAccessToken(), 4));
			AuthZnsiLPS authLPS = new AuthZnsiLPS(defaultGroupActor);
			authLPS.run();
			Integer defaultOwnerId = AuthZnsiLPS.BT_OWNER_ID.getValueOrDefault(null);
			if (defaultOwnerId != null) {
				authLPS.sendFromGaToUser(defaultOwnerId, DefMsg.buildOwnerStartMessage());
			}
		} else {
			if (L.isInfoEnabled()) {
				L.info("VkLpsBot not run - no config");
			}
		}

	}

	public AuthZnsiLPS(GroupActor actor) {
		super(actor);
	}


	@SneakyThrows
	@Override
	protected void handle(JsonObject emulateModel) {
		super.handle(emulateModel);

		VkMsgModelContract msgCrt = VkMsgModelContract.of(emulateModel);
		String msg = msgCrt.msg_text();
		if ("+".equals(msg)) {
			doAuth(msgCrt, null);
		} else if (msg.startsWith("+")) {
			doAuth(msgCrt, msg.substring(1));
		}
	}

	private void doAuth(VkMsgModelContract msgCrt, String additionalyMsgData) {
		Integer fromId = msgCrt.msg_from_id();
		Integer peer_id = msgCrt.peer_id();
		String msg = AuthTokenSrv.produceTokenAndBuildFreshMsgWithLinkToken(NT.VK, additionalyMsgData, fromId.longValue());
		sendFromGaToUser(fromId, msg);
	}

}