package mod.cvbox.network;

import mod.cvbox.core.ModCommon;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class MessageHandler {
	private static final String PROTOCOL_VERSION = Integer.toString(1);
	private static final SimpleChannel Handler = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(ModCommon.MOD_ID,ModCommon.MOD_CHANEL))
			.clientAcceptedVersions(PROTOCOL_VERSION::equals)
			.serverAcceptedVersions(PROTOCOL_VERSION::equals)
			.networkProtocolVersion(() -> PROTOCOL_VERSION)
			.simpleChannel();

	public static void register()
	{
		int disc = 0;

		Handler.registerMessage(disc++, MessageExEnchant_ClearParameter.class, MessageExEnchant_ClearParameter::encode, MessageExEnchant_ClearParameter::decode, MessageExEnchant_ClearParameter.Handler::handle);
		Handler.registerMessage(disc++, MessageExpBank_ExperienceInfo.class, MessageExpBank_ExperienceInfo::encode, MessageExpBank_ExperienceInfo::decode, MessageExpBank_ExperienceInfo.Handler::handle);

		Handler.registerMessage(disc++, MessageExpBank_ExecExperience.class, MessageExpBank_ExecExperience::encode, MessageExpBank_ExecExperience::decode, MessageExpBank_ExecExperience.Handler::handle);
		Handler.registerMessage(disc++, MessageExEnchant_UpdateParameter.class, MessageExEnchant_UpdateParameter::encode, MessageExEnchant_UpdateParameter::decode, MessageExEnchant_UpdateParameter.Handler::handle);
		Handler.registerMessage(disc++, MessageExEnchant_ExecEnchant.class, MessageExEnchant_ExecEnchant::encode, MessageExEnchant_ExecEnchant::decode, MessageExEnchant_ExecEnchant.Handler::handle);
		Handler.registerMessage(disc++, MessageFarmer_UpdateDelivery.class, MessageFarmer_UpdateDelivery::encode, MessageFarmer_UpdateDelivery::decode, MessageFarmer_UpdateDelivery.Handler::handle);
		Handler.registerMessage(disc++, MessageKiller_UpdateAreaSize.class, MessageKiller_UpdateAreaSize::encode, MessageKiller_UpdateAreaSize::decode, MessageKiller_UpdateAreaSize.Handler::handle);
		Handler.registerMessage(disc++, MessageKiller_UpdateTarget.class, MessageKiller_UpdateTarget::encode, MessageKiller_UpdateTarget::decode, MessageKiller_UpdateTarget.Handler::handle);
		Handler.registerMessage(disc++, MessageEXPCollector_LevelUp.class, MessageEXPCollector_LevelUp::encode, MessageEXPCollector_LevelUp::decode, MessageEXPCollector_LevelUp.Handler::handle);
		Handler.registerMessage(disc++, MessageCrusher_ChangeSelect.class, MessageCrusher_ChangeSelect::encode, MessageCrusher_ChangeSelect::decode, MessageCrusher_ChangeSelect.Handler::handle);
		Handler.registerMessage(disc++, MessageStraw_AreaSizeUpdate.class, MessageStraw_AreaSizeUpdate::encode, MessageStraw_AreaSizeUpdate::decode, MessageStraw_AreaSizeUpdate.Handler::handle);
		Handler.registerMessage(disc++, MessageStraw_GetAll.class, MessageStraw_GetAll::encode, MessageStraw_GetAll::decode, MessageStraw_GetAll.Handler::handle);
		Handler.registerMessage(disc++, MessageStraw_ClearArea.class, MessageStraw_ClearArea::encode, MessageStraw_ClearArea::decode, MessageStraw_ClearArea.Handler::handle);
		Handler.registerMessage(disc++, Message_BoxSwitchChange.class, Message_BoxSwitchChange::encode, Message_BoxSwitchChange::decode, Message_BoxSwitchChange.Handler::handle);
		Handler.registerMessage(disc++, Message_ResetWork.class, Message_ResetWork::encode, Message_ResetWork::decode, Message_ResetWork.Handler::handle);
		Handler.registerMessage(disc++, Message_UpdateDestroy.class, Message_UpdateDestroy::encode, Message_UpdateDestroy::decode, Message_UpdateDestroy.Handler::handle);
		Handler.registerMessage(disc++, MessageMillking_GetAll.class, MessageMillking_GetAll::encode, MessageMillking_GetAll::decode, MessageMillking_GetAll.Handler::handle);
		Handler.registerMessage(disc++, MessageMillking_AreaSizeUpdate.class, MessageMillking_AreaSizeUpdate::encode, MessageMillking_AreaSizeUpdate::decode, MessageMillking_AreaSizeUpdate.Handler::handle);
		Handler.registerMessage(disc++, MessageWoolCutting_AreaSizeUpdate.class, MessageWoolCutting_AreaSizeUpdate::encode, MessageWoolCutting_AreaSizeUpdate::decode, MessageWoolCutting_AreaSizeUpdate.Handler::handle);
		Handler.registerMessage(disc++, MessageAutoFeed_AreaSizeUpdate.class, MessageAutoFeed_AreaSizeUpdate::encode, MessageAutoFeed_AreaSizeUpdate::decode, MessageAutoFeed_AreaSizeUpdate.Handler::handle);
		Handler.registerMessage(disc++, Message_DeliverBox_ModeChange.class, Message_DeliverBox_ModeChange::encode, Message_DeliverBox_ModeChange::decode, Message_DeliverBox_ModeChange.Handler::handle);
	}

	public static void SendMessage_ExEnchant_ClearParameter(ServerPlayerEntity player){
		Handler.sendTo(new MessageExEnchant_ClearParameter(), player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
	}

	public static void SendMessage_ExpBank_ExperienceInfo(int player_exp, int box_exp, ServerPlayerEntity player) {
		// TODO 自動生成されたメソッド・スタブ
		Handler.sendTo(new MessageExpBank_ExperienceInfo(player_exp, box_exp),  player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
	}

	public static void SendMessage_RestWork() {
		Handler.sendToServer(new Message_ResetWork());

	}

	public static void SendMessage_Farmer_UpdateDelivery(BlockPos pos, boolean deliver) {
		Handler.sendToServer(new MessageFarmer_UpdateDelivery(pos, deliver));
	}

	public static void SendMessage_BoxSwitchChante(boolean flag) {
		Handler.sendToServer(new Message_BoxSwitchChange(flag));
	}

	public static void SendMessage_ChangeSelect(int sel2) {
		Handler.sendToServer(new MessageCrusher_ChangeSelect(sel2));
	}

	public static void SendMessage_UpdateDestroy(int mode, float time) {
		Handler.sendToServer(new Message_UpdateDestroy(mode,time));
	}

	public static void SendMessage_ExEnchant_UpdateParameter(int enc_index, int enc_level) {
		Handler.sendToServer(new MessageExEnchant_UpdateParameter(enc_index, enc_level));
	}

	public static void SendMessage_ExEnchant_ExEnchant() {
		Handler.sendToServer(new MessageExEnchant_ExecEnchant());
	}

	public static void SendMessage_ExpCollector_LevelUp(int input_exp) {
		Handler.sendToServer(new MessageEXPCollector_LevelUp(input_exp));
	}

	public static void sendMessage_Killer_UpdateTarget(String name) {
		Handler.sendToServer(new MessageKiller_UpdateTarget(name));
	}

	public static void SendMessage_Killer_UpdateAreaSize(int x, int y, int z) {
		Handler.sendToServer(new MessageKiller_UpdateAreaSize(x,y,z));
	}

	public static void SendMessage_Straw_CreaArea() {
		Handler.sendToServer(new MessageStraw_ClearArea());
	}

	public static void SendMessage_Straw_GetAll() {
		Handler.sendToServer(new MessageStraw_GetAll());
	}

	public static void SendMessage_Straw_AreaSizeUpdate(int w, int d) {
		Handler.sendToServer(new MessageStraw_AreaSizeUpdate(w,d));
	}

	public static void SendMessage_ExpBank_ExecExperience(int mode, int x, int y, int z, int exp) {
		Handler.sendToServer(new MessageExpBank_ExecExperience(mode,x,y,z,exp));
	}

	public static void SendMessage_Millking_GetAll() {
		Handler.sendToServer(new MessageMillking_GetAll());
	}

	public static void SendMessage_Millking_AreaSizeUpdate(int x, int z) {
		Handler.sendToServer(new MessageMillking_AreaSizeUpdate(x,z));
	}

	public static void SendMessage_WoolCutting_AreaSizeUpdate(int x, int z) {
		Handler.sendToServer(new MessageWoolCutting_AreaSizeUpdate(x,z));
	}

	public static void SendMessage_AutoFeed_AreaSizeUpdate(int x, int z) {
		Handler.sendToServer(new MessageAutoFeed_AreaSizeUpdate(x,z));
	}

	public static void SendMessage_DeliverBox_ModeChange() {
		Handler.sendToServer(new Message_DeliverBox_ModeChange());
	}

}
