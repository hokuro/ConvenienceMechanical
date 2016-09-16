package mod.cvbox.network;
//package basashi.autoplanting.network;
//
//import basashi.autoplanting.entity.TileEntityAutoPlanting;
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandlerContext;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.BlockPos;
//import net.minecraft.world.World;
//
//public class PacketGuiButton extends AbstractPacket {
//	private int buttonId;
//	private int x = 0;
//	private int y=0;
//	private int z = 0;
//
//	public PacketGuiButton(){
//	}
//
//	public PacketGuiButton(int id, int xx, int yy, int zz){
//		buttonId = id;
//		x = xx;
//		y = yy;
//		z = zz;
//	}
//
//	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
//		buffer.writeInt(buttonId);
//		buffer.writeInt(x);
//		buffer.writeInt(y);
//		buffer.writeInt(z);
//	}
//
//	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
//		buttonId =buffer.readInt();
//
//		x = buffer.readInt();
//		y = buffer.readInt();
//		z = buffer.readInt();
//	}
//
//	public void handleClientSide(EntityPlayer player){
//	}
//
//	public void handleServerSide(EntityPlayer player){
//		World world = player.worldObj;
//
//		TileEntity tileEntity = world.getTileEntity(new BlockPos(x,y,z));
//		if ((tileEntity instanceof TileEntityAutoPlanting)) {
//			TileEntityAutoPlanting tileEntityNoop = (TileEntityAutoPlanting) tileEntity;
//			tileEntityNoop.dt_selectPriority = this.buttonId;
//
//			tileEntityNoop.dt_selectPriority = this.buttonId;
//			if (this.buttonId == 0) {
//				IBlockState meta = world.getBlockState(new BlockPos(x,y,z));
//				world.setBlockState(new BlockPos(x,y,z), meta);
//			} else {
//				IBlockState meta = world.getBlockState(new BlockPos(x,y,z));
//				world.setBlockState(new BlockPos(x,y,z), meta);
//			}
//		}
//	}
//}