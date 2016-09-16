package mod.cvbox.network;

import basashi.expbox.core.ExpBox;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
	public PacketHandler(){

	}

	public static void init(){
		ExpBox.INSTANCE.registerMessage(Message0.class, Message0.class, 0, Side.SERVER);
		ExpBox.INSTANCE.registerMessage(Message1.class, Message1.class, 1, Side.CLIENT);
		ExpBox.INSTANCE.registerMessage(Message2.class, Message2.class, 2, Side.CLIENT);
	}
}
