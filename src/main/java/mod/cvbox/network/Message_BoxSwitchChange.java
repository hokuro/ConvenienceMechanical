package mod.cvbox.network;

import java.util.function.Supplier;

import mod.cvbox.core.log.ModLog;
import mod.cvbox.inventory.IPowerSwitchContainer;
import mod.cvbox.tileentity.IPowerSwitchEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class Message_BoxSwitchChange {

	private boolean nextValue;

	public Message_BoxSwitchChange(){}
	public Message_BoxSwitchChange(boolean value){
		nextValue = value;
	}

	public static void encode(Message_BoxSwitchChange pkt, PacketBuffer buf)
	{
		buf.writeBoolean(pkt.nextValue);
	}

	public static Message_BoxSwitchChange decode(PacketBuffer buf)
	{

		return new Message_BoxSwitchChange(buf.readBoolean());
	}

	public static class Handler
	{
		public static void handle(final Message_BoxSwitchChange pkt, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				EntityPlayer player = ctx.get().getSender();
				try{
					Class<?> cls = player.openContainer.getClass();
					IPowerSwitchEntity te = (IPowerSwitchEntity)((IPowerSwitchContainer)player.openContainer).getTileEntity();
					te.setPower(pkt.nextValue);
				}catch(Exception ex){
					ModLog.log().fatal(ex.getMessage());
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}

}
