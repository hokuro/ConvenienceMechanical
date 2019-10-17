package mod.cvbox.network;

import java.util.function.Supplier;

import mod.cvbox.gui.work.GuiExpBank;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageExpBank_ExperienceInfo {
	private int player_exp;
	private int box_exp;

	public MessageExpBank_ExperienceInfo(){

	}

	public MessageExpBank_ExperienceInfo(int player_exp, int box_exp){
		this.player_exp = player_exp;
		this.box_exp = box_exp;
	}

	public static void encode(MessageExpBank_ExperienceInfo pkt, PacketBuffer buf)
	{
		buf.writeInt(pkt.player_exp);
		buf.writeInt(pkt.box_exp);
	}

	public static MessageExpBank_ExperienceInfo decode(PacketBuffer buf)
	{
		int pexp = buf.readInt();
		int bexp = buf.readInt();
		return new MessageExpBank_ExperienceInfo(pexp,bexp);
	}

	public static class Handler
	{
		public static void handle(final MessageExpBank_ExperienceInfo pkt, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				String uname = Minecraft.getInstance().player.getUniqueID().toString();
				GuiExpBank.getPacket(uname, pkt.box_exp, pkt.player_exp);
			});
			ctx.get().setPacketHandled(true);
		}
	}
}
