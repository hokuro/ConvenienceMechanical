package mod.cvbox.tileentity.factory;

import mod.cvbox.entity.EntityCore;
import mod.cvbox.item.ItemBattery;
import mod.cvbox.item.ItemCore;
import mod.cvbox.tileentity.ab.TileEntityPowerBase;
import mod.cvbox.util.ModUtil;
import mod.cvbox.util.ModUtil.CompaierLevel;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;

public class TileEntityLiquidMaker extends TileEntityPowerBase{
	public static final String NAME = "liquidmaker";
	public static final int FIELD_MODE = 0 + FIELD_OFFSET;

	public final int WORK_TIME = 100;
	public static final int MODE_NONE = 0;
	public static final int MODE_ICE = 1;
	public static final int MODE_LAVA = 2;
	public static final int SLOT_ITEM = 1;

	public TileEntityLiquidMaker(){
		super(EntityCore.LiquidMaker,2);
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	protected boolean canWork() {
		return true;
	}

	private BlockPos[] offset = new BlockPos[]{
			new BlockPos(0,-1,0),
			new BlockPos(1,-1,0),
			new BlockPos(-1,-1,0),
			new BlockPos(0,-1,1),
			new BlockPos(0,-1,-1),
			new BlockPos(1,-1,1),
			new BlockPos(-1,-1,-1),
			new BlockPos(1,-1,-1),
			new BlockPos(-1,-1,1)
		};

	@Override
	protected void onWork() {
    	int mode = getMode();
    	if (mode == MODE_NONE){return;}
		for (BlockPos p : offset){
			BlockState state = this.world.getBlockState(this.pos.add(p.getX(),p.getY(),p.getZ()));
			if (mode == MODE_ICE){
				if (state.getMaterial() == Material.WATER){
					if (!world.isRemote){
						this.world.setBlockState(this.pos.add(p.getX(),p.getY(),p.getZ()), Blocks.ICE.getDefaultState());
					}
				}
			}else{
				if (state.getMaterial() == Material.CORAL ||
					state.getMaterial() == Material.GOURD ||
					state.getMaterial() == Material.GLASS ||
					state.getMaterial() == Material.ROCK ||
					state.getMaterial() == Material.SAND){
					if (!world.isRemote){
						this.world.setBlockState(this.pos.add(p.getX(),p.getY(),p.getZ()),  Blocks.LAVA.getDefaultState());
					}
					double d0 = (double)pos.getX() + p.getX();
					double d1 = (double)pos.getY() + p.getY();
					double d2 = (double)pos.getZ() + p.getZ();
					this.world.playSound(d0,d1,d2,
									SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS,
									0.5F, 2.6F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.8F, true);

					ModUtil.addParticleTypes(world, this.pos, ParticleTypes.LARGE_SMOKE);
				}else if (state.getBlock() != Blocks.LAVA && state.getMaterial() != Material.AIR){
					if (!world.isRemote){
						this.world.setBlockState(this.pos.add(p.getX(),p.getY(),p.getZ()), Blocks.AIR.getDefaultState());
					}
					double d0 = (double)pos.getX() + p.getX();
					double d1 = (double)pos.getY() + p.getY();
					double d2 = (double)pos.getZ() + p.getZ();
					this.world.playSound(d0,d1,d2,
									SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS,
									0.5F, 2.6F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.8F, true);
					ModUtil.addParticleTypes(world, this.pos, ParticleTypes.LARGE_SMOKE);
				}
			}
		}
	}

	@Override
	public int getTickTime() {
		return WORK_TIME;
	}

	@Override
    public void read(CompoundNBT compound) {
		super.read(compound);
    }

	@Override
    public CompoundNBT write(CompoundNBT compound) {
		compound = super.write(compound);

        return compound;
    }

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == SLOT_BATTERY) {
			return (stack.getItem() instanceof ItemBattery);
		}else if (index == SLOT_ITEM){
			return (stack.getItem() == Items.LAVA_BUCKET ||
					stack.getItem() == ItemCore.item_lavaball ||
					stack.getItem() == Items.ICE ||
					stack.getItem() == Items.PACKED_ICE ||
					stack.getItem() == Items.BLUE_ICE);
		}
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public int additionalgetField(int id) {
		int ret = 0;
		switch(id){
		case FIELD_MODE:
			ret = getMode();
			break;
		}
		return ret;
	}

	@Override
	public void additionalsetField(int id, int value) {
	}

	@Override
	public int additionalFieldCount() {
		return 0;
	}

	public static final ItemStack[] ice = new ItemStack[]{
			new ItemStack(Blocks.ICE),
			new ItemStack(Blocks.PACKED_ICE),
			new ItemStack(Blocks.BLUE_ICE),
			new ItemStack(Blocks.FROSTED_ICE)
	};
	public static final ItemStack[] lava = new ItemStack[]{
			new ItemStack(Items.LAVA_BUCKET),
			new ItemStack(ItemCore.item_lavaball)
	};
	public int getMode(){
		int ret = MODE_NONE;
		ItemStack stack = getModeItem();
		if (!stack.isEmpty()){
			for (ItemStack st : ice){
				if (ModUtil.compareItemStacks(stack, st, CompaierLevel.LEVEL_EQUAL_ITEM)){
					ret = MODE_ICE;
					break;
				}
			}
			if (ret == MODE_NONE){
				for (ItemStack st : lava){
					if (ModUtil.compareItemStacks(stack, st, CompaierLevel.LEVEL_EQUAL_ITEM)){
						ret = MODE_LAVA;
						break;
					}
				}
			}
		}
		return ret;
	}

	private ItemStack getModeItem(){
		return this.stacks.get(1);
	}
}
