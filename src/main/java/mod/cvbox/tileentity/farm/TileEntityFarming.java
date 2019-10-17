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
import mod.cvbox.item.ItemSickle;
import mod.cvbox.tileentity.ab.TileEntityPlantNurtureBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CocoaBlock;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ISidedInventoryProvider;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
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

public class TileEntityFarming extends TileEntityPlantNurtureBase{

	public static final String NAME = "farming";
	protected EntityPlayerDummy dummy;

	public TileEntityFarming() {
		super(EntityCore.Farming, NURTUREKIND.FARMING);
	}

	protected void makeArray(){
		int size = (ConfigValue.farming.MaxDistance() * 2 + 1) * (ConfigValue.farming.MaxDistance() * 2 + 1) -1;
		next_x = new int[size];
		next_z = new int[size];

		int maxLp = ConfigValue.farming.MaxDistance() * 4 + 1;
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
		if (timeCnt >= ConfigValue.farming.ExecTimeSpan()){
			for (int i = 0; i < ConfigValue.farming.OneTicPlant(); i++){
				// i収穫するよ余裕があるなら収穫
				if (!isFullHarvestBox()) {
					execHarvest();
				}
				// i収穫物をプランターに移し替える
				if (this.canDeliver) {
					delivery();
				}
				// i種植え
				execPlant();

				// i次の座標へ
				this.nextPos++;
				if (nextPos >= next_x.length){
					nextPos = 0;
				}
			}
			// i骨粉を補充する
			supplyBoneMeal();
			// i骨粉をまく
			scatteredBoneMeal();
			timeCnt = 0;
		}
	}

