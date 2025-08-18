package zznote;

import mp.utl_odb.typedb.TypeDb;
import mp.utl_odb.netapp.INetApp;
import mpe.NT;
import mp.utl_odb.netapp.NetApp;
import mp.utl_odb.netapp.NetUsrSrvEE;
import mp.utl_odb.netapp.mdl.NetUsrId;
import mp.utl_odb.netapp.mdl.NetUserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zk_os.AppZosCore;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ZznNetApp extends NetApp<ZznNetApp.BeaUsrId, ZznNetApp.BeaUserModel> {

	public static final Logger L = LoggerFactory.getLogger(ZznNetApp.class);

	public static void main(String[] args) throws NetUsrSrvEE {
//		AppBeaNet netApp = AppBeaNet.get();
//		BeaUsrId usr = BeaUsrId.newId(BeaUsrId.class, "go", String.valueOf(11));
//		netApp.updateUsrId__(usr, Upd.add);
//		P.exit(netApp.getUsers(10, 0));
	}

	public static final NT NET = NT.BEA;


	public TypeDb initUserDb() {

		Class<BeaUserModel> userModel = getEntityClassUserModel();

		TypeDb db = getDb(userModel);

		AppZznCore.regDb(db);

		db.checkOrCreateDb(true);

		return db;
	}

	@Override
	public TypeDb getDb(Class model) {
		Path path = Paths.get(AppZosCore.getAppDbFile());
		return TypeDb.of(model, path);
	}

	public static class BeaUsrId extends NetUsrId {
		public BeaUsrId(Long user_uid, String nt, String user_nid) {
			super(user_uid, nt, user_nid);
		}
	}

	public static class BeaUserModel extends NetUserModel {
	}

	@Override
	public Class<BeaUsrId> getEntityClassUserId() {
		return BeaUsrId.class;
	}

	@Override
	public Class<BeaUserModel> getEntityClassUserModel() {
		return BeaUserModel.class;
	}

	public static void init() {

		ZznNetApp appBeaNet = new ZznNetApp();
		appBeaNet.initUserDb();

	}

	static {
		init();
	}

	@Override
	public NT getNT() {
		return NET;
	}

	private ZznNetApp() {
		super(NET.name());
	}

	public static ZznNetApp get() {
		return (ZznNetApp) INetApp.getApp(NET.name());
	}

}
