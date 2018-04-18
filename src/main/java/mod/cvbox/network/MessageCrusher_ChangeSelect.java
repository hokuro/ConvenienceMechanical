package mod.cvbox.network;

import io.netty.buffer.ByteBuf;
import mod.cvbox.inventory.ContainerCrusher;
import mod.cvbox.tileentity.TileEntityCrusher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageCrusher_ChangeSelect  implements IMessage, IMessageHandler<MessageCrusher_ChangeSelect, IMessage> {

	private int select;

	public MessageCrusher_ChangeSelect(){}

	public MessageCrusher_ChangeSelect(int sel){
		this.select = sel;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		select = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(select);
	}

	@Override
	public IMessage onMessage(MessageCrusher_ChangeSelect message, MessageContext ctx){
		EntityPlayer player = ctx.getServerHandler().player;
		if ( player.openContainer instanceof ContainerCrusher){
			TileEntityCrusher crusher = ((ContainerCrusher)player.openContainer).getTileEntity();
			crusher.setField(TileEntityCrusher.FIELD_SELECT, message.select);
		}
		return null;
	}
}