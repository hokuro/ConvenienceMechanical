package mod.cvbox.network;

import java.util.function.Supplier;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.inventory.ContainerAutoHarvest;
import mod.cvbox.inventory.ContainerAutoPlanting;
import mod.cvbox.inventory.ContainerWoodHarvester;
import mod.cvbox.inventory.ContainerWoodPlanter;
import mod.cvbox.tileentity.TileEntityHarvester;
import mod.cvbox.tileentity.TileEntityPlanter;
import mod.cvbox.tileentity.TileEntityWoodHarvester;
import mod.cvbox.tileentity.TileEntityWoodPlanter;
import net.minecraft.entity.player.EntityPlayer;
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
				EntityPlayer player = ctx.get().getSender();
				if ( player.openContainer instanceof ContainerAutoPlanting){
					((ContainerAutoPlanting)player.openContainer).setDeliverMode(pkt.deliver);
					World world = ((ContainerAutoPlanting)player.openContainer).getWorld();
					TileEntity ent = world.getTileEntity(pkt.pos);
					if ( ent instanceof TileEntityPlanter){
						((TileEntityPlanter)ent).setField(1, BooleanUtils.toInteger(pkt.deliver));
					}
				}else if (player.openContainer instanceof ContainerAutoHarvest){
					((ContainerAutoHarvest)player.openContainer).setDeliverMode(pkt.deliver);
					World world = ((ContainerAutoHarvest)player.openContainer).getWorld();
					TileEntity ent = world.getTileEntity(pkt.pos);
					if ( ent instanceof TileEntityHarvester){
						((TileEntityHarvester)ent).setField(1, BooleanUtils.toInteger(pkt.deliver));
					}
				}else if (player.openContainer instanceof ContainerWoodPlanter){
					((ContainerWoodPlanter)player.openContainer).setDeliverMode(pkt.deliver);
					World world = ((ContainerWoodPlanter)player.openContainer).getWorld();
					TileEntity ent = world.getTileEntity(pkt.pos);
					if ( ent instanceof TileEntityWoodPlanter){
						((TileEntityWoodPlanter)ent).setField(1, BooleanUtils.toInteger(pkt.deliver));
					}
				}else if (player.openContainer instanceof ContainerWoodHarvester){
					((ContainerWoodHarvester)player.openContainer).setDeliverMode(pkt.deliver);
					World world = ((ContainerWoodHarvester)player.openContainer).getWorld();
					TileEntity ent = world.getTileEntity(pkt.pos);
					if ( ent instanceof TileEntityWoodHarvester){
						((TileEntityWoodHarvester)ent).setField(1, BooleanUtils.toInteger(pkt.deliver));
					}
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
}
