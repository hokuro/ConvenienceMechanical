package mod.cvbox.network;

import java.util.function.Supplier;

import mod.cvbox.inventory.ContainerKiller;
import mod.cvbox.tileentity.TileEntityKiller;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageKiller_UpdateAreaSize {

	private int x;
	private int y;
	private int z;

	public MessageKiller_UpdateAreaSize(){}

	public MessageKiller_UpdateAreaSize(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static void encode(MessageKiller_UpdateAreaSize pkt, PacketBuffer buf)
	{
		buf.writeInt(pkt.x);
		buf.writeInt(pkt.y);
		buf.writeInt(pkt.z);

	}

	public static MessageKiller_UpdateAreaSize decode(PacketBuffer buf)
	{
		return new MessageKiller_UpdateAreaSize(buf.readInt(),buf.readInt(),buf.readInt());
	}

	public static class Handler
	{
		public static void handle(final MessageKiller_UpdateAreaSize pkt, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				EntityPlayer player = ctx.get().getSender();
				if ( player.openContainer instanceof ContainerKiller){
					TileEntityKiller killer = ((ContainerKiller)player.openContainer).getTileEntity();
					killer.setField(TileEntityKiller.FIELD_AREASIZEX, pkt.x);
					killer.setField(TileEntityKiller.FIELD_AREASIZEY, pkt.y);
					killer.setField(TileEntityKiller.FIELD_AREASIZEZ, pkt.z);
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
}
