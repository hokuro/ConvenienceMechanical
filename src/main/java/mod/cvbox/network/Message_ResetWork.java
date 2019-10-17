package mod.cvbox.network;

import java.util.function.Supplier;

import mod.cvbox.core.log.ModLog;
import mod.cvbox.inventory.IPowerSwitchContainer;
import mod.cvbox.tileentity.ab.TileEntityPlantNurtureBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.network.NetworkEvent;

public class Message_ResetWork {


	public Message_ResetWork(){}

	public static void encode(Message_ResetWork pkt, PacketBuffer buf)
	{
	}

	public static Message_ResetWork decode(PacketBuffer buf)
	{

		return new Message_ResetWork();
	}

	public static class Handler
	{
		public static void handle(final Message_ResetWork pkt, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				PlayerEntity player = ctx.get().getSender();
				try{
					Class<?> cls = player.openContainer.getClass();
					TileEntity te = ((IPowerSwitchContainer)player.openContainer).getTileEntity();
					if (te instanceof TileEntityPlantNurtureBase){
						((TileEntityPlantNurtureBase)te).reset();
					}
				}catch(Exception ex){
					ModLog.log().fatal(ex.getMessage());
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
}
