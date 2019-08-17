package mod.cvbox.network;

import java.util.function.Supplier;

import mod.cvbox.inventory.ContainerStraw;
import mod.cvbox.tileentity.TileEntityStraw;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageStraw_AreaSizeUpdate {

	private int width;
	private int depth;

	public MessageStraw_AreaSizeUpdate(){}

	public MessageStraw_AreaSizeUpdate(int w, int d){
		width = w;
		depth = d;
	}

	public static void encode(MessageStraw_AreaSizeUpdate pkt, PacketBuffer buf)
	{
		buf.writeInt(pkt.width);
		buf.writeInt(pkt.depth);
	}

	public static MessageStraw_AreaSizeUpdate decode(PacketBuffer buf)
	{
		return new MessageStraw_AreaSizeUpdate(buf.readInt(),buf.readInt());
	}

	public static class Handler
	{
		public static void handle(final MessageStraw_AreaSizeUpdate pkt, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				EntityPlayer player = ctx.get().getSender();
				if ( player.openContainer instanceof ContainerStraw){
					TileEntityStraw straw = ((ContainerStraw)player.openContainer).getTileEntity();
					int wd = straw.getField(TileEntityStraw.FIELD_WIDTH) + pkt.width;
					int dp = straw.getField(TileEntityStraw.FIELD_DEPTH) + pkt.depth;
					if (wd < 0){wd = 0;}
					if (wd > 255){wd = 255;}
					if (dp < 1){dp=1;}
					if (dp > 255){dp  = 255;}
					straw.setField(TileEntityStraw.FIELD_WIDTH,wd);
					straw.setField(TileEntityStraw.FIELD_DEPTH,dp);
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
}