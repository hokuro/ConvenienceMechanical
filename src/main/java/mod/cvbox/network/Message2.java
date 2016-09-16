package mod.cvbox.network;

public class Message2 {//implements IMessage, IMessageHandler<Message2, IMessage> {
//	private int x;
//	private int y;
//	private int z;
//
//	public Message2(){}
//	public Message2(int x, int y, int z){
//		this.x = x;
//		this.y = y;
//		this.z = z;
//	}
//
//	@Override
//	public void fromBytes(ByteBuf buf) {
//		// TODO 自動生成されたメソッド・スタブ
//		x = buf.readInt();
//		y = buf.readInt();
//		z = buf.readInt();
//	}
//
//	@Override
//	public void toBytes(ByteBuf buf) {
//		// TODO 自動生成されたメソッド・スタブ
//		buf.writeInt(x);
//		buf.writeInt(y);
//		buf.writeInt(z);
//	}
//
//	public IMessage onMessage(Message2 message, MessageContext ctx){
//		BlockPos pos = new BlockPos(message.x, message.y, message.z);
//		if(ctx.side == Side.CLIENT){
//			TileEntityExpBank ent = (TileEntityExpBank) ExpBox.proxy.getClientWorld().getTileEntity(pos);
//			if (ent != null){
//
//			}
//		}
//		if(ctx.side == Side.SERVER){
//			TileEntityExpBank ent = (TileEntityExpBank)ctx.getServerHandler().playerEntity.worldObj.getTileEntity(pos);
//			if(ent != null){
//			}
//		}
//		return null;
//	}

}
