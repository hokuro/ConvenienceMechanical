package mod.cvbox.network;

import io.netty.buffer.ByteBuf;
import mod.cvbox.inventory.ContainerKiller;
import mod.cvbox.tileentity.TileEntityKiller;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageKiller_UpdateAreaSize implements IMessage, IMessageHandler<MessageKiller_UpdateAreaSize, IMessage> {

	private int x;
	private int y;
	private int z;

	public MessageKiller_UpdateAreaSize(){}

	public MessageKiller_UpdateAreaSize(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}

	@Override
	public IMessage onMessage(MessageKiller_UpdateAreaSize message, MessageContext ctx){
		EntityPlayer player = ctx.getServerHandler().player;
		if ( player.openContainer instanceof ContainerKiller){
			TileEntityKiller killer = ((ContainerKiller)player.openContainer).getTileEntity();
			killer.setField(TileEntityKiller.FIELD_AREASIZEX, message.x);
			killer.setField(TileEntityKiller.FIELD_AREASIZEY, message.y);
			killer.setField(TileEntityKiller.FIELD_AREASIZEZ, message.z);
		}
		return null;
	}
}
