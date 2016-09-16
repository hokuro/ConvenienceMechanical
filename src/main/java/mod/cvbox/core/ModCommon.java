package mod.cvbox.core;

public final class ModCommon {
	// デバッグモードかどうか
	public static boolean isDebug = false;

	// モッドID
	public static final String MOD_ID = "cvbox";
	// モッド名
	public static final String MOD_NAME = "ConvenienceBox";
	public static final String MOD_PACKAGE = "mod.cvbox";
	public static final String MOD_CLIENT_SIDE = ".client.ClientProxy";
	public static final String MOD_SERVER_SIDE = ".core.CommonProxy";
	public static final String MOD_FACTRY = ".client.config.ConvenienceFactory";
	// モッドバージョン
	public static final String MOD_VERSION = "@VERSION@";
	// コンフィグファイル名
	public static final String MOD_CONFIG_FILE = "";
	// コンフィグ
	public static final String MOD_CONFIG_LANG = "";
	// コンフィグリロード間隔
	public static final long MOD_CONFIG_RELOAD = 500L;

	// コンフィグ カテゴリー general
	public static final String MOD_CONFIG_CAT_GENELAL = "general";
	public static final String MOD_CHANEL ="Mod_Channel_ConvenienceBox";


	// GUIID
	public static final int GUIID_EXPBANK = 0;
	public static final int GUIID_PLANTER = 5;
	public static final int GUIID_REPAIR = 100;


	// MessageID
	public static final int MESSID_MESSAGEPLANTING = 0;
	public static final int MESSID_MESSAGEECECECPERIENCE = 5;
	public static final int MESSID_MESSAGEEXPERIENCEINFO = 6;



	// Mod Const
	public static final int PLANTER_MAX_SLOT = 9;
//	public static final String MOD_CHANNEL1 = "AutoPlanting1";
//	public static final String MOD_CHANNEL2 = "AutoPlanting2";

	public static final int PLANTER_MAX_ROW_SLOT = 6;
	public static final int PLANTER_MAX_COL_SLOT = 9;
	public static final int PLANTER_CONTENASIZE = PLANTER_MAX_ROW_SLOT*PLANTER_MAX_COL_SLOT;




}
