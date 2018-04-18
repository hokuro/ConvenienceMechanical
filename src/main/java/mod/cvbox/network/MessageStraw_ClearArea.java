package mod.cvbox.network;

import io.netty.buffer.ByteBuf;
import mod.cvbox.inventory.ContainerStraw;
import mod.cvbox.tileentity.TileEntityStraw;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageStraw_ClearArea implements IMessage, IMessageHandler<MessageStraw_ClearArea, IMessage> {

	public MessageStraw_ClearArea(){}


	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	@Override
	public IMessage onMessage(MessageStraw_ClearArea message, MessageContext ctx){
		EntityPlayer player = ctx.getServerHandler().player;
		if ( player.openContainer instanceof ContainerStraw){
			TileEntityStraw straw = ((ContainerStraw)player.openContainer).getTileEntity();
			straw.resetSearchArea();
		}
		return null;
	}
}