package mod.cvbox.network;

import io.netty.buffer.ByteBuf;
import mod.cvbox.block.BlockExpBank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageExpBank_ExecExperience implements IMessage, IMessageHandler<MessageExpBank_ExecExperience, IMessage> {
	private int mode;
	private int x;
	private int y;
	private int z;
	private int exp;

	public MessageExpBank_ExecExperience(){}

	public MessageExpBank_ExecExperience(int mode, int x, int y, int z, int exp){
		this.mode = mode;
		this.x = x;
		this.y = y;
		this.z = z;
		this.exp = exp;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		// TODO 自動生成されたメソッド・スタブ
		mode = buf.readInt();
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		exp = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// TODO 自動生成されたメソッド・スタブ
		buf.writeInt(mode);
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(exp);
	}

	@Override
	public IMessage onMessage(MessageExpBank_ExecExperience message, MessageContext ctx){
		EntityPlayer entityPlayer = ctx.getServerHandler().player;

		BlockPos pos = new BlockPos(message.x, message.y, message.z);
		if (entityPlayer != null){
			BlockExpBank.update_exp(ctx.getServerHandler().player.world,  pos,  message.mode, message.exp, entityPlayer);
		}
		return null;
	}

}
