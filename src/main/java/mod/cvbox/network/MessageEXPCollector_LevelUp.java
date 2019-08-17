package mod.cvbox.network;

import java.util.function.Supplier;

import mod.cvbox.inventory.ContainerExpCollector;
import mod.cvbox.tileentity.TileEntityExpCollector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageEXPCollector_LevelUp {
	private int exp;

	public MessageEXPCollector_LevelUp(){}

	public MessageEXPCollector_LevelUp(int exp){
		this.exp = exp;
	}

	public static void encode(MessageEXPCollector_LevelUp pkt, PacketBuffer buf)
	{
		buf.writeInt(pkt.exp);

	}

	public static MessageEXPCollector_LevelUp decode(PacketBuffer buf)
	{
		return new MessageEXPCollector_LevelUp(buf.readInt());
	}

	public static class Handler
	{
		public static void handle(final MessageEXPCollector_LevelUp pkt, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				EntityPlayer player = ctx.get().getSender();
				if (player.openContainer instanceof ContainerExpCollector){
					TileEntityExpCollector collector = ((ContainerExpCollector)player.openContainer).getTileEntity();
					if (pkt.exp < 0){
						player.giveExperiencePoints(collector.getField(TileEntityExpCollector.FIELD_EXPVALUE));
						collector.setField(TileEntityExpCollector.FIELD_EXPVALUE,0);
					}else{
						for ( int i = 0; i < pkt.exp; i++){
							int value = collector.getField(TileEntityExpCollector.FIELD_EXPVALUE)-player.xpBarCap();
							if (value >= 0){
								player.addExperienceLevel(1);
								collector.setField(TileEntityExpCollector.FIELD_EXPVALUE,value);
							}
						}

					}
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
}
