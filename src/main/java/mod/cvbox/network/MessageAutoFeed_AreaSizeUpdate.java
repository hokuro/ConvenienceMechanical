package mod.cvbox.network;

import java.util.function.Supplier;

import mod.cvbox.inventory.farm.ContainerAutoFeed;
import mod.cvbox.tileentity.farm.TileEntityAutoFeed;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageAutoFeed_AreaSizeUpdate {

	private int range_x;
	private int range_z;

	public MessageAutoFeed_AreaSizeUpdate(){}

	public MessageAutoFeed_AreaSizeUpdate(int x, int z){
		range_x = x;
		range_z = z;
	}

	public static void encode(MessageAutoFeed_AreaSizeUpdate pkt, PacketBuffer buf) {
		buf.writeInt(pkt.range_x);
		buf.writeInt(pkt.range_z);
	}

	public static MessageAutoFeed_AreaSizeUpdate decode(PacketBuffer buf) {
		return new MessageAutoFeed_AreaSizeUpdate(buf.readInt(),buf.readInt());
	}

	public static class Handler {
		public static void handle(final MessageAutoFeed_AreaSizeUpdate pkt, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				PlayerEntity player = ctx.get().getSender();
				if ( player.openContainer instanceof ContainerAutoFeed){
					TileEntityAutoFeed entity = ((ContainerAutoFeed)player.openContainer).getTileEntity();
					int rx = entity.getField(TileEntityAutoFeed.FIELD_RANGEX) + pkt.range_x;
					int rz = entity.getField(TileEntityAutoFeed.FIELD_RANGEZ) + pkt.range_z;
					if (rx < 0){rx = 0;}
					if (rx > 255){rx = 255;}
					if (rz < 0){rz=0;}
					if (rz > 255){rz  = 255;}
					entity.setField(TileEntityAutoFeed.FIELD_RANGEX,rx);
					entity.setField(TileEntityAutoFeed.FIELD_RANGEZ,rz);
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
}
