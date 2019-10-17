package mod.cvbox.entity;

import mod.cvbox.block.BlockCore;
import mod.cvbox.core.ModCommon;
import mod.cvbox.tileentity.factory.TileEntityBedrockMaker;
import mod.cvbox.tileentity.factory.TileEntityCompresser;
import mod.cvbox.tileentity.factory.TileEntityCrusher;
import mod.cvbox.tileentity.factory.TileEntityDeliverBox;
import mod.cvbox.tileentity.factory.TileEntityDestroyer;
import mod.cvbox.tileentity.factory.TileEntityExpCollector;
import mod.cvbox.tileentity.factory.TileEntityGenomAnalyser;
import mod.cvbox.tileentity.factory.TileEntityIPSCellMaker;
import mod.cvbox.tileentity.factory.TileEntityKiller;
import mod.cvbox.tileentity.factory.TileEntityLiquidMaker;
import mod.cvbox.tileentity.factory.TileEntityLivingCreator;
import mod.cvbox.tileentity.factory.TileEntitySetter;
import mod.cvbox.tileentity.factory.TileEntityStraw;
import mod.cvbox.tileentity.factory.TileEntityVacumer;
import mod.cvbox.tileentity.farm.TileEntityAfforestation;
import mod.cvbox.tileentity.farm.TileEntityAutoFeed;
import mod.cvbox.tileentity.farm.TileEntityFarming;
import mod.cvbox.tileentity.farm.TileEntityMillking;
import mod.cvbox.tileentity.farm.TileEntityWoolCutting;
import mod.cvbox.tileentity.work.TileEntityExEnchantmentTable;
import mod.cvbox.tileentity.work.TileEntityExpBank;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;

public class EntityCore {
	public static TileEntityType<?> ExpBank;
	public static TileEntityType<?> ExEnchantmentTable;
	public static TileEntityType<?> Setter;
	public static TileEntityType<?> Killer;
	public static TileEntityType<?> ExpCollector;
	public static TileEntityType<?> Crusher;
	public static TileEntityType<?> Compresser;
	public static TileEntityType<?> Straw;
	public static TileEntityType<?> Destroyer;
	public static TileEntityType<?> LiquidMaker;
	public static TileEntityType<?> Vacumer;
	public static TileEntityType<?> Afforestation;
	public static TileEntityType<?> Farming;
	public static TileEntityType<?> Millking;
	public static TileEntityType<?> WoolCutting;
	public static TileEntityType<?> AutoFeed;
	public static TileEntityType<?> GenomAnalyser;
	public static TileEntityType<?> IPSCellMaker;
	public static TileEntityType<?> LivingCreater;
	public static TileEntityType<?> BedrockMaker;
	public static TileEntityType<?> Deliverbox;


	private EntityCore(){

	}

	public static void register(Register<EntityType<?>> event){

	}

	public static void registerTE(final RegistryEvent.Register<TileEntityType<?>> event) {

		ExpBank = TileEntityType.Builder.create(TileEntityExpBank::new, BlockCore.block_expbank).build(null);
		ExpBank.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityExpBank.NAME));
		event.getRegistry().register(ExpBank);

		ExEnchantmentTable = TileEntityType.Builder.create(TileEntityExEnchantmentTable::new, BlockCore.block_exenchanter).build(null);
		ExEnchantmentTable.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityExEnchantmentTable.NAME));
		event.getRegistry().register(ExEnchantmentTable);

		Setter = TileEntityType.Builder.create(TileEntitySetter::new, BlockCore.block_setter).build(null);
		Setter.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntitySetter.NAME));
		event.getRegistry().register(Setter);

		Killer = TileEntityType.Builder.create(TileEntityKiller::new, BlockCore.block_killer).build(null);
		Killer.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityKiller.NAME));
		event.getRegistry().register(Killer);

		ExpCollector = TileEntityType.Builder.create(TileEntityExpCollector::new, BlockCore.block_excollector).build(null);
		ExpCollector.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityExpCollector.NAME));
		event.getRegistry().register(ExpCollector);

		Crusher = TileEntityType.Builder.create(TileEntityCrusher::new, BlockCore.block_crusher).build(null);
		Crusher.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityCrusher.NAME));
		event.getRegistry().register(Crusher);

		Compresser = TileEntityType.Builder.create(TileEntityCompresser::new, BlockCore.block_compresser).build(null);
		Compresser.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityCompresser.NAME));
		event.getRegistry().register(Compresser);

		Straw = TileEntityType.Builder.create(TileEntityStraw::new, BlockCore.block_straw).build(null);
		Straw.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityStraw.NAME));
		event.getRegistry().register(Straw);

		Destroyer = TileEntityType.Builder.create(TileEntityDestroyer::new, BlockCore.block_destroyer).build(null);
		Destroyer.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityDestroyer.NAME));
		event.getRegistry().register(Destroyer);

		LiquidMaker = TileEntityType.Builder.create(TileEntityLiquidMaker::new, BlockCore.block_liquidmaker).build(null);
		LiquidMaker.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityLiquidMaker.NAME));
		event.getRegistry().register(LiquidMaker);

		Vacumer = TileEntityType.Builder.create(TileEntityVacumer::new, BlockCore.block_vacumer).build(null);
		Vacumer.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityVacumer.NAME));
		event.getRegistry().register(Vacumer);

		Afforestation = TileEntityType.Builder.create(TileEntityAfforestation::new, BlockCore.block_afforestation).build(null);
		Afforestation.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityAfforestation.NAME));
		event.getRegistry().register(Afforestation);

		Farming = TileEntityType.Builder.create(TileEntityFarming::new, BlockCore.block_farming).build(null);
		Farming.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityFarming.NAME));
		event.getRegistry().register(Farming);

		Millking = TileEntityType.Builder.create(TileEntityMillking::new, BlockCore.block_millking).build(null);
		Millking.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityMillking.NAME));
		event.getRegistry().register(Millking);

		WoolCutting = TileEntityType.Builder.create(TileEntityWoolCutting::new, BlockCore.block_woolcutting).build(null);
		WoolCutting.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityWoolCutting.NAME));
		event.getRegistry().register(WoolCutting);


		AutoFeed = TileEntityType.Builder.create(TileEntityAutoFeed::new, BlockCore.block_autofeed).build(null);
		AutoFeed.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityAutoFeed.NAME));
		event.getRegistry().register(AutoFeed);

		GenomAnalyser = TileEntityType.Builder.create(TileEntityGenomAnalyser::new, BlockCore.block_genomanalyser).build(null);
		GenomAnalyser.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityGenomAnalyser.NAME));
		event.getRegistry().register(GenomAnalyser);

		IPSCellMaker = TileEntityType.Builder.create(TileEntityIPSCellMaker::new, BlockCore.block_ipscellmaker).build(null);
		IPSCellMaker.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityIPSCellMaker.NAME));
		event.getRegistry().register(IPSCellMaker);

		LivingCreater = TileEntityType.Builder.create(TileEntityLivingCreator::new, BlockCore.block_livingcreator).build(null);
		LivingCreater.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityLivingCreator.NAME));
		event.getRegistry().register(LivingCreater);

		BedrockMaker = TileEntityType.Builder.create(TileEntityBedrockMaker::new, BlockCore.block_bedrockmaker).build(null);
		BedrockMaker.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityBedrockMaker.NAME));
		event.getRegistry().register(BedrockMaker);

		Deliverbox = TileEntityType.Builder.create(TileEntityDeliverBox::new, BlockCore.block_deliverbox).build(null);
		Deliverbox.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityDeliverBox.NAME));
		event.getRegistry().register(Deliverbox);

	}
}
