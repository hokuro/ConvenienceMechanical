package mod.cvbox.network;

import io.netty.buffer.ByteBuf;
import mod.cvbox.inventory.ContainerExEnchantment;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageExEnchant_UpdateParameter implements IMessage, IMessageHandler<MessageExEnchant_UpdateParameter, IMessage> {

	private int enc_index;
	private int enc_level;

	public MessageExEnchant_UpdateParameter(){}

	public MessageExEnchant_UpdateParameter(int idx, int level){
		enc_index = idx;
		enc_level = level;
	}

	public int getIndex(){return enc_index;}
	public int getLevel(){return enc_level;}

	@Override
	public void fromBytes(ByteBuf buf) {
		enc_index = buf.readInt();
		enc_level = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(enc_index);
		buf.writeInt(enc_level);
	}

	@Override
	public IMessage onMessage(MessageExEnchant_UpdateParameter message, MessageContext ctx){
		EntityPlayerMP entityPlayer = ctx.getServerHandler().player;
		if (entityPlayer != null){
			ContainerExEnchantment.updateEnchantment(message.getIndex(),message.getLevel(),entityPlayer);
		}
		return null;
	}
}