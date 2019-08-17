package mod.cvbox.network;

import java.util.function.Supplier;

import mod.cvbox.block.BlockExpBank;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageExpBank_ExecExperience {
	private int mode;
	private int x;
	private int y;
	private int z;
	private int exp;

	public MessageExpBank_ExecExperience(){}

	public MessageExpBank_ExecExperience(int mode, int x, int y, int z, int exp){
		this.mode = mode;
		this.x = x;
		this.y = y;
		this.z = z;
		this.exp = exp;
	}

	public static void encode(MessageExpBank_ExecExperience pkt, PacketBuffer buf)
	{
		buf.writeInt(pkt.mode);
		buf.writeInt(pkt.x);
		buf.writeInt(pkt.y);
		buf.writeInt(pkt.z);
		buf.writeInt(pkt.exp);

	}

	public static MessageExpBank_ExecExperience decode(PacketBuffer buf)
	{
		return new MessageExpBank_ExecExperience(buf.readInt(),buf.readInt(),
				buf.readInt(),buf.readInt(),
				buf.readInt());
	}

	public static class Handler
	{
		public static void handle(final MessageExpBank_ExecExperience pkt, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				BlockPos pos = new BlockPos(pkt.x, pkt.y, pkt.z);
				if (ctx.get().getSender() != null){
					BlockExpBank.update_exp(ctx.get().getSender().world,  pos,  pkt.mode, pkt.exp, ctx.get().getSender());
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}


}
