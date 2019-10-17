package mod.cvbox.network;

import java.util.function.Supplier;

import mod.cvbox.inventory.farm.ContainerWoolCutting;
import mod.cvbox.tileentity.farm.TileEntityWoolCutting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageWoolCutting_AreaSizeUpdate {

	private int range_x;
	private int range_z;

	public MessageWoolCutting_AreaSizeUpdate(){}

	public MessageWoolCutting_AreaSizeUpdate(int x, int z){
		range_x = x;
		range_z = z;
	}

	public static void encode(MessageWoolCutting_AreaSizeUpdate pkt, PacketBuffer buf) {
		buf.writeInt(pkt.range_x);
		buf.writeInt(pkt.range_z);
	}

	public static MessageWoolCutting_AreaSizeUpdate decode(PacketBuffer buf) {
		return new MessageWoolCutting_AreaSizeUpdate(buf.readInt(),buf.readInt());
	}

	public static class Handler {
		public static void handle(final MessageWoolCutting_AreaSizeUpdate pkt, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				PlayerEntity player = ctx.get().getSender();
				if ( player.openContainer instanceof ContainerWoolCutting){
					TileEntityWoolCutting entity = ((ContainerWoolCutting)player.openContainer).getTileEntity();
					int rx = entity.getField(TileEntityWoolCutting.FIELD_RANGEX) + pkt.range_x;
					int rz = entity.getField(TileEntityWoolCutting.FIELD_RANGEZ) + pkt.range_z;
					if (rx < 0){rx = 0;}
					if (rx > 255){rx = 255;}
					if (rz < 0){rz=0;}
					if (rz > 255){rz  = 255;}
					entity.setField(TileEntityWoolCutting.FIELD_RANGEX,rx);
					entity.setField(TileEntityWoolCutting.FIELD_RANGEZ,rz);
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
}
