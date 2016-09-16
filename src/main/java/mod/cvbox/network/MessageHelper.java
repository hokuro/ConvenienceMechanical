package mod.cvbox.network;

import mod.cvbox.core.ModCommon;
import mod.cvbox.core.Mod_ConvenienceBox;
import net.minecraftforge.fml.relauncher.Side;

public class MessageHelper {

	public static void RegisterMessage(){
		Mod_ConvenienceBox.Net_Instance.registerMessage(MessagePlanting.class, MessagePlanting.class, ModCommon.MESSID_MESSAGEPLANTING, Side.CLIENT);
		Mod_ConvenienceBox.Net_Instance.registerMessage(MessageExecExperience.class, MessageExecExperience.class, ModCommon.MESSID_MESSAGEECECECPERIENCE, Side.SERVER);
		Mod_ConvenienceBox.Net_Instance.registerMessage(MessageExperienceInfo.class, MessageExperienceInfo.class, ModCommon.MESSID_MESSAGEEXPERIENCEINFO, Side.CLIENT);
		//Mod_ConvenienceBox.Net_Instance.registerMessage(Message2.class, Message2.class, 2, Side.CLIENT);
	}



	public static void MessagePlanting(int x, int y, int z){
		Mod_ConvenienceBox.Net_Instance.sendToAll(new MessagePlanting(x,y,z));
	}

	public static void MessageExecExperience(int mode, int x, int y, int z, int exp){
		Mod_ConvenienceBox.Net_Instance.sendToServer(new MessageExecExperience(mode,x,y,z,exp));
	}
}
