package mod.cvbox.network;

import io.netty.buffer.ByteBuf;
import mod.cvbox.gui.GuiExpBank;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class Message1 implements IMessage, IMessageHandler<Message1, IMessage> {
	private int player_exp;
	private int box_exp;

	public Message1(){

	}

	public Message1(int player_exp, int box_exp){
		this.player_exp = player_exp;
		this.box_exp = box_exp;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		// TODO 自動生成されたメソッド・スタブ
		player_exp = buf.readInt();
		box_exp = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// TODO 自動生成されたメソッド・スタブ
		buf.writeInt(player_exp);
		buf.writeInt(box_exp);
	}

	public IMessage onMessage(Message1 message, MessageContext ctx){
		GuiExpBank.getPacket(FMLClientHandler.instance().getClient().thePlayer.getPersistentID().toString(),message.box_exp, message.player_exp);
		return null;
	}

}
