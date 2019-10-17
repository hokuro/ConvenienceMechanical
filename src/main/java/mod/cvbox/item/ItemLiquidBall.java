package mod.cvbox.item;

import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class ItemLiquidBall extends Item {

	private final Fluid containedBlock;

	public ItemLiquidBall(Fluid containedFluidIn, Item.Properties property){
		super(property);
		this.containedBlock = containedFluidIn;
	}


	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		RayTraceResult raytraceresult = rayTrace(worldIn, playerIn, this.containedBlock == Fluids.EMPTY ? RayTraceContext.FluidMode.SOURCE_ONLY : RayTraceContext.FluidMode.NONE);
		ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, worldIn, itemstack, raytraceresult);
		if (ret != null) return ret;
		if (raytraceresult.getType() == RayTraceResult.Type.MISS) {
		return new ActionResult<>(ActionResultType.PASS, itemstack);
		} else if (raytraceresult.getType() != RayTraceResult.Type.BLOCK) {
		return new ActionResult<>(ActionResultType.PASS, itemstack);
		} else {
		BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult)raytraceresult;
		BlockPos blockpos = blockraytraceresult.getPos();
		if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, blockraytraceresult.getFace(), itemstack)) {
			BlockState blockstate = worldIn.getBlockState(blockpos);
			BlockPos blockpos1 = blockstate.getBlock() instanceof ILiquidContainer && this.containedBlock == Fluids.WATER ? blockpos : blockraytraceresult.getPos().offset(blockraytraceresult.getFace());
			if (this.tryPlaceContainedLiquid(playerIn, worldIn, blockpos1, blockraytraceresult)) {
				if (playerIn instanceof ServerPlayerEntity) {
					CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity)playerIn, blockpos1, itemstack);
				}
				if (playerIn.abilities.isCreativeMode) {
					itemstack.shrink(1);
				}
				return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
			} else {
				return new ActionResult<>(ActionResultType.FAIL, itemstack);
			}
		} else {
			return new ActionResult<>(ActionResultType.FAIL, itemstack);
		}
		}
	}


	public boolean tryPlaceContainedLiquid(@Nullable PlayerEntity player, World worldIn, BlockPos posIn, @Nullable BlockRayTraceResult p_180616_4_) {
		if (!(this.containedBlock instanceof FlowingFluid)) {
			return false;
		} else {
			BlockState blockstate = worldIn.getBlockState(posIn);
			Material material = blockstate.getMaterial();
			boolean flag = !material.isSolid();
			boolean flag1 = material.isReplaceable();
			if (worldIn.isAirBlock(posIn) || flag || flag1 || blockstate.getBlock() instanceof ILiquidContainer && ((ILiquidContainer)blockstate.getBlock()).canContainFluid(worldIn, posIn, blockstate, this.containedBlock)) {
				if (blockstate.getBlock() instanceof ILiquidContainer && this.containedBlock == Fluids.WATER) {
					if (((ILiquidContainer)blockstate.getBlock()).receiveFluid(worldIn, posIn, blockstate, ((FlowingFluid)this.containedBlock).getStillFluidState(false))) {
						this.playEmptySound(player, worldIn, posIn);
					}
				} else {
					if (!worldIn.isRemote && (flag || flag1) && !material.isLiquid()) {
						worldIn.destroyBlock(posIn, true);
					}

					this.playEmptySound(player, worldIn, posIn);
					worldIn.setBlockState(posIn, this.containedBlock.getDefaultState().getBlockState(), 11);
				}

				return true;
			} else {
				return p_180616_4_ == null ? false : this.tryPlaceContainedLiquid(player, worldIn, p_180616_4_.getPos().offset(p_180616_4_.getFace()), (BlockRayTraceResult)null);
			}
		}
	}

	protected void playEmptySound(@Nullable PlayerEntity player, IWorld worldIn, BlockPos pos) {
		SoundEvent soundevent = this.containedBlock.getAttributes().getEmptySound();
		if(soundevent == null) soundevent = this.containedBlock.isIn(FluidTags.LAVA) ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA : SoundEvents.ITEM_BUCKET_EMPTY;
		worldIn.playSound(player, pos, soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}
}
