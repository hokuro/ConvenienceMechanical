package mod.cvbox.core;

import java.util.ArrayList;

import mod.cvbox.block.BlockCore;
import mod.cvbox.block.BlockSuperAnvil;
import mod.cvbox.event.McEventHandler;
import mod.cvbox.gui.GuiHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = ModCommon.MOD_ID, name = ModCommon.MOD_NAME, version = ModCommon.MOD_VERSION)
public class ReAnvile {
	@Mod.Instance(ModCommon.MOD_ID)
	public static ReAnvile instance;
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModCommon.MOD_CHANEL);
	public static final McEventHandler mcEvent = new McEventHandler();

	public static final BlockSuperAnvil anv = new BlockSuperAnvil();

	@EventHandler
	public void construct(FMLConstructionEvent event) {
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ConfigValue.init(event);
		BlockCore.registerBlock(event);
//		try{
//			GameRegistry.addSubstitutionAlias("minecraft:anvil", Type.BLOCK, anv);
//			GameRegistry.addSubstitutionAlias("minecraft:anvil", Type.ITEM, new ItemAnvilBlock(anv));
//		}catch(Exception ex){
//			ModLog.log().fatal(ex.getStackTrace().toString());
//		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

		MinecraftForge.EVENT_BUS.register(new McEventHandler());
        NetworkRegistry.INSTANCE.registerGuiHandler(this.instance, new GuiHandler());

	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		for (Enchantment ench: Enchantment.enchantmentRegistry){
			if (ench != null){
				String enchName = Utils.getEnchName(ench);
				int defaultLimit = ench.getMaxLevel();
				int enchLimit = ConfigValue.EnchantmentLimits(enchName, defaultLimit);
				ConfigValue.General.ENCHANT_LIMITS.put(Enchantment.getEnchantmentID(ench), enchLimit);
				ArrayList<String> defaultBlackList = new ArrayList<String>();
				for (Enchantment ench1: Enchantment.enchantmentRegistry){
					if (ench1 != null && Enchantment.getEnchantmentID(ench1) != Enchantment.getEnchantmentID(ench) && !ench.canApplyTogether(ench1)){
						String ench1Name = Utils.getEnchName(ench1);
						defaultBlackList.add(ench1Name);
					}
				}
				String[] enchBlackList = ConfigValue.EnchantmentBlack(enchName, defaultBlackList.toArray(new String[0]));
                ConfigValue.General.ENCHANT_BLACK_LIST.put(Enchantment.getEnchantmentID(ench), enchBlackList);
				ConfigValue.save();
			}
		}
		GameRegistry.addRecipe(new ItemStack(BlockCore.getBlock(BlockCore.NAME_BLOCKREANVILE),1,0),
				"000",
				" 1 ",
				"111",
				'0',Blocks.diamond_block,
				'1',Items.iron_ingot);
	}

	@EventHandler
	public void updateAlphaAnvil(FMLMissingMappingsEvent event){
		for (int i = 0; i < event.get().size(); i++){
			final MissingMapping missingMapping = event.get().get(i);
			if (missingMapping.name.equals("ReAnvile:reanvil")){
				switch(missingMapping.type){
				case BLOCK:
					missingMapping.remap(anv);;
					break;
				case ITEM:
					missingMapping.remap(Item.getItemFromBlock(anv));
					break;
				}
			}
		}
	}
}
