package mod.cvbox.network;

import java.util.function.Supplier;

import mod.cvbox.inventory.factory.ContainerDestroyer;
import mod.cvbox.tileentity.factory.TileEntityDestroyer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class Message_UpdateDestroy  {

	private int mode;
	private float time;

	public Message_UpdateDestroy(){}

	public Message_UpdateDestroy(int imode, float itime){
		mode = imode;
		time = itime;
	}

	public static void encode(Message_UpdateDestroy pkt, PacketBuffer buf)
	{
		buf.writeInt(pkt.mode);
		buf.writeFloat(pkt.time);
	}

	public static Message_UpdateDestroy decode(PacketBuffer buf)
	{

		return new Message_UpdateDestroy(buf.readInt(),buf.readFloat());
	}

	public static class Handler
	{
		public static void handle(final Message_UpdateDestroy pkt, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				PlayerEntity player = ctx.get().getSender();
				if ( player.openContainer instanceof ContainerDestroyer){
					TileEntityDestroyer destroyer = (TileEntityDestroyer)((ContainerDestroyer) player.openContainer).getTileEntity();
					destroyer.updateValues(pkt.mode,pkt.time);
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
}
