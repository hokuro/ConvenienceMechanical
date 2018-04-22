package mod.cvbox.core;

public final class ModCommon {
	// デバッグモードかどうか
	public static boolean isDebug = false;

	// モッドID
	public static final String MOD_ID = "cvbox";
	// モッド名
	public static final String MOD_NAME = "ConvenienceBox";
    // 前に読み込まれるべき前提MODをバージョン込みで指定
    public static final String MOD_DEPENDENCIES = "required-after:Forge@[1.9-12.16.0.1853,)";
    // 起動出来るMinecraft本体のバージョン。記法はMavenのVersion Range Specificationを検索すること。
    public static final String MOD_ACCEPTED_MC_VERSIONS = "[1.12]";
	public static final String MOD_PACKAGE = "mod.cvbox";
	public static final String MOD_CLIENT_SIDE = ".client.ClientProxy";
	public static final String MOD_SERVER_SIDE = ".core.CommonProxy";
	public static final String MOD_FACTRY = ".client.config.ConvenienceFactory";
	// モッドバージョン
	public static final String MOD_VERSION = "1.12.0";
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
	public static final int GUIID_EXPBANK = 1;
	public static final int GUIID_EXENCHANTER = 2;
	public static final int GUIID_EXANVIL = 3;
	public static final int GUIID_PLANTER = 101;
	public static final int GUIID_HARVESTER = 102;
	public static final int GUIID_WOODPLANTER = 103;
	public static final int GUIID_WOODHARVESTER = 104;
	public static final int GUIID_SETTER = 201;
	public static final int GUIID_KILLER = 202;
	public static final int GUIID_EXPCOLLECTOR = 203;
	public static final int GUIID_CRUSHER = 204;
	public static final int GUIID_COMPLESSER = 205;
	public static final int GUIID_STRAW = 206;

	public static final int GUIID_REPAIR = 201;

	// GUINAME
	public static final String GUIID_EXENCHANTMENT = "exenchantment";


	// MessageID
	public static final int MESSID_MESSAGEPLANTING = 101;
	public static final int MESSID_MESSAGEECECECPERIENCE = 1;
	public static final int MESSID_MESSAGEEXPERIENCEINFO = 2;
	public static final int MESSID_MESSAGEUPDATEENCHANT = 3;
	public static final int MESSID_MESSAGECLEARENCHANT = 4;
	public static final int MESSID_MESSAGEEXECENCHANT = 5;
	public static final int MESSID_MESSAGEFARMERUPDATE = 6;
	public static final int MESSID_MESSAGEKILLAREAUPDATE = 7;
	public static final int MESSID_MESSAGEKILLTARGETUPDATE = 8;
	public static final int MESSID_MESSAGELEVELUP = 9;
	public static final int MESSID_MESSAGECRUSHERSELECTCHANGE = 10;
	public static final int MESSID_MESSAGESTRAWAREAPUDATE = 11;
	public static final int MESSID_MESSAGESTRAWGETALL = 12;
	public static final int MESSID_MESSAGESTRAWCLEAR = 13;



	// Mod Const
	public static final int PLANTER_MAX_SLOT = 9;
//	public static final String MOD_CHANNEL1 = "AutoPlanting1";
//	public static final String MOD_CHANNEL2 = "AutoPlanting2";



























}
