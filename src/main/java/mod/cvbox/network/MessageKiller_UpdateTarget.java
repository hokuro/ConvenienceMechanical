package mod.cvbox.network;

import java.util.function.Supplier;

import mod.cvbox.inventory.ContainerKiller;
import mod.cvbox.tileentity.TileEntityKiller;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageKiller_UpdateTarget {

	String entityName;

	public MessageKiller_UpdateTarget(){}

	public MessageKiller_UpdateTarget(String name){
		entityName = name;
	}

	public static void encode(MessageKiller_UpdateTarget pkt, PacketBuffer buf)
	{
		buf.writeInt(pkt.entityName.length());
		buf.writeString(pkt.entityName);

	}

	public static MessageKiller_UpdateTarget decode(PacketBuffer buf)
	{
		int len = buf.readInt();
		return new MessageKiller_UpdateTarget(buf.readString(len));
	}

	public static class Handler
	{
		public static void handle(final MessageKiller_UpdateTarget pkt, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				EntityPlayer player = ctx.get().getSender();
				if ( player.openContainer instanceof ContainerKiller){
					TileEntityKiller killer = ((ContainerKiller)player.openContainer).getTileEntity();
					killer.updateTarget(pkt.entityName);
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
}
