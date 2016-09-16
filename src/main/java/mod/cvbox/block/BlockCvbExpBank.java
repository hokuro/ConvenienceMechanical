package mod.cvbox.block;

import java.util.Random;

import mod.cvbox.core.FileManager;
import mod.cvbox.core.ModCommon;
import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.core.PlayerExpBank;
import mod.cvbox.network.MessageExperienceInfo;
import mod.cvbox.tileentity.TileEntityExpBank;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCvbExpBank extends BlockContainer {

	public static final PropertyDirection FACING = BlockDirectional.FACING;

	public BlockCvbExpBank(Material material){
		super(material);
		setHardness(0.5F);
		this.setStepSound(SoundType.ANVIL);
		this.setResistance(2000.0F);
		this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ){
		TileEntityExpBank tileEntity = (TileEntityExpBank)worldIn.getTileEntity(pos);
		if ((tileEntity == null) || (playerIn == null)){
			return false;
		}
		if ((playerIn.capabilities.isCreativeMode) && (playerIn.isSneaking())){
			return true;
		}
		if (playerIn.isSneaking()){
			return false;
		}
		if(!worldIn.isRemote){
			FileManager.read_file(playerIn.getPersistentID().toString());
		}
		CreateMessage(playerIn);
		playerIn.openGui(Mod_ConvenienceBox.instance,  ModCommon.GUIID_EXPBANK, worldIn, pos.getX(),pos.getY(),pos.getZ());
		return true;
	}

	@Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.INVISIBLE;
    }
	@Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

	@Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

	@Override
    public boolean isFullBlock(IBlockState state)
    {
        return false;
    }

	@Override
	public boolean getUseNeighborBrightness(IBlockState state){
		return true;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune){
		return Item.getItemFromBlock(this);
	}

	@Override
	public int quantityDropped(Random random){
		return 1;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		try{
			return new TileEntityExpBank();
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
    	EnumFacing fc = BlockPistonBase.getFacingFromEntity(pos, placer);
        return this.getDefaultState().withProperty(FACING, fc);
    }

	@Override
	public IBlockState getStateFromMeta(int meta){
		return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state){
		return ((EnumFacing) state.getValue(FACING)).getIndex();
	}

	@Override
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, new IProperty[]{FACING});
	}

	public static void update_exp(World worldIn, BlockPos pos, int mode, int Input, EntityPlayer player){
		TileEntityExpBank ent = (TileEntityExpBank)worldIn.getTileEntity(pos);
		if (ent == null){
			return;
		}

		int list_no = -1;
		int box_exp = 0;
		int add_player_exp = 0;
		int sub_player_exp = 0;
		int exp = 0;

		if (player == null){
			return;
		}
		String player_uuid = player.getPersistentID().toString();
		for (int n = 0; n < PlayerExpBank.player_name.size(); n++){
			if (((String) PlayerExpBank.player_name.get(n)).equals(player_uuid)){
				list_no = n;
				box_exp = ((Integer) PlayerExpBank.box_exp.get(n)).intValue();
				break;
			}
		}

		if (list_no == -1){
			PlayerExpBank.player_name.add(player_uuid);
			PlayerExpBank.box_exp.add(Integer.valueOf(0));
			list_no = PlayerExpBank.player_name.size() -1;
			box_exp =0;
		}

		if(list_no < 0){
			return;
		}
		if ((mode ==1) || (mode == 3)){
			if (mode == 1){
				exp = 0;
				add_player_exp = box_exp;
			}else if (mode == 3){
				if (Input > box_exp){
					add_player_exp = box_exp;
					exp = 0;
				}else{
					add_player_exp = Input;
					exp = box_exp - Input;
				}
			}
			if (Integer.MAX_VALUE < ((Integer)PlayerExpBank.player_exp.get(list_no)).intValue() + add_player_exp){
				return;
			}
			if (((String)PlayerExpBank.player_name.get(list_no)).equals(player_uuid)){
				PlayerExpBank.box_exp.set(list_no, Integer.valueOf(exp));
				add_experience(player,add_player_exp);
				FileManager.save_file(player_uuid);
				CreateMessage(player);
			}
		}else{ if((mode == 2) || (mode == 4)){
			if(PlayerExpBank.MAX_EXP >= ((Integer)PlayerExpBank.box_exp.get(list_no)).intValue() + Input){
				if(((Integer) PlayerExpBank.player_exp.get(list_no)).intValue() >= Input){
					sub_player_exp = Input;
					exp = box_exp + Input;
				}else{
					sub_player_exp = ((Integer) PlayerExpBank.player_exp.get(list_no)).intValue();
					exp = box_exp +((Integer)PlayerExpBank.player_exp.get(list_no)).intValue();
				}
			}else{
				int temp = PlayerExpBank.MAX_EXP - ((Integer) PlayerExpBank.box_exp.get(list_no)).intValue();
				sub_player_exp = temp;
				exp = PlayerExpBank.MAX_EXP;
			}
			if (((String)PlayerExpBank.player_name.get(list_no)).equals(player_uuid)){
				PlayerExpBank.box_exp.set(list_no, Integer.valueOf(exp));

				int temp = ((Integer)PlayerExpBank.player_exp.get(list_no)).intValue() - sub_player_exp;
				player.experienceTotal = 0;
				player.experienceLevel = 0;
				player.experience = 0.0F;

				add_experience(player,temp);
				FileManager.save_file(player_uuid);
				CreateMessage(player);
			}
		}else if (mode == 7){
			if (player.experienceLevel > Input){
				int tmp = 0;
				for (int n = 0; n < Input; n++){
					tmp += xpBarCap(n);
				}
				if (PlayerExpBank.MAX_EXP < box_exp + (((Integer) PlayerExpBank.player_exp.get(list_no)).intValue() - tmp)){
					return;
				}
				exp = box_exp + (((Integer) PlayerExpBank.player_exp.get(list_no)).intValue() -tmp);

				player.experienceTotal = 0;
				player.experienceLevel = 0;
				player.experience = 0.0F;

				add_experience(player,tmp);
				while((player.experienceLevel < Input) && (exp >0)){
					add_experience(player,1);
					exp--;
				}
				PlayerExpBank.box_exp.set(list_no, Integer.valueOf(exp));
				FileManager.save_file(player_uuid);
				CreateMessage(player);
			} else {
				int tmp = 0;
				for (int n = 0; n < Input; n++) {
					tmp += xpBarCap(n);
				}
				if (tmp < 0) {
					return;
				}
				int sub = tmp - ((Integer) PlayerExpBank.player_exp.get(list_no)).intValue();
				if (((Integer) PlayerExpBank.box_exp.get(list_no)).intValue() - sub < 0) {
					return;
				}
				exp = ((Integer) PlayerExpBank.box_exp.get(list_no)).intValue() - sub;

				add_experience(player, sub);
				while ((player.experienceLevel < Input) && (exp > 0)) {
					add_experience(player, 1);
					exp--;
				}
				PlayerExpBank.box_exp.set(list_no, Integer.valueOf(exp));

				FileManager.save_file(player_uuid);

				CreateMessage(player);
			}
		}
	}

	}
	public static void CreateMessage(EntityPlayer player){
		String player_uuid = player.getPersistentID().toString();

		int list_no = -1;
		int box_exp = 0;
		for (int n = 0; n < PlayerExpBank.player_name.size(); n++){
			if(((String) PlayerExpBank.player_name.get(n)).equals(player_uuid)){
				list_no = n;
				box_exp = ((Integer)PlayerExpBank.box_exp.get(n)).intValue();
				break;
			}
		}
		if (list_no == -1){
			PlayerExpBank.player_name.add(player_uuid);
			PlayerExpBank.box_exp.add(Integer.valueOf(0));
			PlayerExpBank.player_exp.add(Integer.valueOf(0));
			list_no = PlayerExpBank.player_name.size() -1;
			box_exp = 0;
		}
		int tmp = 0;
		for (int n = 0; n < player.experienceLevel; n++){
			tmp += xpBarCap(n);
		}
		int bar_exp = (int)(player.experience * player.xpBarCap());
		int player_exp = tmp + bar_exp;
		PlayerExpBank.player_exp.set(list_no, Integer.valueOf(player_exp));
		if(player instanceof EntityPlayerMP){
			Mod_ConvenienceBox.Net_Instance.sendTo(new MessageExperienceInfo(player_exp, box_exp), (EntityPlayerMP)player);
		}
	}

	public static void add_experience(EntityPlayer entityplayer, int p){
		int j = Integer.MAX_VALUE - entityplayer.experienceTotal;
		if(p > j){
			p = j;
		}
		entityplayer.experience += p/entityplayer.xpBarCap();
		for (entityplayer.experienceTotal += p; entityplayer.experience >= 1.0F; entityplayer.experience /= entityplayer.xpBarCap()){
			entityplayer.experience = ((entityplayer.experience -1.0F) * entityplayer.xpBarCap());
					entityplayer.addExperienceLevel(1);
		}
	}

	public static int xpBarCap(int lv){
		return ((lv >= 15) ? (37 +(lv-15)*5) : ((lv >= 30) ? (112+(lv-30) * 9) : (7 + lv * 2)));
	}
}
