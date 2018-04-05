package mod.cvbox.network;

import io.netty.buffer.ByteBuf;
import mod.cvbox.inventory.ContainerExEnchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageExEnchant_ExecEnchant  implements IMessage, IMessageHandler<MessageExEnchant_ExecEnchant, IMessage> {

	public MessageExEnchant_ExecEnchant(){}


	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	@Override
	public IMessage onMessage(MessageExEnchant_ExecEnchant message, MessageContext ctx){
        EntityPlayer player = ctx.getServerHandler().player;
		if (player != null){
			ContainerExEnchantment.execEnchant(player);
		}
		return null;
	}

}
