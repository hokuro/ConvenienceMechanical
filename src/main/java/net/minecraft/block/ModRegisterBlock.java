package net.minecraft.block;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModRegisterBlock {
	// 名前
	public static final String NAME_BLOCKFUSE = "fuse";
	public static final String NAME_BLOCKNCBOMB = "nuclearexplosive";
	public static final String NAME_BLOCKTUNNELBOMB = "tunnelexplosive";

//	// ブロック
//	public static final Block block_Fuse = new BlockFuse()
//			.setHardness(0.0F)
//			.setStepSound(SoundType.PLANT)
//			.setUnlocalizedName(NAME_BLOCKFUSE);
//	public static final Block block_NCBomb = new BlockNuclearExplosive()
//			.setHardness(0.0F)
//			.setStepSound(SoundType.PLANT)
//			.setUnlocalizedName(NAME_BLOCKNCBOMB);
//	public static final Block bolock_TunnelBomb = new BlockTunnelExplosive()
//			.setHardness(0.0F)
//			.setStepSound(SoundType.PLANT)
//			.setUnlocalizedName(NAME_BLOCKTUNNELBOMB);

	public static void registerBlock(FMLPreInitializationEvent event){
//		GameRegistry.register(block_Fuse, new ResourceLocation(ModCommon.MOD_ID+":"+NAME_BLOCKFUSE));
//		GameRegistry.register(new ItemBlock(block_Fuse),new ResourceLocation(ModCommon.MOD_ID+":"+NAME_BLOCKFUSE));
//
//		GameRegistry.register(block_NCBomb,new ResourceLocation(ModCommon.MOD_ID+":"+NAME_BLOCKNCBOMB));
//		GameRegistry.register(new ItemBlock(block_NCBomb),new ResourceLocation(ModCommon.MOD_ID+":"+NAME_BLOCKNCBOMB));
//
//		GameRegistry.register(bolock_TunnelBomb,new ResourceLocation(ModCommon.MOD_ID+":"+NAME_BLOCKTUNNELBOMB));
//		GameRegistry.register(new ItemBlock(bolock_TunnelBomb),new ResourceLocation(ModCommon.MOD_ID+":"+NAME_BLOCKTUNNELBOMB));
//
//		if (event.getSide().isClient()){
//			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block_Fuse), 0,
//					new ModelResourceLocation(new ResourceLocation(ModCommon.MOD_ID + ":"+NAME_BLOCKFUSE),"inventory"));
//
//			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block_NCBomb), 0,
//					new ModelResourceLocation(new ResourceLocation(ModCommon.MOD_ID + ":"+NAME_BLOCKNCBOMB),"inventory"));
//
//			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(bolock_TunnelBomb), 0,
//					new ModelResourceLocation(new ResourceLocation(ModCommon.MOD_ID + ":"+NAME_BLOCKTUNNELBOMB),"inventory"));
//		}
	}
}