	private void execHarvest(){
		List<ItemStack> drops;
		ItemStack tool = this.toolInventory.getStackInSlot(SLOT_TOOL);

		for (int y = -ConfigValue.farming.MaxHeight(); y <= ConfigValue.farming.MaxHeight(); y++){
			boolean canhavest = false;
			BlockPos plantPos = new BlockPos(this.pos.add(next_x[nextPos], y, next_z[nextPos]));
			BlockState state = world.getBlockState(plantPos);

			// i空気ブロックの場合無視
			if (state.getMaterial() == Material.AIR){continue;}
			Block block = state.getBlock();

			if (block instanceof CropsBlock &&
					((CropsBlock)block).isMaxAge(state)){
				canhavest = true;
			}else if (block == Blocks.NETHER_WART && (state).get(NetherWartBlock.AGE) >= 3){
				canhavest = true;
			}else if ((block == Blocks.CACTUS || block == Blocks.SUGAR_CANE || block == Blocks.BAMBOO) &&
					(world.getBlockState(plantPos.offset(Direction.DOWN)).getBlock() == block &&
					world.getBlockState(plantPos.offset(Direction.UP)).getBlock() != block)){
				while(true){
					if ((block == Blocks.CACTUS || block == Blocks.SUGAR_CANE || block == Blocks.BAMBOO)) {
						if ((world.getBlockState(plantPos.offset(Direction.DOWN)).getBlock() == block &&
							world.getBlockState(plantPos.offset(Direction.UP)).getBlock() != block)) {
							canhavest = true;
							break;
						}
					} else {
						break;
					}
					plantPos = plantPos.up();
					block = world.getBlockState(plantPos).getBlock();
				}
				y = plantPos.getY();
			}else if (block == Blocks.KELP) {
				if ((world.getBlockState(plantPos.offset(Direction.DOWN)).getBlock() == Blocks.KELP_PLANT &&
					world.getBlockState(plantPos.offset(Direction.UP)).getBlock() != block)) {
					canhavest = true;
				}
			}else if (block == Blocks.MELON || block == Blocks.PUMPKIN){
				canhavest = true;
			}else if (block == Blocks.COCOA  && (state).get(CocoaBlock.AGE) >= 2){
				canhavest = true;
			}else if (block == Blocks.CHORUS_FLOWER &&
					world.getBlockState(plantPos.offset(Direction.DOWN)).getBlock() != Blocks.END_STONE){
				canhavest = true;

			}else if (block == Blocks.SWEET_BERRY_BUSH && (state).get(SweetBerryBushBlock.AGE) >= 3) {
				int j = 1 + this.world.rand.nextInt(2) + EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, tool);
				drops = new ArrayList<ItemStack>();
				drops.add(new ItemStack(Items.SWEET_BERRIES, j + ((state).get(SweetBerryBushBlock.AGE) >= 3 ? 1 : 0)));
				this.world.setBlockState(plantPos, state.with(SweetBerryBushBlock.AGE, Integer.valueOf(1)), 2);
				for (ItemStack stack : drops) {
					if (!stack.isEmpty()) {
						ItemStack over = this.harvesterInventory.addItem(stack);
						if (!over.isEmpty()) {
							InventoryHelper.spawnItemStack(this.world, pos.getX(), pos.getY() + world.rand.nextInt(5), pos.getZ(), over);
						}
					}
				}
				if (tool.attemptDamageItem(1, world.rand, null)){
					this.setInventorySlotContents(SLOT_TOOL, ItemStack.EMPTY);
				}
			}

			if (canhavest) {
				LootContext.Builder bilder = (new LootContext.Builder((ServerWorld)world)).withParameter(LootParameters.POSITION, plantPos).withParameter(LootParameters.BLOCK_STATE, state).withParameter(LootParameters.TOOL, tool);
				drops = state.getDrops(bilder);
				for (ItemStack stack : drops) {
					if (!stack.isEmpty()) {
						ItemStack over = this.harvesterInventory.addItem(stack);
						if (!over.isEmpty()) {
							InventoryHelper.spawnItemStack(this.world, pos.getX(), pos.getY() + world.rand.nextInt(5), pos.getZ(), over);
						}
					}
				}
				world.removeBlock(plantPos, false);
				if (tool.attemptDamageItem(1, world.rand, null)){
					this.setInventorySlotContents(SLOT_TOOL, ItemStack.EMPTY);
				}
			}
		}
	}

	private ItemStack execPlant(){
		ItemStack seeds = nextItem();
		if (!seeds.isEmpty()){
			dummy.setHeldItem(Hand.MAIN_HAND, seeds);
			for (int y = -ConfigValue.farming.MaxHeight(); y <= ConfigValue.farming.MaxHeight(); y++){
				BlockPos plantPos = new BlockPos(this.pos.add( next_x[nextPos], y, next_z[nextPos]));
				BlockState state = world.getBlockState(plantPos);
				// i空気ブロックの場合無視
				if (state.getMaterial() == Material.AIR){continue;}

				if (seeds.getItem() == Item.getItemFromBlock(Blocks.SUGAR_CANE)){
					// iサトウキビ
					if (state.getBlock() != Blocks.SUGAR_CANE){
						// i見つけたブロックがサトウキビブロック以外なら設置
						seeds.onItemUse(new ItemUseContext(dummy, Hand.MAIN_HAND, new BlockRayTraceResult(new Vec3d(1.0f, 1.0f, 1.0f), Direction.UP, plantPos, true)));
					}
				}else if (seeds.getItem() == Item.getItemFromBlock(Blocks.CACTUS)){
					// iサボテン
					if (state.getBlock() != Blocks.CACTUS){
						// i見つけたブロックがサボテン以外なら設置
						seeds.onItemUse(new ItemUseContext(dummy, Hand.MAIN_HAND, new BlockRayTraceResult(new Vec3d(1.0f, 1.0f, 1.0f), Direction.UP, plantPos, true)));
					}
				}else if (seeds.getItem() == Item.getItemFromBlock(Blocks.KELP)){
					// i昆布
					if (state.getBlock() != Blocks.KELP && state.getBlock() != Blocks.KELP_PLANT && state.getBlock() != Blocks.WATER){
						// i見つけたブロックが昆布以外なら設置
						seeds.onItemUse(new ItemUseContext(dummy, Hand.MAIN_HAND, new BlockRayTraceResult(new Vec3d(1.0f, 1.0f, 1.0f), Direction.UP, plantPos, true)));
					}
				}else if (seeds.getItem() == Item.getItemFromBlock(Blocks.BAMBOO)){
					// i竹
					if (state.getBlock() != Blocks.BAMBOO){
						// i見つけたブロックが竹以外なら設置
						seeds.onItemUse(new ItemUseContext(dummy, Hand.MAIN_HAND, new BlockRayTraceResult(new Vec3d(1.0f, 1.0f, 1.0f), Direction.UP, plantPos, true)));
					}
				}else if (seeds.getItem() == Items.COCOA_BEANS){
					// iカカオ
					seeds.onItemUse(new ItemUseContext(dummy, Hand.MAIN_HAND, new BlockRayTraceResult(new Vec3d(1.0f, 1.0f, 1.0f), Direction.NORTH, plantPos, true)));
					if (seeds.getCount() != 0 && !seeds.isEmpty()) {
						seeds.onItemUse(new ItemUseContext(dummy, Hand.MAIN_HAND, new BlockRayTraceResult(new Vec3d(1.0f, 1.0f, 1.0f), Direction.WEST, plantPos, true)));
					}
					if (seeds.getCount() != 0 && !seeds.isEmpty()) {
						seeds.onItemUse(new ItemUseContext(dummy, Hand.MAIN_HAND, new BlockRayTraceResult(new Vec3d(1.0f, 1.0f, 1.0f), Direction.EAST, plantPos, true)));
					}
					if (seeds.getCount() != 0 && !seeds.isEmpty()) {
						seeds.onItemUse(new ItemUseContext(dummy, Hand.MAIN_HAND, new BlockRayTraceResult(new Vec3d(1.0f, 1.0f, 1.0f), Direction.SOUTH, plantPos, true)));
					}
				}else if (seeds.getItem() == Item.getItemFromBlock(Blocks.CHORUS_FLOWER)){
					// iコーラルフラワー
					if (state.getBlock() == Blocks.END_STONE) {
						seeds.onItemUse(new ItemUseContext(dummy, Hand.MAIN_HAND, new BlockRayTraceResult(new Vec3d(1.0f, 1.0f, 1.0f), Direction.UP, plantPos, true)));
					}
				}else if (seeds.getItem() == Items.SWEET_BERRIES){
					// スイートベリー
					if (state.getBlock() != Blocks.SWEET_BERRY_BUSH) {
						seeds.onItemUse(new ItemUseContext(dummy, Hand.MAIN_HAND, new BlockRayTraceResult(new Vec3d(1.0f, 1.0f, 1.0f), Direction.UP, plantPos, true)));
					}
				}else if (seeds.getItem() == Items.PUMPKIN_SEEDS || seeds.getItem() == Items.MELON_SEEDS){
					// かぼちゃ or スイカ
					if (state.getBlock() == Blocks.FARMLAND) {
						seeds.onItemUse(new ItemUseContext(dummy, Hand.MAIN_HAND, new BlockRayTraceResult(new Vec3d(1.0f, 1.0f, 1.0f), Direction.UP, plantPos, true)));
					}
				}else{
					if ( ConfigValue.farming.CanFarming()){
						// i耕せる土地なら強制的に耕す
						Block blk = state.getBlock();
						if ((Blocks.DIRT == blk || Blocks.GRASS_BLOCK == blk) && world.getBlockState(plantPos.offset(Direction.UP)).getMaterial() == Material.AIR){
							world.setBlockState(plantPos,Blocks.FARMLAND.getDefaultState().with(FarmlandBlock.MOISTURE, 7));
						}
					}
					// iその他
					seeds.onItemUse(new ItemUseContext(dummy, Hand.MAIN_HAND, new BlockRayTraceResult(new Vec3d(1.0f, 1.0f, 1.0f), Direction.UP, plantPos, true)));
				}
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
					// i配信に成功したら元の素材を1減らす
					deliver.shrink(1);
				}
			}
		}
	}

	private void supplyBoneMeal() {
		// i直上がコンポスターなら骨粉を補充
		int size = Math.min(Items.BONE_MEAL.getMaxStackSize(), this.toolInventory.getInventoryStackLimit());
		if ((this.toolInventory.getStackInSlot(SLOT_MEAL).isEmpty()) ||
				(!this.toolInventory.getStackInSlot(SLOT_MEAL).isEmpty() && this.toolInventory.getStackInSlot(SLOT_MEAL).getCount() < size)) {

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
							 // i余ってるサイズを計算
							 ItemStack invStack = this.toolInventory.getStackInSlot(SLOT_MEAL);
							 int count = size - invStack.getCount();
							 if (invStack.isEmpty()) {
								 // i元々空っぽなら限界まで
								 count = this.toolInventory.getInventoryStackLimit();
							 }
							 // iコンポスタ側のスタック数と比較して小さい方を採用
							 int newCount = Math.min(count, itemstack.getCount());
							 // iインベントリにアイテム設定
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
		// i最大範囲の1/10+1箇所適当に肥料をばらまく
		ItemStack stack = this.toolInventory.getStackInSlot(SLOT_MEAL);
		dummy.setHeldItem(Hand.MAIN_HAND, stack);
		if (!stack.isEmpty()) {
			int max = ConfigValue.farming.MaxDistance();
			int chance = Math.min(world.rand.nextInt(((max * max)/10)+2) + 1, stack.getCount());
			for (int i = 0; i < chance; i++) {
				int point = world.rand.nextInt(next_x.length);
				for (int y = -ConfigValue.farming.MaxHeight(); y <= ConfigValue.farming.MaxHeight(); y++){
					BlockPos scpos = new BlockPos(this.pos.add(next_x[point], y , next_z[point]));
					BlockState state = world.getBlockState(scpos);
					if (state.getBlock() instanceof CropsBlock) {
						stack.onItemUse(new ItemUseContext((PlayerEntity)dummy, Hand.MAIN_HAND,  new BlockRayTraceResult(new Vec3d(1.0D, 1.0D, 1.0D), Direction.UP, scpos, true)));
						break;
					}else if (state.getBlock() == Blocks.COCOA) {
						stack.onItemUse(new ItemUseContext((PlayerEntity)dummy, Hand.MAIN_HAND,  new BlockRayTraceResult(new Vec3d(1.0D, 1.0D, 1.0D), Direction.UP, scpos, true)));
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
		}else if(slot == 1 && stack.getItem() instanceof ItemSickle) {
			ret = true;
		}else if(slot == 2 && stack.getItem() == Items.BONE_MEAL) {
			ret = true;
		}
		return ret;
	}

	public boolean isValidatePlanter(int slot, ItemStack stack) {
		return ConfigValue.farming.CheckItem(stack);
	}

	public boolean isValidateHarvester(int slot, ItemStack stack) {
		return false;
	}
}
