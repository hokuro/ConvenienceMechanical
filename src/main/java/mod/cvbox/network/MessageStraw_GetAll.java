package mod.cvbox.network;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import mod.cvbox.inventory.ContainerStraw;
import mod.cvbox.tileentity.TileEntityStraw;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageStraw_GetAll  {

	public MessageStraw_GetAll(){}


	public static void encode(MessageStraw_GetAll pkt, PacketBuffer buf)
	{
	}

	public static MessageStraw_GetAll decode(PacketBuffer buf)
	{
		return new MessageStraw_GetAll();
	}

	public static class Handler
	{
		public static void handle(final MessageStraw_GetAll pkt, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				EntityPlayer player = ctx.get().getSender();
				if ( player.openContainer instanceof ContainerStraw){
					TileEntityStraw straw = ((ContainerStraw)player.openContainer).getTileEntity();
					int tank = straw.getField(TileEntityStraw.FIELD_TANK);
					List<ItemStack> stacks = new ArrayList<ItemStack>();

					while(tank > 0){
						stacks.add(new ItemStack(straw.getLiquid(),tank>64?64:tank));
						tank-=64;
					}
					if (!straw.isEmpty()){
						stacks.add(straw.getStackInSlot(1));
						straw.setInventorySlotContents(1, ItemStack.EMPTY);
					}

		            for (int i = 0; i < stacks.size(); ++i)
		            {
		            	player.inventory.placeItemBackInInventory(player.world, stacks.get(i).copy());
		            }
		            straw.setField(TileEntityStraw.FIELD_TANK, 0);
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
}