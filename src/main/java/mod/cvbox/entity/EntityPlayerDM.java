package mod.cvbox.entity;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class EntityPlayerDM extends PlayerEntity {
	public EntityPlayerDM(World world, GameProfile gp){
		super(world,gp);
	}

	public boolean canCommandSenderUseCommand(int par1, String par2Str){
		return false;
	}

	public void addChatMessage(ITextComponent var1){
	}

	@Override
	public boolean isSpectator() {
		return false;
	}

	@Override
	public boolean isCreative() {
		return false;
	}
}
