package mod.cvbox.core;

import mod.cvbox.block.BlockCore;
import mod.cvbox.item.ItemCore;
import mod.cvbox.network.MessageCrusher_ChangeSelect;
import mod.cvbox.network.MessageEXPCollector_LevelUp;
import mod.cvbox.network.MessageExEnchant_ClearParameter;
import mod.cvbox.network.MessageExEnchant_ExecEnchant;
import mod.cvbox.network.MessageExEnchant_UpdateParameter;
import mod.cvbox.network.MessageExpBank_ExecExperience;
import mod.cvbox.network.MessageExpBank_ExperienceInfo;
import mod.cvbox.network.MessageFarmer_UpdateDelivery;
import mod.cvbox.network.MessageKiller_UpdateAreaSize;
import mod.cvbox.network.MessageKiller_UpdateTarget;
import mod.cvbox.network.MessageStraw_AreaSizeUpdate;
import mod.cvbox.network.MessageStraw_ClearArea;
import mod.cvbox.network.MessageStraw_GetAll;
import mod.cvbox.network.Message_BoxSwitchChange;
import mod.cvbox.network.Message_ResetWork;
import mod.cvbox.network.Message_UpdateDestroy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

public class ModRegister {
	public static void RegisterBlock(FMLPreInitializationEvent event) {
		BlockCore.register(event);
	}

	public static void RegisterItem(FMLPreInitializationEvent event){
		ItemCore.register(event);
	}

	public static void RegisterEntity(CommonProxy proxy){
		// タイルエンティティはserverとclientで登録方法が違う為プロキシで分ける
		proxy.registerTileEntity();
	}

	public static void RegisterRender(CommonProxy proxy){
		// レンダーはクライアントの未登録
		proxy.registerRender();
	}

	public static void RegisterRecipe(){

	}

	public static void RegisterMessage(){
		Mod_ConvenienceBox.Net_Instance.registerMessage(MessageExpBank_ExecExperience.class, MessageExpBank_ExecExperience.class, ModCommon.MESSID_MESSAGEECECECPERIENCE, Side.SERVER);
		Mod_ConvenienceBox.Net_Instance.registerMessage(MessageExpBank_ExperienceInfo.class, MessageExpBank_ExperienceInfo.class, ModCommon.MESSID_MESSAGEEXPERIENCEINFO, Side.CLIENT);
		Mod_ConvenienceBox.Net_Instance.registerMessage(MessageExEnchant_UpdateParameter.class, MessageExEnchant_UpdateParameter.class, ModCommon.MESSID_MESSAGEUPDATEENCHANT, Side.SERVER);
		Mod_ConvenienceBox.Net_Instance.registerMessage(MessageExEnchant_ClearParameter.class, MessageExEnchant_ClearParameter.class, ModCommon.MESSID_MESSAGECLEARENCHANT, Side.CLIENT);
		Mod_ConvenienceBox.Net_Instance.registerMessage(MessageExEnchant_ExecEnchant.class, MessageExEnchant_ExecEnchant.class, ModCommon.MESSID_MESSAGEEXECENCHANT, Side.SERVER);
		Mod_ConvenienceBox.Net_Instance.registerMessage(MessageFarmer_UpdateDelivery.class,MessageFarmer_UpdateDelivery.class, ModCommon.MESSID_MESSAGEFARMERUPDATE, Side.SERVER);
		Mod_ConvenienceBox.Net_Instance.registerMessage(MessageKiller_UpdateAreaSize.class,MessageKiller_UpdateAreaSize.class, ModCommon.MESSID_MESSAGEKILLAREAUPDATE, Side.SERVER);
		Mod_ConvenienceBox.Net_Instance.registerMessage(MessageKiller_UpdateTarget.class,MessageKiller_UpdateTarget.class, ModCommon.MESSID_MESSAGEKILLTARGETUPDATE, Side.SERVER);
		Mod_ConvenienceBox.Net_Instance.registerMessage(MessageEXPCollector_LevelUp.class,MessageEXPCollector_LevelUp.class, ModCommon.MESSID_MESSAGELEVELUP, Side.SERVER);
		Mod_ConvenienceBox.Net_Instance.registerMessage(MessageCrusher_ChangeSelect.class,MessageCrusher_ChangeSelect.class, ModCommon.MESSID_MESSAGECRUSHERSELECTCHANGE, Side.SERVER);
		Mod_ConvenienceBox.Net_Instance.registerMessage(MessageStraw_AreaSizeUpdate.class,MessageStraw_AreaSizeUpdate.class, ModCommon.MESSID_MESSAGESTRAWAREAPUDATE, Side.SERVER);
		Mod_ConvenienceBox.Net_Instance.registerMessage(MessageStraw_GetAll.class,MessageStraw_GetAll.class,ModCommon.MESSID_MESSAGESTRAWGETALL, Side.SERVER);
		Mod_ConvenienceBox.Net_Instance.registerMessage(MessageStraw_ClearArea.class,MessageStraw_ClearArea.class,ModCommon.MESSID_MESSAGESTRAWCLEAR, Side.SERVER);
		Mod_ConvenienceBox.Net_Instance.registerMessage(Message_BoxSwitchChange.class,Message_BoxSwitchChange.class,ModCommon.MESSID_MESSAGEBOXPOWER, Side.SERVER);
		Mod_ConvenienceBox.Net_Instance.registerMessage(Message_ResetWork.class,Message_ResetWork.class,ModCommon.MESSID_MESSAGERESET, Side.SERVER);
		Mod_ConvenienceBox.Net_Instance.registerMessage(Message_UpdateDestroy.class,Message_UpdateDestroy.class,ModCommon.MESSID_MESSAGEUPDATEDESTROY, Side.SERVER);
	}


}
