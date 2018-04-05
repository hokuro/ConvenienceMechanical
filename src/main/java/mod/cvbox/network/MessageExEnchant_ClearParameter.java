package mod.cvbox.network;

import io.netty.buffer.ByteBuf;
import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.inventory.ContainerExEnchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageExEnchant_ClearParameter implements IMessage, IMessageHandler<MessageExEnchant_ClearParameter, IMessage> {

	public MessageExEnchant_ClearParameter(){}


	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	@Override
	public IMessage onMessage(MessageExEnchant_ClearParameter message, MessageContext ctx){
        EntityPlayer player = Mod_ConvenienceBox.proxy.getEntityPlayerInstance();
		if (player != null){
			ContainerExEnchantment.updateEnchantment(-1,0,player);
		}
		return null;
	}

}