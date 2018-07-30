package mod.cvbox.network;

import io.netty.buffer.ByteBuf;
import mod.cvbox.inventory.ContainerDestroyer;
import mod.cvbox.tileentity.TileEntityDestroyer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class Message_UpdateDestroy  implements IMessage, IMessageHandler<Message_UpdateDestroy, IMessage> {

	private int mode;
	private float time;

	public Message_UpdateDestroy(){}

	public Message_UpdateDestroy(int imode, float itime){
		mode = imode;
		time = itime;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		mode = buf.readInt();
		time = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(mode);
		buf.writeFloat(time);
	}

	@Override
	public IMessage onMessage(Message_UpdateDestroy message, MessageContext ctx){
		EntityPlayer player = ctx.getServerHandler().player;
		if ( player.openContainer instanceof ContainerDestroyer){
			TileEntityDestroyer destroyer = (TileEntityDestroyer)((ContainerDestroyer) player.openContainer).getTileEntity();
			destroyer.updateValues(message.mode,message.time);
		}
		return null;
	}
}