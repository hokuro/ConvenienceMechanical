package mod.cvbox.tileentity;
import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityPlayerDummy extends EntityPlayer {

	public EntityPlayerDummy(World worldIn, GameProfile gameProfileIn) {
		super(worldIn, gameProfileIn);
	}

	@Override
	public boolean isSpectator() {
		return false;
	}

	@Override
	public boolean isCreative() {
		return false;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound){}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound){}

}
