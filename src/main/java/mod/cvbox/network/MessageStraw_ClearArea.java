package mod.cvbox.network;

import java.util.function.Supplier;

import mod.cvbox.inventory.ContainerStraw;
import mod.cvbox.tileentity.TileEntityStraw;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageStraw_ClearArea {

	public MessageStraw_ClearArea(){}


	public static void encode(MessageStraw_ClearArea pkt, PacketBuffer buf)
	{
	}

	public static MessageStraw_ClearArea decode(PacketBuffer buf)
	{
		return new MessageStraw_ClearArea();
	}

	public static class Handler
	{
		public static void handle(final MessageStraw_ClearArea pkt, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				EntityPlayer player = ctx.get().getSender();
				if ( player.openContainer instanceof ContainerStraw){
					TileEntityStraw straw = ((ContainerStraw)player.openContainer).getTileEntity();
					straw.resetSearchArea();
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
}