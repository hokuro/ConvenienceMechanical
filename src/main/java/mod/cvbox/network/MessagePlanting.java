package mod.cvbox.network;

import io.netty.buffer.ByteBuf;
import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.tileentity.TileEntityPlanter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessagePlanting implements IMessage, IMessageHandler<MessagePlanting, IMessage> {

	private int x;
	private int y;
	private int z;

	public MessagePlanting(int xx, int yy, int zz){
		x = xx;
		y = yy;
		z = zz;
	}

	@Override
	public IMessage onMessage(MessagePlanting message, MessageContext ctx) {
		World world = Mod_ConvenienceBox.proxy.getClientWorld();
		TileEntity tilEntity = world.getTileEntity(new BlockPos(x,y,z));
		if ((tilEntity instanceof TileEntityPlanter)){
			TileEntityPlanter tileEntityNoop = (TileEntityPlanter) tilEntity;
		}
		return null;
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
}
