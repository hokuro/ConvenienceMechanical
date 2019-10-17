package mod.cvbox.network;

import java.util.function.Supplier;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.inventory.factory.ContainerDeliverBox;
import mod.cvbox.tileentity.factory.TileEntityDeliverBox;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class Message_DeliverBox_ModeChange {

	public Message_DeliverBox_ModeChange(){
	}

	public static void encode(Message_DeliverBox_ModeChange pkt, PacketBuffer buf) {
	}

	public static Message_DeliverBox_ModeChange decode(PacketBuffer buf) {
		return new Message_DeliverBox_ModeChange();
	}

	public static class Handler {
		public static void handle(final Message_DeliverBox_ModeChange pkt, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				PlayerEntity player = ctx.get().getSender();
				if ( player.openContainer instanceof ContainerDeliverBox){
					TileEntityDeliverBox entity = ((ContainerDeliverBox)player.openContainer).getTileEntity();
					boolean v = BooleanUtils.toBoolean(entity.getField(TileEntityDeliverBox.FIELD_ISMATCH));
					entity.setField(TileEntityDeliverBox.FIELD_ISMATCH, BooleanUtils.toInteger(!v));
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
}