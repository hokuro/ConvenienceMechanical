package mod.cvbox.network;

import org.apache.commons.lang3.BooleanUtils;

import io.netty.buffer.ByteBuf;
import mod.cvbox.core.log.ModLog;
import mod.cvbox.inventory.IPowerSwitchContainer;
import mod.cvbox.tileentity.IPowerSwitchEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class Message_BoxSwitchChange implements IMessage, IMessageHandler<Message_BoxSwitchChange, IMessage> {

	@Override
	public void fromBytes(ByteBuf buf) {
		nextValue = BooleanUtils.toBoolean(buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(BooleanUtils.toInteger(nextValue));
	}


	private boolean nextValue;

	public Message_BoxSwitchChange(){}
	public Message_BoxSwitchChange(boolean value){
		nextValue = value;
	}

	@Override
	public IMessage onMessage(Message_BoxSwitchChange message, MessageContext ctx){
		EntityPlayer player = ctx.getServerHandler().player;
		try{
			Class<?> cls = player.openContainer.getClass();
			IPowerSwitchEntity te = (IPowerSwitchEntity)((IPowerSwitchContainer)player.openContainer).getTileEntity();
			te.setPower(message.nextValue);
		}catch(Exception ex){
			ModLog.log().fatal(ex.getMessage());
		}
		return null;
	}
}
