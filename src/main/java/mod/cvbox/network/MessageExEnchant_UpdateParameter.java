package mod.cvbox.network;

import java.util.function.Supplier;

import mod.cvbox.inventory.ContainerExEnchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageExEnchant_UpdateParameter{

	private int enc_index;
	private int enc_level;

	public MessageExEnchant_UpdateParameter(){}

	public MessageExEnchant_UpdateParameter(int idx, int level){
		enc_index = idx;
		enc_level = level;
	}

	public int getIndex(){return enc_index;}
	public int getLevel(){return enc_level;}

	public static void encode(MessageExEnchant_UpdateParameter pkt, PacketBuffer buf)
	{
		buf.writeInt(pkt.enc_index);
		buf.writeInt(pkt.enc_level);
	}

	public static MessageExEnchant_UpdateParameter decode(PacketBuffer buf)
	{
		return new MessageExEnchant_UpdateParameter(buf.readInt(),buf.readInt());
	}

	public static class Handler
	{
		public static void handle(final MessageExEnchant_UpdateParameter pkt, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				EntityPlayer player = ctx.get().getSender();
				if (player != null){
					ContainerExEnchantment.updateEnchantment(pkt.getIndex(),pkt.getLevel(),player);
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}

}