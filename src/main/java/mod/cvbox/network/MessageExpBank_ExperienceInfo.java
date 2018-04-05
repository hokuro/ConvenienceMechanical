package mod.cvbox.network;

import io.netty.buffer.ByteBuf;
import mod.cvbox.gui.GuiExpBank;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageExpBank_ExperienceInfo implements IMessage, IMessageHandler<MessageExpBank_ExperienceInfo, IMessage> {
	private int player_exp;
	private int box_exp;

	public MessageExpBank_ExperienceInfo(){

	}

	public MessageExpBank_ExperienceInfo(int player_exp, int box_exp){
		this.player_exp = player_exp;
		this.box_exp = box_exp;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		player_exp = buf.readInt();
		box_exp = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(player_exp);
		buf.writeInt(box_exp);
	}

	public IMessage onMessage(MessageExpBank_ExperienceInfo message, MessageContext ctx){
		GuiExpBank.getPacket(FMLClientHandler.instance().getClient().player.getPersistentID().toString(),message.box_exp, message.player_exp);
		return null;
	}

}
