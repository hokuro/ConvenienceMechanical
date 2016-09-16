package mod.cvbox.network;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Lists;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@ChannelHandler.Sharable
public class PacketPipeline extends MessageToMessageCodec<FMLProxyPacket, AbstractPacket>{
	private EnumMap<Side, FMLEmbeddedChannel> channels;
	private LinkedList<Class<? extends AbstractPacket>> packets = Lists.newLinkedList();
	private boolean isPostInitialised = false;

	public PacketPipeline(){
	}

	public boolean registerPacket(Class<? extends AbstractPacket> clazz){
		if((this.packets.size() > 256 | this.packets.contains(clazz) | isPostInitialised)){
			return false;
		}
		packets.add(clazz);
		return true;
	}

	public void init(String channelName){
		channels = NetworkRegistry.INSTANCE.newChannel(channelName, this);
	}

	public void postInit(){
		if (isPostInitialised){
			return;
		}
		isPostInitialised = true;

		Collections.sort(packets, new Comparator(){
			@Override
			public int compare(Object o1, Object o2) {
				int com = String.CASE_INSENSITIVE_ORDER.compare(((Class<? extends AbstractPacket>)o1).getCanonicalName(), ((Class<? extends AbstractPacket>)o2).getCanonicalName());
				if (com == 0){
					com = ((Class<? extends AbstractPacket>)o1).getCanonicalName().compareTo(((Class<? extends AbstractPacket>)o2).getCanonicalName());
				}
				return com;
			}
		});
	}

	protected void encode(ChannelHandlerContext ctx, AbstractPacket msg, List<Object> out) throws Exception{
		ByteBuf buffer = Unpooled.buffer();
		Class<? extends AbstractPacket> clazz = msg.getClass();
		if (!packets.contains(msg.getClass())){
			throw new NullPointerException("No Packet Registerd for:" + msg.getClass().getCanonicalName());
		}
		byte discriminator = (byte) this.packets.indexOf(clazz);
		buffer.writeByte(discriminator);
		msg.encodeInto(ctx,buffer);
		PacketBuffer buf = new PacketBuffer(buffer.copy());
		FMLProxyPacket proxyPacket = new FMLProxyPacket(
				new CPacketCustomPayload((String) ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get(),buf));
		out.add(proxyPacket);
	}

	protected void decode(ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out) throws Exception{
		ByteBuf payload = msg.payload();
		byte discriminator = payload.readByte();
		Class<? extends AbstractPacket> clazz = (Class)packets.get(discriminator);
		if (clazz == null){
			throw new NullPointerException("No packet registerd for discrimanator: " + discriminator);
		}
		AbstractPacket pkt = (AbstractPacket)clazz.newInstance();
		pkt.decodeInto(ctx, payload.slice());
		EntityPlayer player;
		switch(FMLCommonHandler.instance().getEffectiveSide()){
		case CLIENT:
			player = getClientPlayer();
			pkt.handleClientSide(player);
			break;
		case SERVER:
			INetHandler netHandler = (INetHandler)ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
			player = ((NetHandlerPlayServer)netHandler).playerEntity;
			pkt.handleServerSide(player);
			break;
		}
		out.add(pkt);
	}

	@SideOnly(Side.CLIENT)
	private EntityPlayer getClientPlayer(){
		return Minecraft.getMinecraft().thePlayer;
	}

	public void sendPacketToAllPlayer(AbstractPacket packet){
		((FMLEmbeddedChannel) channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGET)
		.set(FMLOutboundHandler.OutboundTarget.ALL);
		((FMLEmbeddedChannel) channels.get(Side.SERVER)).writeAndFlush(packet);
	}

	public void sendPacketToPlayer(AbstractPacket packet, EntityPlayerMP player){
		((FMLEmbeddedChannel) channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGET)
			.set(FMLOutboundHandler.OutboundTarget.PLAYER);
		((FMLEmbeddedChannel) channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS)
			.set(player);
		((FMLEmbeddedChannel) channels.get(Side.SERVER)).writeAndFlush(packet);
	}

	public void sendPacketToAllInDimension(AbstractPacket packet, int dimensionId){
		((FMLEmbeddedChannel) channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGET)
			.set(FMLOutboundHandler.OutboundTarget.DIMENSION);
		((FMLEmbeddedChannel) channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS)
			.set(Integer.valueOf(dimensionId));
		((FMLEmbeddedChannel)channels.get(Side.SERVER)).writeAndFlush(packet);
	}

	public void sendPacketToServer(AbstractPacket packet){
		((FMLEmbeddedChannel) channels.get(Side.CLIENT)).attr(FMLOutboundHandler.FML_MESSAGETARGET)
			.set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		((FMLEmbeddedChannel) channels.get(Side.CLIENT)).writeAndFlush(packet);
	}
}