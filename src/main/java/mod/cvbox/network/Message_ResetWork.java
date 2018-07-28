package mod.cvbox.network;

import org.apache.commons.lang3.BooleanUtils;

import io.netty.buffer.ByteBuf;
import mod.cvbox.core.log.ModLog;
import mod.cvbox.inventory.IPowerSwitchContainer;
import mod.cvbox.tileentity.TileEntityHarvester;
import mod.cvbox.tileentity.TileEntityPlanter;
import mod.cvbox.tileentity.TileEntityWoodHarvester;
import mod.cvbox.tileentity.TileEntityWoodPlanter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class Message_ResetWork implements IMessage, IMessageHandler<Message_ResetWork, IMessage> {

	@Override
	public void fromBytes(ByteBuf buf) {
		nextValue = BooleanUtils.toBoolean(buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(BooleanUtils.toInteger(nextValue));
	}


	private boolean nextValue;

	public Message_ResetWork(){}

	@Override
	public IMessage onMessage(Message_ResetWork message, MessageContext ctx){
		EntityPlayer player = ctx.getServerHandler().player;
		try{
			Class<?> cls = player.openContainer.getClass();
			TileEntity te = ((IPowerSwitchContainer)player.openContainer).getTileEntity();
			if (te instanceof TileEntityHarvester){
				((TileEntityHarvester)te).reset();
			}else if (te instanceof TileEntityPlanter){
				((TileEntityPlanter)te).reset();
			}else if (te instanceof TileEntityWoodHarvester){
				((TileEntityWoodHarvester)te).reset();
			}else if (te instanceof TileEntityWoodPlanter){
				((TileEntityWoodPlanter)te).reset();
			}
		}catch(Exception ex){
			ModLog.log().fatal(ex.getMessage());
		}
		return null;
	}
}