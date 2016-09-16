package mod.cvbox.config;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ConfigValue{
	public static final int MAX_FIELD = 5000;
	public static final int MAX_DISTANCE = 100;
	private static final ModConfig config = new ModConfig();

	public static class General{
		@ConfigProperty(comment="Maximum farmland 1-5000")
		public static int MaxFarmland = 1000;
		@ConfigProperty(comment="Maximum Distance 1-100")
		public static int MaxDistance = 30;
		@ConfigProperty(comment="right click possible [true/false]")
		public static boolean RightClick = true;
		@ConfigProperty(comment="enable item ids")
		public static String TargetItemIds = "";

		public static boolean Check(){
			boolean ischange = false;
			if (MaxFarmland < 1) {
				MaxFarmland = 1;
				ischange = true;
			} else if (MaxFarmland > MAX_FIELD) {
				MaxFarmland = MAX_FIELD;
				ischange = true;
			}
			if (MaxDistance < 1) {
				MaxDistance = 1;
				ischange = true;
			} else if (MaxDistance > MAX_DISTANCE) {
				MaxDistance = MAX_DISTANCE;
				ischange = true;
			}
			return ischange;
		}
	}

	public static void init(FMLPreInitializationEvent event){
		config.init(new Class<?>[]{General.class}, event);
		boolean ischagne = General.Check();
		if(ischagne){config.saveConfig();}
	}


}