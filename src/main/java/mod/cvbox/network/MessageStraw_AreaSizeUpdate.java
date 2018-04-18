package mod.cvbox.network;

import io.netty.buffer.ByteBuf;
import mod.cvbox.inventory.ContainerStraw;
import mod.cvbox.tileentity.TileEntityStraw;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageStraw_AreaSizeUpdate implements IMessage, IMessageHandler<MessageStraw_AreaSizeUpdate, IMessage> {

	private int width;
	private int depth;

	public MessageStraw_AreaSizeUpdate(){}

	public MessageStraw_AreaSizeUpdate(int w, int d){
		width = w;
		depth = d;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		width = buf.readInt();
		depth = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(width);
		buf.writeInt(depth);
	}

	@Override
	public IMessage onMessage(MessageStraw_AreaSizeUpdate message, MessageContext ctx){
		EntityPlayer player = ctx.getServerHandler().player;
		if ( player.openContainer instanceof ContainerStraw){
			TileEntityStraw straw = ((ContainerStraw)player.openContainer).getTileEntity();
			int wd = straw.getField(TileEntityStraw.FIELD_WIDTH) + message.width;
			int dp = straw.getField(TileEntityStraw.FIELD_DEPTH) + message.depth;
			if (wd < 0){wd = 0;}
			if (wd > 255){wd = 255;}
			if (dp < 1){dp=1;}
			if (dp > 255){dp  = 255;}
			straw.setField(TileEntityStraw.FIELD_WIDTH,wd);
			straw.setField(TileEntityStraw.FIELD_DEPTH,dp);
		}
		return null;
	}
}