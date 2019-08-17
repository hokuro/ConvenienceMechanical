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
	public static final String MOD_CHANEL ="mod_channec_conveniencebox";


	// GUIID
	public static final String GUIID_EXPBANK = "gui_expbank";
	public static final String GUIID_EXENCHANTER = "gui_exenchant";
	public static final String GUIID_EXANVIL = "gui_exanvil";
	public static final String GUIID_PLANTER = "gui_planter";
	public static final String GUIID_HARVESTER = "gui_harvester";
	public static final String GUIID_WOODPLANTER = "gui_woodplanter";
	public static final String GUIID_WOODHARVESTER = "gui_woodharvester";
	public static final String GUIID_SETTER = "gui_setter";
	public static final String GUIID_KILLER = "gui_killer";
	public static final String GUIID_EXPCOLLECTOR = "gui_expcollector";
	public static final String GUIID_CRUSHER = "gui_crusher";
	public static final String GUIID_COMPLESSER = "gui_complesser";
	public static final String GUIID_STRAW = "gui_straw";
	public static final String GUIID_DESTROY="gui_destroy";
	public static final String GUIID_LIQUIDMAKER = "gui_liquidmaker";
	public static final String GUID_VACUMER = "gui_vaclumer";


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
	public static final int MESSID_MESSAGERESET = 14;
	public static final int MESSID_MESSAGEUPDATEDESTROY = 15;
	public static final int MESSID_MESSAGEBOXPOWER = 201;



	// Mod Const
	public static final int PLANTER_MAX_SLOT = 9;
//	public static final String MOD_CHANNEL1 = "AutoPlanting1";
//	public static final String MOD_CHANNEL2 = "AutoPlanting2";
































}
