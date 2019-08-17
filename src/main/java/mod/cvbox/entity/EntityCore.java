package mod.cvbox.entity;

import mod.cvbox.core.ModCommon;
import mod.cvbox.tileentity.TileEntityCompresser;
import mod.cvbox.tileentity.TileEntityCrusher;
import mod.cvbox.tileentity.TileEntityDestroyer;
import mod.cvbox.tileentity.TileEntityExEnchantmentTable;
import mod.cvbox.tileentity.TileEntityExpBank;
import mod.cvbox.tileentity.TileEntityExpCollector;
import mod.cvbox.tileentity.TileEntityHarvester;
import mod.cvbox.tileentity.TileEntityKiller;
import mod.cvbox.tileentity.TileEntityLiquidMaker;
import mod.cvbox.tileentity.TileEntityPlanter;
import mod.cvbox.tileentity.TileEntitySetter;
import mod.cvbox.tileentity.TileEntityStraw;
import mod.cvbox.tileentity.TileEntityVacumer;
import mod.cvbox.tileentity.TileEntityWoodHarvester;
import mod.cvbox.tileentity.TileEntityWoodPlanter;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;

public class EntityCore {
	public static TileEntityType<?> ExpBank;
	public static TileEntityType<?> ExEnchantmentTable;
	public static TileEntityType<?> Planter;
	public static TileEntityType<?> Harvester;
	public static TileEntityType<?> WoodPlanter;
	public static TileEntityType<?> WoodHarvester;
	public static TileEntityType<?> Setter;
	public static TileEntityType<?> Killer;
	public static TileEntityType<?> ExpCollector;
	public static TileEntityType<?> Crusher;
	public static TileEntityType<?> Compresser;
	public static TileEntityType<?> Straw;
	public static TileEntityType<?> Destroyer;
	public static TileEntityType<?> LiquidMaker;
	public static TileEntityType<?> Vacumer;


	private EntityCore(){

	}

	public static void register(Register<EntityType<?>> event){

	}

	public static void registerTE(final RegistryEvent.Register<TileEntityType<?>> event) {

		ExpBank = TileEntityType.Builder.create(TileEntityExpBank::new).build(null);
		ExpBank.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityExpBank.NAME));
		event.getRegistry().register(ExpBank);

		ExEnchantmentTable = TileEntityType.Builder.create(TileEntityExEnchantmentTable::new).build(null);
		ExEnchantmentTable.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityExEnchantmentTable.NAME));
		event.getRegistry().register(ExEnchantmentTable);

		Planter = TileEntityType.Builder.create(TileEntityPlanter::new).build(null);
		Planter.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityPlanter.NAME));
		event.getRegistry().register(Planter);

		Harvester = TileEntityType.Builder.create(TileEntityHarvester::new).build(null);
		Harvester.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityHarvester.NAME));
		event.getRegistry().register(Harvester);

		WoodPlanter = TileEntityType.Builder.create(TileEntityWoodPlanter::new).build(null);
		WoodPlanter.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityWoodPlanter.NAME));
		event.getRegistry().register(WoodPlanter);

		WoodHarvester = TileEntityType.Builder.create(TileEntityWoodHarvester::new).build(null);
		WoodHarvester.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityWoodHarvester.NAME));
		event.getRegistry().register(WoodHarvester);

		Setter = TileEntityType.Builder.create(TileEntitySetter::new).build(null);
		Setter.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntitySetter.NAME));
		event.getRegistry().register(Setter);

		Killer = TileEntityType.Builder.create(TileEntityKiller::new).build(null);
		Killer.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityKiller.NAME));
		event.getRegistry().register(Killer);

		ExpCollector = TileEntityType.Builder.create(TileEntityExpCollector::new).build(null);
		ExpCollector.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityExpCollector.NAME));
		event.getRegistry().register(ExpCollector);

		Crusher = TileEntityType.Builder.create(TileEntityCrusher::new).build(null);
		Crusher.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityCrusher.NAME));
		event.getRegistry().register(Crusher);

		Compresser = TileEntityType.Builder.create(TileEntityCompresser::new).build(null);
		Compresser.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityCompresser.NAME));
		event.getRegistry().register(Compresser);

		Straw = TileEntityType.Builder.create(TileEntityStraw::new).build(null);
		Straw.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityStraw.NAME));
		event.getRegistry().register(Straw);

		Destroyer = TileEntityType.Builder.create(TileEntityDestroyer::new).build(null);
		Destroyer.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityDestroyer.NAME));
		event.getRegistry().register(Destroyer);

		LiquidMaker = TileEntityType.Builder.create(TileEntityLiquidMaker::new).build(null);
		LiquidMaker.setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityLiquidMaker.NAME));
		event.getRegistry().register(LiquidMaker);

		Vacumer = TileEntityType.Builder.create(TileEntityVacumer::new).build(null);
		Vacumer .setRegistryName(new ResourceLocation(ModCommon.MOD_ID, TileEntityVacumer.NAME));
		event.getRegistry().register(Vacumer);
	}
}
