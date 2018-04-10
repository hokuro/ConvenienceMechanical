package mod.cvbox.network;

import org.apache.commons.lang3.BooleanUtils;

import io.netty.buffer.ByteBuf;
import mod.cvbox.inventory.ContainerAutoHarvest;
import mod.cvbox.inventory.ContainerAutoPlanting;
import mod.cvbox.inventory.ContainerWoodHarvester;
import mod.cvbox.inventory.ContainerWoodPlanter;
import mod.cvbox.tileentity.TileEntityHarvester;
import mod.cvbox.tileentity.TileEntityPlanter;
import mod.cvbox.tileentity.TileEntityWoodHarvester;
import mod.cvbox.tileentity.TileEntityWoodPlanter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageFarmer_UpdateDelivery implements IMessage, IMessageHandler<MessageFarmer_UpdateDelivery, IMessage> {
	private BlockPos pos;
	private Boolean deliver;

	public MessageFarmer_UpdateDelivery(){}

	public MessageFarmer_UpdateDelivery(BlockPos posIn, Boolean deliver){
		pos = posIn;
		this.deliver = deliver;
	}

	public boolean Deliver(){return deliver;}

	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(),
				buf.readInt(),
				buf.readInt());
		deliver = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeBoolean(deliver);
	}

	@Override
	public IMessage onMessage(MessageFarmer_UpdateDelivery message, MessageContext ctx){
		EntityPlayer player = ctx.getServerHandler().player;
		if ( player.openContainer instanceof ContainerAutoPlanting){
			((ContainerAutoPlanting)player.openContainer).setDeliverMode(message.deliver);
			World world = ((ContainerAutoPlanting)player.openContainer).getWorld();
			TileEntity ent = world.getTileEntity(message.pos);
			if ( ent instanceof TileEntityPlanter){
				((TileEntityPlanter)ent).setField(1, BooleanUtils.toInteger(message.deliver));
			}
		}else if (player.openContainer instanceof ContainerAutoHarvest){
			((ContainerAutoHarvest)player.openContainer).setDeliverMode(message.deliver);
			World world = ((ContainerAutoHarvest)player.openContainer).getWorld();
			TileEntity ent = world.getTileEntity(message.pos);
			if ( ent instanceof TileEntityHarvester){
				((TileEntityHarvester)ent).setField(1, BooleanUtils.toInteger(message.deliver));
			}
		}else if (player.openContainer instanceof ContainerWoodPlanter){
			((ContainerWoodPlanter)player.openContainer).setDeliverMode(message.deliver);
			World world = ((ContainerWoodPlanter)player.openContainer).getWorld();
			TileEntity ent = world.getTileEntity(message.pos);
			if ( ent instanceof TileEntityWoodPlanter){
				((TileEntityWoodPlanter)ent).setField(1, BooleanUtils.toInteger(message.deliver));
			}
		}else if (player.openContainer instanceof ContainerWoodHarvester){
			((ContainerWoodHarvester)player.openContainer).setDeliverMode(message.deliver);
			World world = ((ContainerWoodHarvester)player.openContainer).getWorld();
			TileEntity ent = world.getTileEntity(message.pos);
			if ( ent instanceof TileEntityWoodHarvester){
				((TileEntityWoodHarvester)ent).setField(1, BooleanUtils.toInteger(message.deliver));
			}
		}

		return null;
	}
}
