package mod.cvbox.network;

import java.util.function.Supplier;

import mod.cvbox.inventory.ContainerExEnchantment;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageExEnchant_ClearParameter {

	public MessageExEnchant_ClearParameter(){}


	public static void encode(MessageExEnchant_ClearParameter pkt, PacketBuffer buf)
	{
	}

	public static MessageExEnchant_ClearParameter decode(PacketBuffer buf)
	{
		return new MessageExEnchant_ClearParameter();
	}

	public static class Handler
	{
		public static void handle(final MessageExEnchant_ClearParameter pkt, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				EntityPlayer player = Minecraft.getInstance().player;
				if (player != null){
					ContainerExEnchantment.updateEnchantment(-1,0,player);
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
}