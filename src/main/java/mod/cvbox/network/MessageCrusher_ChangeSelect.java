package mod.cvbox.network;

import java.util.function.Supplier;

import mod.cvbox.inventory.factory.ContainerCrusher;
import mod.cvbox.tileentity.factory.TileEntityCrusher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageCrusher_ChangeSelect {

	private int select;

	public MessageCrusher_ChangeSelect(){}

	public MessageCrusher_ChangeSelect(int sel){
		this.select = sel;
	}


	public static void encode(MessageCrusher_ChangeSelect pkt, PacketBuffer buf)
	{
		buf.writeInt(pkt.select);
	}

	public static MessageCrusher_ChangeSelect decode(PacketBuffer buf)
	{

		return new MessageCrusher_ChangeSelect(buf.readInt());
	}

	public static class Handler
	{
		public static void handle(final MessageCrusher_ChangeSelect pkt, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				PlayerEntity player = ctx.get().getSender();
				if ( player.openContainer instanceof ContainerCrusher){
					TileEntityCrusher crusher = ((ContainerCrusher)player.openContainer).getTileEntity();
					crusher.setField(TileEntityCrusher.FIELD_SELECT, pkt.select);
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}

}
