package mod.cvbox.network;

import io.netty.buffer.ByteBuf;
import mod.cvbox.inventory.ContainerKiller;
import mod.cvbox.tileentity.TileEntityKiller;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageKiller_UpdateTarget implements IMessage, IMessageHandler<MessageKiller_UpdateTarget, IMessage> {

	String entityName;

	public MessageKiller_UpdateTarget(){}

	public MessageKiller_UpdateTarget(String name){
		entityName = name;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int len = buf.readInt();
		byte[] name = new byte[len];
		buf.readBytes(name);
		entityName = new String(name);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		byte[] name = entityName.getBytes();
		buf.writeInt(name.length);
		buf.writeBytes(name);
	}

	@Override
	public IMessage onMessage(MessageKiller_UpdateTarget message, MessageContext ctx){
		EntityPlayer player = ctx.getServerHandler().player;
		if ( player.openContainer instanceof ContainerKiller){
			TileEntityKiller killer = ((ContainerKiller)player.openContainer).getTileEntity();
			killer.updateTarget(message.entityName);
		}
		return null;
	}
}
