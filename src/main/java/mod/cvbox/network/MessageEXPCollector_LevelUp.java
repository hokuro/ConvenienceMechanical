package mod.cvbox.network;

import io.netty.buffer.ByteBuf;
import mod.cvbox.inventory.ContainerExpCollector;
import mod.cvbox.tileentity.TileEntityExpCollector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageEXPCollector_LevelUp implements IMessage, IMessageHandler<MessageEXPCollector_LevelUp, IMessage> {
	private int exp;

	public MessageEXPCollector_LevelUp(){}

	public MessageEXPCollector_LevelUp(int exp){
		this.exp = exp;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		exp = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {

		buf.writeInt(exp);
	}

	@Override
	public IMessage onMessage(MessageEXPCollector_LevelUp message, MessageContext ctx){
		EntityPlayer player = ctx.getServerHandler().player;
		if (player.openContainer instanceof ContainerExpCollector){
			TileEntityExpCollector collector = ((ContainerExpCollector)player.openContainer).getTileEntity();
			if (message.exp < 0){
				player.addExperience(collector.getField(TileEntityExpCollector.FIELD_EXPVALUE));
				collector.setField(TileEntityExpCollector.FIELD_EXPVALUE,0);
			}else{
				for ( int i = 0; i < message.exp; i++){
					int value = collector.getField(TileEntityExpCollector.FIELD_EXPVALUE)-player.xpBarCap();
					if (value >= 0){
						player.addExperienceLevel(1);
						collector.setField(TileEntityExpCollector.FIELD_EXPVALUE,value);
					}
				}

			}
		}
		return null;
	}

}
