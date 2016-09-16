package mod.cvbox.core;
import java.util.Map;

import mod.cvbox.block.BlockCore;
import mod.cvbox.block.BlockCvbExpBank;
import mod.cvbox.core.log.ModLog;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRegister {

	public static void RegisterBlock(FMLPreInitializationEvent event){

		Map<String,Block> blockMap =null;//= BlockFoods.blockMap();
		Map<String,Item> itemMap  =null;//= BlockFoods.itemMap();
		Map<String, ResourceLocation[]> resourceMap  =null;//= BlockFoods.resourceMap();

		for (String key : blockMap.keySet()){
			if (ModCommon.isDebug){ModLog.log().debug("ブロック登録:"+key+"登録");};
			GameRegistry.register(blockMap.get(key),new ResourceLocation(ModCommon.MOD_ID+":"+key));
			GameRegistry.register(itemMap.get(key),new ResourceLocation(ModCommon.MOD_ID+":"+key));
			if (ModCommon.isDebug){ModLog.log().debug("ブロック登録:"+key+"登録完了");}
		}

		if ( event.getSide().isClient())
		{
			for ( String key: resourceMap.keySet()){
				if (ModCommon.isDebug){ModLog.log().debug("モデル登録:"+ key+"登録");}
				Item witem = Item.getItemFromBlock(blockMap.get(key));
				ResourceLocation[] wresource = resourceMap.get(key);
				if (wresource.length > 1){
					ModelLoader.registerItemVariants(witem, wresource);
				}
				for (int i = 0; i < wresource.length; i++){
					ModelLoader.setCustomModelResourceLocation(witem, i,
							new ModelResourceLocation(wresource[i], "inventory"));
					if (ModCommon.isDebug){ModLog.log().debug("モデル登録:リソース:"+ wresource[i]);}
				}
				if (ModCommon.isDebug){ModLog.log().debug("モデル登録:"+ key+"登録完了");}
			}
		};
	}

	public static void RegisterItem(FMLPreInitializationEvent event){
		Map<String,Item> itemMap  =null;
		Map<String, ModelResourceLocation[]> resourceMap =null;

		for (String key : itemMap.keySet()){
			GameRegistry.register(itemMap.get(key),new ResourceLocation(key));
		}

        //テクスチャ・モデル指定JSONファイル名の登録。
        if (event.getSide().isClient()) {
        	for (String key : resourceMap.keySet()){
        		//1IDで複数モデルを登録するなら、上のメソッドで登録した登録名を指定する。
        		int cnt = 0;
        		for (ModelResourceLocation rc : resourceMap.get(key)){
        			ModelLoader.setCustomModelResourceLocation(itemMap.get(key), cnt, rc);
        			cnt++;
        		}
        	}
        }
	}

	public static void RegisterEntity(CommonProxy proxy){
		// タイルエンティティはserverとclientで登録方法が違う為プロキシで分ける
		proxy.registerTileEntity();
	}

	public static void RegisterRender(CommonProxy proxy){
		// レンダーはクライアントの未登録
		proxy.registerRender();
	}

	public static void RegisterRecipe(){
		GameRegistry.addRecipe(new ItemStack(BlockCore.getBlock(BlockCvbExpBank.NAME_EXPBANK), 1, 8),
				new Object[] { "OGO", "ICI", "ERE", Character.valueOf('O'), Blocks.obsidian,
						Character.valueOf('E'), Items.ender_pearl, Character.valueOf('I'), Items.iron_ingot,
						Character.valueOf('R'), Items.redstone, Character.valueOf('C'), Blocks.chest,
						Character.valueOf('G'), Blocks.glass_pane });
		for (int n = 0; n <= 14; n++) {
			for (int m = 0; m <= 16; m++) {
				if (n != m) {
					GameRegistry.addShapelessRecipe(
							new ItemStack(BlockCore.getBlock(BlockCvbExpBank.NAME_EXPBANK), 1, 16-m),
							new Object[] {
							new ItemStack(BlockCore.getBlock(BlockCvbExpBank.NAME_EXPBANK), 1, n),
							new ItemStack(Items.dye, 1, m) });
				}
			}
		}

		GameRegistry.addRecipe(new ItemStack(BlockCore.instance.getBlock(BlockCore.NAME_AUTOPLANTING),1,0),
				new Object[]{"020",
							 "010",
							 "020",
							'0', Items.wheat_seeds,
							'1', Blocks.dropper,
							'2', Items.redstone});
			GameRegistry.addRecipe(new ItemStack(BlockCore.instance.getBlock(BlockCore.NAME_AUTOPLANTING),1,1),
				new Object[]{"020",
							 "010",
							 "030",
							'0', Items.wheat_seeds,
							'1', Blocks.dropper,
							'2', Items.redstone,
							'3', Items.beef});

	}

	public static void RegisterMessage(){
	}
}
