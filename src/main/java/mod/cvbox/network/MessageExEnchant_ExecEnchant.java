package mod.cvbox.network;

import java.util.function.Supplier;

import mod.cvbox.inventory.work.ContainerExEnchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageExEnchant_ExecEnchant {

	public MessageExEnchant_ExecEnchant(){}


	public static void encode(MessageExEnchant_ExecEnchant pkt, PacketBuffer buf)
	{
	}

	public static MessageExEnchant_ExecEnchant decode(PacketBuffer buf)
	{
		return new MessageExEnchant_ExecEnchant();
	}

	public static class Handler
	{
		public static void handle(final MessageExEnchant_ExecEnchant pkt, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				PlayerEntity player = ctx.get().getSender();
				if (player != null){
					ContainerExEnchantment.execEnchant(player);
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
}
