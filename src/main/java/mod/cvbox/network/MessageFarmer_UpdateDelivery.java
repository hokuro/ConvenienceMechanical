package mod.cvbox.network;

import java.util.function.Supplier;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.inventory.farm.ContainerPlantNurture;
import mod.cvbox.tileentity.ab.TileEntityPlantNurtureBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageFarmer_UpdateDelivery {
	private BlockPos pos;
	private Boolean deliver;

	public MessageFarmer_UpdateDelivery(){}

	public MessageFarmer_UpdateDelivery(BlockPos posIn, Boolean deliver){
		pos = posIn;
		this.deliver = deliver;
	}

	public boolean Deliver(){return deliver;}

	public static void encode(MessageFarmer_UpdateDelivery pkt, PacketBuffer buf)
	{
		buf.writeInt(pkt.pos.getX());
		buf.writeInt(pkt.pos.getY());
		buf.writeInt(pkt.pos.getZ());
		buf.writeBoolean(pkt.deliver);

	}

	public static MessageFarmer_UpdateDelivery decode(PacketBuffer buf)
	{
		BlockPos p = new BlockPos(buf.readInt(),buf.readInt(),buf.readInt());
		return new MessageFarmer_UpdateDelivery(p,buf.readBoolean());
	}

	public static class Handler
	{
		public static void handle(final MessageFarmer_UpdateDelivery pkt, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				PlayerEntity player = ctx.get().getSender();
				if ( player.openContainer instanceof ContainerPlantNurture){
					((ContainerPlantNurture)player.openContainer).setDeliverMode(pkt.deliver);
					World world = ((ContainerPlantNurture)player.openContainer).getWorld();
					TileEntity ent = world.getTileEntity(pkt.pos);
					if ( ent instanceof TileEntityPlantNurtureBase){
						((TileEntityPlantNurtureBase)ent).setField(1, BooleanUtils.toInteger(pkt.deliver));
					}
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
}
