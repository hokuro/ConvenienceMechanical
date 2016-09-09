package mod.cvmachine.core;

public class ModCommon {
	// デバッグモードかどうか
	public static boolean isDebug = false;

	// モッドID
	public static final String MOD_ID = "cvmachine";
	// モッド名
	public static final String MOD_NAME = "ConvenienceMechanical";
	public static final String MOD_PACKAGE = "mod.cvmachine";
	public static final String MOD_CLIENT_SIDE = ".client.ClientProxy";
	public static final String MOD_SERVER_SIDE = ".core.CommonProxy";
	public static final String MOD_FACTRY = ".client.config.ExBombsGuiFactory";
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
	public static final String MOD_CHANEL ="Mod_Channel_CVMachine";

	public static final int MOD_GUI_ID_MISSILE = 10;
	public static final int MOD_GUI_ID_RADAR = 0;
}
