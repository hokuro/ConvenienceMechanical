package mod.cvbox.block.work;

import javax.annotation.Nullable;

import mod.cvbox.block.ab.BlockHorizontalContainer;
import mod.cvbox.core.FileManager;
import mod.cvbox.core.PlayerExpBank;
import mod.cvbox.inventory.work.ContainerExpBank;
import mod.cvbox.network.MessageHandler;
import mod.cvbox.tileentity.work.TileEntityExpBank;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockExpBank extends BlockHorizontalContainer {
	public BlockExpBank(Block.Properties property) {
		super(property);
	}

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
		TileEntityExpBank tileEntity = (TileEntityExpBank)worldIn.getTileEntity(pos);
		if ((tileEntity == null) || (playerIn == null)){
			return false;
		}
		if ((playerIn.abilities.isCreativeMode) && (playerIn.isSneaking())){
			return true;
		}
		if (playerIn.isSneaking()){
			return false;
		}
		if(!worldIn.isRemote){
			FileManager.read_file(playerIn.getUniqueID().toString());
			CreateMessage(playerIn);
			NetworkHooks.openGui((ServerPlayerEntity)playerIn,
        			new INamedContainerProvider() {
    					@Override
    					@Nullable
    					public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
    						TileEntity ent = worldIn.getTileEntity(pos);
    						if (ent instanceof TileEntityExpBank) {
    							return new ContainerExpBank(id, playerInv, ent);
    						}
    						return null;
    					}

    					@Override
    					public ITextComponent getDisplayName() {
    						return new TranslationTextComponent("container.expbank");
    					}
    				},
        			(buf)->{
						buf.writeInt(pos.getX());
						buf.writeInt(pos.getY());
						buf.writeInt(pos.getZ());
					});
		}else{

		}
		return true;
	}

	@Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		try{
			return new TileEntityExpBank();
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}

	public static void update_exp(World worldIn, BlockPos pos, int mode, int Input, PlayerEntity player){
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
		String player_uuid = player.getUniqueID().toString();
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
	public static void CreateMessage(PlayerEntity player){
		String player_uuid = player.getUniqueID().toString();

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
		if(player instanceof ServerPlayerEntity){
			MessageHandler.SendMessage_ExpBank_ExperienceInfo(player_exp, box_exp, (ServerPlayerEntity)player);
		}
	}

	public static void add_experience(PlayerEntity entityplayer, int p){
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
