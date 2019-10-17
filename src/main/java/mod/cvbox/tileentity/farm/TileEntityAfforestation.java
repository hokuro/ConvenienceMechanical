package mod.cvbox.tileentity.farm;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import mod.cvbox.config.ConfigValue;
import mod.cvbox.entity.EntityCore;
import mod.cvbox.entity.EntityPlayerDummy;
import mod.cvbox.inventory.farm.ContainerPlantNurture.NURTUREKIND;
import mod.cvbox.item.ItemBattery;
import mod.cvbox.tileentity.ab.TileEntityPlantNurtureBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.LogBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ISidedInventoryProvider;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;

public class TileEntityAfforestation extends TileEntityPlantNurtureBase{

	public static final String NAME = "afforestation";
	protected EntityPlayerDummy dummy;

	public TileEntityAfforestation() {
		super(EntityCore.Afforestation, NURTUREKIND.SAPLING);
	}

	protected void makeArray(){
		int size = (ConfigValue.afforestation.MaxDistance() * 2 + 1) * (ConfigValue.afforestation.MaxDistance() * 2 + 1) -1;
		next_x = new int[size];
		next_z = new int[size];

		int maxLp = ConfigValue.afforestation.MaxDistance() * 4 + 1;
		int len = 0;
		int x = 0;
		int z = 0;
		int idx = 0;
		for (int i = 0; i < maxLp; i ++){
			if (i%2 == 0 && i != maxLp-1){len++;}
			for (int j = 0; j < len; j++){
				switch(i%4){
				case 0: x++; break;
				case 1: z++;break;
				case 2: x--;break;
				case 3: z--;break;
				}


				next_x[idx] = x;
				next_z[idx] = z;
				idx++;
			}
		}
	}

	@Override
	public void tick() {
		if (!world.isRemote) {
			if (dummy == null){
				dummy = new EntityPlayerDummy(this.world, new GameProfile(new UUID(0,0), "dummy"));
			}
		}
		super.tick();
	}

	protected void Exec() {
		this.timeCnt++;
		if (timeCnt >= ConfigValue.afforestation.ExecTimeSpan()){
			for (int i = 0; i < ConfigValue.afforestation.OneTicPlant(); i++){
				// 伐採するよ余裕があるなら伐採
				if (!isFullHarvestBox()) {
					execHarvest();
				}
				// 苗木をプランターに移し替える
				if (this.canDeliver) {
					delivery();
				}
				// 植林
				execPlant();

				// 次の座標へ
				this.nextPos++;
				if (nextPos >= next_x.length){
					nextPos = 0;
				}
			}
			// 骨粉を補充する
			supplyBoneMeal();
			// 骨粉をまく
			scatteredBoneMeal();
			timeCnt = 0;
		}
	}

	public boolean isFullHarvestBox() {
		boolean ret = true;
		for (int i = 0; i < harvesterInventory.getSizeInventory(); i++) {
			if (harvesterInventory.getStackInSlot(i).isEmpty() || harvesterInventory.getStackInSlot(i).getCount() < harvesterInventory.getStackInSlot(i).getMaxStackSize()) {
				ret = false;
				break;
			}
		}
		return ret;
	}

