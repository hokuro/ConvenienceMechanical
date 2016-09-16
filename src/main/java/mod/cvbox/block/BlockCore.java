package mod.cvbox.block;

import java.util.HashMap;
import java.util.Map;

import mod.cvbox.block.item.ItemCvbExpBank;
import mod.cvbox.block.item.ItemCvbPlanter;
import mod.cvbox.core.ModCommon;
import mod.cvbox.core.Mod_ConvienienceBox;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAnvilBlock;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

public class BlockCore{
	public static final String NAME_EXPBANK = "expbank";
	public static final String NAME_PLANTER = "planter";
	public static final String NAME_HARVESTER = "harvester";
	public static final String NAME_CRUSHER = "crusher";
	public static final String NAME_COMPRESSER = "compresser";
	public static final String NAME_LIQUIDPUMP = "liquidpump";
	public static final String NAME_RECYCLER = "recycler";
	public static final String NAME_SEPARATOR = "separator";
	public static final String NAME_LAVAGENERATOR = "lavagenerator";
	public static final String NAME_STONEGENERATOR = "stonegenerator";
	public static final String NAME_SUPERANVIL = "superanvil";

	public static final Block block_expbank = new BlockCvbExpBank(Material.rock).setUnlocalizedName(NAME_EXPBANK).setCreativeTab(Mod_ConvienienceBox.tabCvBox);
	public static final Block block_planter = new BlockCvbPlanter().setHardness(0.3F).setUnlocalizedName(NAME_PLANTER).setCreativeTab(Mod_ConvienienceBox.tabCvBox);
	public static final Block block_harvester = new Block(Material.rock).setCreativeTab(Mod_ConvienienceBox.tabCvBox);
	public static final Block block_crusher = new Block(Material.rock).setCreativeTab(Mod_ConvienienceBox.tabCvBox);
	public static final Block block_compresser = new Block(Material.rock).setCreativeTab(Mod_ConvienienceBox.tabCvBox);
	public static final Block block_liquidpump = new Block(Material.rock).setCreativeTab(Mod_ConvienienceBox.tabCvBox);
	public static final Block block_recycler = new Block(Material.rock).setCreativeTab(Mod_ConvienienceBox.tabCvBox);
	public static final Block block_separator = new Block(Material.rock).setCreativeTab(Mod_ConvienienceBox.tabCvBox);
	public static final Block block_lavagenerator = new Block(Material.rock).setCreativeTab(Mod_ConvienienceBox.tabCvBox);
	public static final Block block_stonegenerator = new Block(Material.rock).setCreativeTab(Mod_ConvienienceBox.tabCvBox);
	public static final Block block_superanvile = new BlockSuperAnvil().setCreativeTab(Mod_ConvienienceBox.tabCvBox);

	public static Map<String,Block> getBlock(){
		return (new HashMap<String,Block>(){
			{put(NAME_EXPBANK, block_expbank);}
			{put(NAME_PLANTER, block_planter);}
			{put(NAME_HARVESTER, block_harvester);}
			{put(NAME_CRUSHER, block_crusher);}
			{put(NAME_COMPRESSER, block_compresser);}
			{put(NAME_LIQUIDPUMP, block_liquidpump);}
			{put(NAME_RECYCLER, block_recycler);}
			{put(NAME_SEPARATOR, block_separator);}
			{put(NAME_LAVAGENERATOR, block_lavagenerator);}
			{put(NAME_STONEGENERATOR, block_stonegenerator);}
			{put(NAME_SUPERANVIL, block_superanvile);}
		});
	}

	public static Map<String,Item> getItem(){
		return (new HashMap<String,Item>(){
			{put(NAME_EXPBANK, new ItemCvbExpBank(block_expbank));}
			{put(NAME_PLANTER, new ItemCvbPlanter(block_planter));}
			{put(NAME_HARVESTER, new ItemBlock(block_harvester));}
			{put(NAME_CRUSHER, new ItemBlock(block_crusher));}
			{put(NAME_COMPRESSER, new ItemBlock(block_compresser));}
			{put(NAME_LIQUIDPUMP, new ItemBlock(block_liquidpump));}
			{put(NAME_RECYCLER, new ItemBlock(block_recycler));}
			{put(NAME_SEPARATOR, new ItemBlock(block_separator));}
			{put(NAME_LAVAGENERATOR, new ItemBlock(block_lavagenerator));}
			{put(NAME_STONEGENERATOR, new ItemBlock(block_stonegenerator));}
			{put(NAME_SUPERANVIL, new ItemAnvilBlock(block_superanvile));}
		});
	}
	public static Map<String,ResourceLocation[]> getResource(){
		return (new HashMap<String,ResourceLocation[]>(){
			{put(NAME_EXPBANK, new ResourceLocation[]{
					new ResourceLocation(ModCommon.MOD_ID + ":" + "expbank"),
					new ResourceLocation(ModCommon.MOD_ID + ":" + "expbank_orange"),
					new ResourceLocation(ModCommon.MOD_ID + ":" + "expbank_magenta"),
					new ResourceLocation(ModCommon.MOD_ID + ":" + "expbank_lightblue"),
					new ResourceLocation(ModCommon.MOD_ID + ":" + "expbank_yellow"),
					new ResourceLocation(ModCommon.MOD_ID + ":" + "expbank_pink"),
					new ResourceLocation(ModCommon.MOD_ID + ":" + "expbank_lime"),
					new ResourceLocation(ModCommon.MOD_ID + ":" + "expbank_gray"),
					new ResourceLocation(ModCommon.MOD_ID + ":" + "expbank_lightgray"),
					new ResourceLocation(ModCommon.MOD_ID + ":" + "expbank_cyan"),
					new ResourceLocation(ModCommon.MOD_ID + ":" + "expbank_purple"),
					new ResourceLocation(ModCommon.MOD_ID + ":" + "expbank_blue"),
					new ResourceLocation(ModCommon.MOD_ID + ":" + "expbank_brown"),
					new ResourceLocation(ModCommon.MOD_ID + ":" + "expbank_green"),
					new ResourceLocation(ModCommon.MOD_ID + ":" + "expbank_red"),
					new ResourceLocation(ModCommon.MOD_ID + ":" + "expbank_black")});}
			{put(NAME_PLANTER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_PLANTER)});}
			{put(NAME_HARVESTER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_HARVESTER)});}
			{put(NAME_CRUSHER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_CRUSHER)});}
			{put(NAME_COMPRESSER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_COMPRESSER)});}
			{put(NAME_LIQUIDPUMP, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_LIQUIDPUMP)});}
			{put(NAME_RECYCLER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_RECYCLER)});}
			{put(NAME_SEPARATOR, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_SEPARATOR)});}
			{put(NAME_LAVAGENERATOR, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_LAVAGENERATOR)});}
			{put(NAME_STONEGENERATOR, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_STONEGENERATOR)});}
			{put(NAME_SUPERANVIL, new ResourceLocation[]{
					new ResourceLocation(ModCommon.MOD_ID+":"+NAME_SUPERANVIL+"_intact"),
					new ResourceLocation(ModCommon.MOD_ID+":"+NAME_SUPERANVIL+"_slightly_damaged"),
					new ResourceLocation(ModCommon.MOD_ID+":"+NAME_SUPERANVIL+"_very_damaged")});}
		});

	}
}