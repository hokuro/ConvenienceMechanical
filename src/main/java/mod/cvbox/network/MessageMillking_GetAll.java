package mod.cvbox.network;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import mod.cvbox.inventory.farm.ContainerMillking;
import mod.cvbox.tileentity.farm.TileEntityMillking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageMillking_GetAll  {

	public MessageMillking_GetAll(){}


	public static void encode(MessageMillking_GetAll pkt, PacketBuffer buf){
	}

	public static MessageMillking_GetAll decode(PacketBuffer buf){
		return new MessageMillking_GetAll();
	}

	public static class Handler {
		public static void handle(final MessageMillking_GetAll pkt, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				PlayerEntity player = ctx.get().getSender();
				if ( player.openContainer instanceof ContainerMillking){
					TileEntityMillking entity = ((ContainerMillking)player.openContainer).getTileEntity();
					int tank = entity.getField(TileEntityMillking.FIELD_TANK);
					List<ItemStack> stacks = new ArrayList<ItemStack>();

					while(tank > 0) {
						stacks.add(new ItemStack(entity.getLiquid(),tank>64?64:tank));
						tank-=64;
					}
					if (!entity.isEmpty()) {
						stacks.add(entity.getStackInSlot(1).copy());
						entity.setInventorySlotContents(1, ItemStack.EMPTY);
					}

					for (int i = 0; i < stacks.size(); ++i) {
						player.inventory.placeItemBackInInventory(player.world, stacks.get(i));
					}
					entity.setField(TileEntityMillking.FIELD_TANK, 0);
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
}
