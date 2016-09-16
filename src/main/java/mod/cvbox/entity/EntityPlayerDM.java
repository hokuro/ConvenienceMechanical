package mod.cvbox.entity;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class EntityPlayerDM extends EntityPlayer {
	public EntityPlayerDM(World world, GameProfile gp){
		super(world,gp);
	}

	public boolean canCommandSenderUseCommand(int par1, String par2Str){
		return false;
	}
//
//	public ChunkCoordComparator getPlayerCoordinates(){
////		return new ChunkCoordComparator(MathHelper.floor_double(posX),
////			MathHelper.floor_double(posY+0.5D), MathHelper.floor_double(posZ));
//	}

	public void addChatMessage(ITextComponent var1){
	}

	@Override
	public boolean isSpectator() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean isCreative() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}
}