	private void execHarvest(){
		List<ItemStack> drops;
		ItemStack tool = this.toolInventory.getStackInSlot(SLOT_TOOL);
		for (int y = -ConfigValue.afforestation.MaxHarvestHeight(); y <= ConfigValue.afforestation.MaxHarvestHeight(); y++){
			boolean canhavest = false;
			BlockPos plantPos = new BlockPos(this.pos.add( next_x[nextPos], y, next_z[nextPos]));
			BlockState state = world.getBlockState(plantPos);
			Block block = state.getBlock();
			// 空気ブロックの場合無視
			if (state.getMaterial() == Material.AIR){continue;}

			BlockPos plantPos2 = plantPos.add(0,0,0);
			drops = new ArrayList<ItemStack>();
			while((block instanceof LogBlock) || (block instanceof LeavesBlock)){
				y = plantPos2.getY()-this.pos.getY();
				drops.clear();
				LootContext.Builder bilder = (new LootContext.Builder((ServerWorld)world)).withParameter(LootParameters.POSITION, plantPos2).withParameter(LootParameters.BLOCK_STATE, state).withParameter(LootParameters.TOOL, tool);
				drops = state.getDrops(bilder);
				for (ItemStack drop : drops) {
					ItemStack over = this.harvesterInventory.addItem(drop);
					// あふれた分のアイテムをぶちまける
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY() + world.rand.nextInt(5), pos.getZ(), over);
				}
				world.destroyBlock(plantPos2, false);
				if (tool.attemptDamageItem(1, world.rand, null)){
					this.setInventorySlotContents(SLOT_TOOL, ItemStack.EMPTY);
				}
				plantPos2 = plantPos2.offset(Direction.UP);
				state = world.getBlockState(plantPos2);
				block = state.getBlock();
			}
		}
	}

	private ItemStack execPlant(){
		ItemStack seeds = nextItem();
		if (!seeds.isEmpty()){
			dummy.setHeldItem(Hand.MAIN_HAND, seeds);
			for (int y = -ConfigValue.afforestation.MaxPlantHeight(); y <= ConfigValue.afforestation.MaxPlantHeight(); y++){
				BlockPos plantPos = new BlockPos(this.pos.add( next_x[nextPos], y, next_z[nextPos]));
				BlockState state = world.getBlockState(plantPos);
				// 空気ブロックの場合無視
				if (state.getMaterial() == Material.AIR){continue;}

				// 植樹
				seeds.onItemUse(new ItemUseContext((PlayerEntity)dummy, Hand.MAIN_HAND,  new BlockRayTraceResult(new Vec3d(1.0D, 1.0D, 1.0D),Direction.UP,plantPos, true)));
				if (seeds.getCount() <= 0 || seeds.isEmpty()){
					break;
				}
			}
		}
		return seeds;
	}

	private ItemStack nextItem(){
		ItemStack ret = ItemStack.EMPTY;
		for (int i = 0; i < planterInventory.getSizeInventory(); i++){
			if (!planterInventory.getStackInSlot(i).isEmpty()){
				ret = planterInventory.getStackInSlot(i);
			}
		}
		return ret;
	}

	private void delivery() {
		for (int i = 0; i < harvesterInventory.getSizeInventory(); i++) {
			ItemStack deliver = harvesterInventory.getStackInSlot(i);
			if (isValidatePlanter(i, deliver)) {
				ItemStack addStack = new ItemStack(deliver.getItem(),1);
				if (planterInventory.addItem(addStack).isEmpty()) {
					// 配信に成功したら元の素材を1減らす
					deliver.shrink(1);
				}
			}
		}
	}

	private void supplyBoneMeal() {
		// 直上がコンポスターなら骨粉を補充
		int size = Math.min(Items.BONE_MEAL.getMaxStackSize(), this.toolInventory.getInventoryStackLimit());
		if (((this.toolInventory.getStackInSlot(SLOT_MEAL).isEmpty())) || ((!this.toolInventory.getStackInSlot(SLOT_MEAL).isEmpty()) && this.toolInventory.getStackInSlot(SLOT_MEAL).getCount() < size)) {

	        IInventory iinventory = null;
			BlockState state = world.getBlockState(pos.offset(Direction.UP));
			if (state.getBlock() == Blocks.COMPOSTER) {
				iinventory = ((ISidedInventoryProvider)state.getBlock()).createInventory(state, world, pos.offset(Direction.UP));
		   	}
			if (iinventory != null) {
				Direction direction = Direction.DOWN;
				if (iinventory instanceof ISidedInventory) {
					 int[] slots = ((ISidedInventory)iinventory).getSlotsForFace(Direction.DOWN);
					 for (int slot : slots) {
						 ItemStack itemstack = iinventory.getStackInSlot(slot);
						 if (((ISidedInventory)iinventory).canExtractItem(slot, itemstack, Direction.DOWN)) {
							 // 余ってるサイズを計算
							 ItemStack invStack = this.toolInventory.getStackInSlot(SLOT_MEAL);
							 int count = size - invStack.getCount();
							 if (invStack.isEmpty()) {
								 // 元々空っぽなら限界まで
								 count = this.toolInventory.getInventoryStackLimit();
							 }
							 // コンポスタ側のスタック数と比較して小さい方を採用
							 int newCount = Math.min(count, itemstack.getCount());
							 // インベントリにアイテム設定
							 this.toolInventory.setInventorySlotContents(SLOT_MEAL, new ItemStack(itemstack.getItem(), invStack.isEmpty()?newCount:newCount + invStack.getCount()));

							 itemstack.shrink(newCount);
							 if (itemstack.isEmpty()) {
								 ((ISidedInventory)iinventory).markDirty();
							 }
						 }
					 }
				}
			}
		}
	}


	private void scatteredBoneMeal() {
		// 最大範囲の1/10+1箇所適当に肥料をばらまく
		ItemStack stack = this.toolInventory.getStackInSlot(SLOT_MEAL);
		if (!stack.isEmpty()) {
			int max = ConfigValue.afforestation.MaxDistance();
			int chance = Math.min(world.rand.nextInt((max * max)/10 + 1), stack.getCount());
			for (int i = 0; i < chance; i++) {
				int point = world.rand.nextInt(next_x.length);
				for (int y = -ConfigValue.afforestation.MaxPlantHeight(); y <= ConfigValue.afforestation.MaxPlantHeight(); y++){
					BlockPos pos = new BlockPos(next_x[point], y , next_z[point]);
					BlockState state = world.getBlockState(pos);
					if (state.getBlock() instanceof SaplingBlock) {
						stack.onItemUse(new ItemUseContext((PlayerEntity)dummy, Hand.MAIN_HAND,  new BlockRayTraceResult(new Vec3d(1.0D, 1.0D, 1.0D),Direction.UP, pos, true)));
						break;
					}
				}
			}
		}
	}

	public boolean isValidateTools(int slot, ItemStack stack) {
		boolean ret = false;
		if (slot == 0 && stack.getItem() instanceof ItemBattery) {
			ret = true;
		}else if(slot == 1 && stack.getItem() instanceof AxeItem) {
			ret = true;
		}else if(slot == 2 && stack.getItem() == Items.BONE_MEAL) {
			ret = true;
		}
		return ret;
	}

	public boolean isValidatePlanter(int slot, ItemStack stack) {
		return (Block.getBlockFromItem(stack.getItem()) instanceof SaplingBlock);
	}

	public boolean isValidateHarvester(int slot, ItemStack stack) {
		return false;
	}
}
