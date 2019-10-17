package mod.cvbox.entity;
import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class EntityPlayerDummy extends PlayerEntity {

	public EntityPlayerDummy(World worldIn, GameProfile gameProfile) {
		super(worldIn, gameProfile);
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
	public void read(CompoundNBT compound){}

//	@Override
//	public void write(CompoundNBT compound){
//		s
//	}

}
