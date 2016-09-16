package mod.cvbox.block;

import java.util.Random;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import mod.cvbox.config.ConfigValue;
import mod.cvbox.core.AutoPlanting;
import mod.cvbox.core.ModCommon;
import mod.cvbox.entity.EntityPlayerDM;
import mod.cvbox.entity.TileEntityAutoPlanting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemDye;
//import net.minecraft.util.IIcon;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCvbPlanter extends BlockContainer{
	private int[] chkX;
	private int[] chkY;
	private int[] chkZ;
	private int[] chk;
	private int chkCounter = 0;
	private int maxFarland;
	private int sowDistanceMax;
	private static EntityPlayerDM player = null;
	private ItemStack[] tagList = new ItemStack[ModCommon.PLANTER_CONTENASIZE];
	private int tagNum = 0;
	private final Random random = new Random();

	public BlockCvbPlanter() {
		super(Material.wood);
		maxFarland = ConfigValue.General.MaxFarmland;
		sowDistanceMax = ConfigValue.General.MaxDistance;
		chkCounter = 0;
		chkX = new int[maxFarland + 1];
		chkY = new int[maxFarland + 1];
		chkZ = new int[maxFarland + 1];
		chk = new int[this.maxFarland + 1];
		for (int q = 0; q < maxFarland + 1; q++){
			chkX[q] = 0;
			chkY[q] = 0;
			chkZ[q] = 0;
			chk[q] = -1;
		}
		setTickRandomly(true);
		this.setStepSound(SoundType.WOOD);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state){
		return false;
	}

	@Override
	public boolean isFullBlock(IBlockState state){
		return false;
	}

	@Override
	public boolean getUseNeighborBrightness(IBlockState state){
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par2){
		return new TileEntityAutoPlanting();
	}

    /**返しているのは「切り抜き」「ミップマップされた」である。*/
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer(){
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    /**
     RenderPassに渡される数値はJsonで指定したtintindexで、tintindexを指定しないとこのメソッドは呼ばれない。
     @see #getRenderColor
     */
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass){
        return ItemDye.dyeColors[renderPass];
    }


	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ){
		if ((!worldIn.isRemote) && (!playerIn.isSneaking())){
			playerIn.openGui(AutoPlanting.instance, AutoPlanting.guiIdAutoSowSeed, worldIn, pos.getX(),pos.getY(),pos.getZ());
			return true;
		}
		if (AutoPlanting.rightClick != true){
			return true;
		}
		int ret;
		if (playerIn.getHeldEquipment() == null){
			ret = Exec(worldIn,pos.getX(),pos.getY(),pos.getZ(),playerIn);
		}
		return true;
	}

	@Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
		BlockPos pos2 = pos.add(0, 1, 0);
		boolean flag = (worldIn.isBlockIndirectlyGettingPowered(pos)) > 0 || (worldIn.isBlockIndirectlyGettingPowered(pos2) > 0);
		if (flag) {
			worldIn.scheduleBlockUpdate(pos, BlockCore.block_planter, tickRate(worldIn),0);
		}
    }

	@Override
	 public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
		int ret;
		if ((!worldIn.isRemote) &&
				((worldIn.isBlockIndirectlyGettingPowered(pos)>0) ||
				(worldIn.isBlockPowered(pos)))) {
			GameProfile gp = new GameProfile(UUID.randomUUID(), "cvb_plunter");

			player = new EntityPlayerDM(worldIn, gp);
			ret = Exec(worldIn, pos.getX(),pos.getY(),pos.getZ(), player);
		}
    }

	@Override
	public int damageDropped(IBlockState state) {
		return 0;
	}

	@Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state )
    {
		TileEntityAutoPlanting tile = (TileEntityAutoPlanting) worldIn.getTileEntity(pos);
		if (tile != null) {
			for (int i1 = 0; i1 < tile.getSizeInventory(); i1++) {
				ItemStack itemstack = tile.getStackInSlot(i1);
				if (itemstack != null) {
					float f = random.nextFloat() * 0.8F + 0.1F;
					float f1 = random.nextFloat() * 0.8F + 0.1F;
					EntityItem entityitem;
					for (float f2 = random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; worldIn.spawnEntityInWorld(entityitem)) {
						int j1 = random.nextInt(21) + 10;
						if (j1 > itemstack.stackSize) {
							j1 = itemstack.stackSize;
						}
						itemstack.stackSize -= j1;
						entityitem = new EntityItem(worldIn, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2,
								new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
						float f3 = 0.05F;
						entityitem.motionX = ((float) random.nextGaussian() * f3);
						entityitem.motionY = ((float) random.nextGaussian() * f3 + 0.2F);
						entityitem.motionZ = ((float) random.nextGaussian() * f3);
						if (itemstack.hasTagCompound()) {
							entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
						}
					}
				}
			}
			worldIn. updateComparatorOutputLevel(pos,state.getBlock());
		}
		super.breakBlock(worldIn, pos, state);
    }

	private int Exec(World world, int i, int j, int k, EntityPlayer entityplayer){
		IBlockState meta = world.getBlockState(new BlockPos(i,j,k));
		boolean iss = getList(world, i, j, k);
		if (!iss){
			return -1;
		}

		Block chkBlockId = meta.getBlock();
		if (chkBlockId == Blocks.air){
			return 0;
		}
		int ret = SeedCheck(world, i, j, k);
		if (ret==0){
			ret = SeedSet(world, entityplayer, new BlockPos(i,j,k));
		}
		for ( int q = 0; q < maxFarland + 1; q++){
			chkX[q] = 0;
			chkY[q] = 0;
			chkZ[q] = 0;
			chk[q] = -1;
		}
		chkCounter = 0;

		return 0;
	}

	private boolean getList(World world, int i, int j, int k){
		TileEntityAutoPlanting tile = (TileEntityAutoPlanting) world.getTileEntity(new BlockPos(i,j,k));

		int num = 0;
		for (int m = 0; m < ModCommon.PLANTER_CONTENASIZE; m++){
			ItemStack is = tile.getStackInSlot(m);
			if (is != null){
				tagList[(num++)] = is;
			}
		}
		tagNum = num;
		if (tagNum != 0){
			return true;
		}
		return false;
	}


	private int SeedCheck(World world, int i, int j, int k){
		chkX[0] = i;
		chkY[0] = j;
		chkZ[0] = k;
		chk[0] = 0;
		chkCounter = 1;

		if (!checkPower(world, new BlockPos(i,j,k))){return 1;}
		int cou = 0;
		for (int m = 0; m < sowDistanceMax; m++){
			while (chk[cou] == m){
				int i2 = chkX[cou];
				int j2 = chkY[cou];
				int k2 = chkZ[cou];
				for ( int xx = i2 -1; xx <= i2+1; xx++){
					for (int yy = j2 -1; yy <= j2+1; yy++){
						for (int zz = k2-1; zz <= k2+1; zz++){
							if ((world.getBlockState(new BlockPos(xx,yy,zz)).getBlock() == Blocks.farmland) &&
									((xx!=i) ||(zz != k))){
								if(!check(xx,yy,zz)){
									if (chkCounter > maxFarland){
										return 1;
									}
									this.chkX[this.chkCounter] = xx;
									this.chkY[this.chkCounter] = yy;
									this.chkZ[this.chkCounter] = zz;
									this.chk[this.chkCounter] = (m + 1);
									this.chkCounter += 1;
								}
							}
						}
					}
				}
				cou++;
				if (cou > maxFarland){
					return 1;
				}
			}
		}
		return 0;
	}

	private int SeedSet(World world, EntityPlayer entityplayer, BlockPos pos){
		boolean ret;
		TileEntityAutoPlanting tile = (TileEntityAutoPlanting) world.getTileEntity(pos);

		for (int p = 0; p < chkCounter; p++){
			Block tagPos = world.getBlockState(new BlockPos(chkX[p],chkY[p],chkZ[p])).getBlock();
			Block tagPos2 = world.getBlockState(new BlockPos(chkX[p],chkY[p]+1,chkZ[p])).getBlock();
			if ((tagPos == Blocks.farmland) &&
					(tagPos2 == Blocks.air)){
				for ( int y2 = chkY[0] -1; y2 <= chkY[0]+1; y2++){
					for ( int x2 = chkX[0] -1; x2 <= chkX[0]+1; x2++){
						for ( int z2 = chkZ[0] -1; z2 <= chkZ[0]+1; z2++){
							for ( int fi = 0; fi < tile.getSizeInventory(); fi++){
								ItemStack item = tile.getStackInSlot(fi);
								if (item != null){
									ItemStack dummy = tile.getStackInSlot(fi);
									if ((EnumActionResult.SUCCESS == item.getItem().onItemUse(dummy, entityplayer, world, new BlockPos(chkX[p], chkY[p], chkZ[p]), EnumHand.MAIN_HAND, EnumFacing.UP, 0.0F, 0.0F, 0.0F))){

										if (tile.getStackInSlot(fi).stackSize == 0){
											tile.decrStackSize(fi,1);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return 0;
	}

	private boolean check(int i, int j, int k) {
		for (int m = 0; m < this.chkCounter; m++) {
			if ((this.chkX[m] == i) && (this.chkY[m] == j) && (this.chkZ[m] == k)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkPower(World worldIn, BlockPos pos){
		BlockPos pos1 = pos.add(1,0,0);
		BlockPos pos2 = pos.add(-1,0,0);
		BlockPos pos3 = pos.add(0,0,1);
		BlockPos pos4 = pos.add(0,0,-1);
		BlockPos pos5 = pos.add(0,1,0);
		BlockPos pos6 = pos.add(1,1,0);
		BlockPos pos7 = pos.add(-1,1,0);
		BlockPos pos8 = pos.add(0,1,1);
		BlockPos pos9 = pos.add(0,1,-1);

		return worldIn.isBlockIndirectlyGettingPowered(pos) > 0 ||
		worldIn.isBlockIndirectlyGettingPowered(pos1) > 0 ||
		worldIn.isBlockIndirectlyGettingPowered(pos2) > 0 ||
		worldIn.isBlockIndirectlyGettingPowered(pos3) > 0 ||
		worldIn.isBlockIndirectlyGettingPowered(pos4) > 0 ||
		worldIn.isBlockIndirectlyGettingPowered(pos5) > 0 ||
		worldIn.isBlockIndirectlyGettingPowered(pos6) > 0 ||
		worldIn.isBlockIndirectlyGettingPowered(pos7) > 0 ||
		worldIn.isBlockIndirectlyGettingPowered(pos8) > 0 ||
		worldIn.isBlockIndirectlyGettingPowered(pos9) > 0;
	}
}

