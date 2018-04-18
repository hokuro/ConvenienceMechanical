package mod.cvbox.network;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import mod.cvbox.inventory.ContainerStraw;
import mod.cvbox.tileentity.TileEntityStraw;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageStraw_GetAll implements IMessage, IMessageHandler<MessageStraw_GetAll, IMessage> {

	public MessageStraw_GetAll(){}


	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	@Override
	public IMessage onMessage(MessageStraw_GetAll message, MessageContext ctx){
		EntityPlayer player = ctx.getServerHandler().player;
		if ( player.openContainer instanceof ContainerStraw){
			TileEntityStraw straw = ((ContainerStraw)player.openContainer).getTileEntity();
			int tank = straw.getField(TileEntityStraw.FIELD_TANK);
			List<ItemStack> stacks = new ArrayList<ItemStack>();

			while(tank > 0){
				stacks.add(new ItemStack(straw.getLiquid(),tank>64?64:tank));
				tank-=64;
			}
			if (!straw.isEmpty()){
				stacks.add(straw.getStackInSlot(0));
				straw.setInventorySlotContents(0, ItemStack.EMPTY);
			}

            for (int i = 0; i < stacks.size(); ++i)
            {
            	player.inventory.placeItemBackInInventory(player.world, stacks.get(i).copy());
            }
		}
		return null;
	}
}