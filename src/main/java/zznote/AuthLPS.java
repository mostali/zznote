package zznote;

import botcore.DefMsg;
import com.google.gson.JsonObject;
import com.vk.api.sdk.client.actors.GroupActor;
import lombok.SneakyThrows;
import mpc.env.boot.AppBoot;
import mpe.NT;
import mpu.str.STR;
import netv5.api.bot_example.SimpleLongPoolServer;

public class AuthLPS extends SimpleLongPoolServer {

	@SneakyThrows
	public static void runAsDefaultLPS() {
		GroupActor defaultGroupActor = getDefaultGroupActor(null);
		if (defaultGroupActor != null) {
			AppBoot.bootLog("Running Bot {}:{}:{}", AuthLPS.class.getSimpleName(), STR.toStringSE(defaultGroupActor.getGroupId() + "", 2), STR.toStringSE(defaultGroupActor.getAccessToken(), 4));
			AuthLPS authLPS = new AuthLPS(defaultGroupActor);
			authLPS.run();
			Integer defaultOwnerId = AuthLPS.BT_OWNER_ID.getValueOrDefault(null);
			if (defaultOwnerId != null) {
				authLPS.sendFromGaToUser(defaultOwnerId, DefMsg.buildOwnerStartMessage());
			}
		} else {
			if (L.isInfoEnabled()) {
				L.info("VkLpsBot not run - no config");
			}
		}

	}

	public AuthLPS(GroupActor actor) {
		super(actor);
	}


	public static final int TIMEOUT_MIN = 30;

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
		String msg = AuthSrv.produceTokenAndBuildFreshMsgWithLinkToken(NT.VK, additionalyMsgData, fromId.longValue());
		sendFromGaToUser(fromId, msg);
	}

}