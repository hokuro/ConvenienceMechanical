package mod.cvbox.core;


import mod.cvbox.gui.GuiAutoHarvest;
import mod.cvbox.gui.GuiAutoPlanting;
import mod.cvbox.gui.GuiCompresser;
import mod.cvbox.gui.GuiCrusher;
import mod.cvbox.gui.GuiDestroyer;
import mod.cvbox.gui.GuiExEnchantment;
import mod.cvbox.gui.GuiExRepair;
import mod.cvbox.gui.GuiExpBank;
import mod.cvbox.gui.GuiExpCollector;
import mod.cvbox.gui.GuiKiller;
import mod.cvbox.gui.GuiLiquidMaker;
import mod.cvbox.gui.GuiSetter;
import mod.cvbox.gui.GuiStraw;
import mod.cvbox.gui.GuiVacumer;
import mod.cvbox.gui.GuiWoodHarvester;
import mod.cvbox.gui.GuiWoodPlanter;
import mod.cvbox.inventory.ContainerAutoHarvest;
import mod.cvbox.inventory.ContainerAutoPlanting;
import mod.cvbox.inventory.ContainerComplesser;
import mod.cvbox.inventory.ContainerCrusher;
import mod.cvbox.inventory.ContainerDestroyer;
import mod.cvbox.inventory.ContainerExEnchantment;
import mod.cvbox.inventory.ContainerExRepair;
import mod.cvbox.inventory.ContainerExpBank;
import mod.cvbox.inventory.ContainerExpCollector;
import mod.cvbox.inventory.ContainerKiller;
import mod.cvbox.inventory.ContainerLiquidMaker;
import mod.cvbox.inventory.ContainerSetter;
import mod.cvbox.inventory.ContainerStraw;
import mod.cvbox.inventory.ContainerVacumer;
import mod.cvbox.inventory.ContainerWoodHarvester;
import mod.cvbox.inventory.ContainerWoodPlanter;
import mod.cvbox.tileentity.TileEntityCompresser;
import mod.cvbox.tileentity.TileEntityCrusher;
import mod.cvbox.tileentity.TileEntityDestroyer;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGui implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x,y,z);
		TileEntity ent = null;
		switch(id){
		case ModCommon.GUIID_EXPBANK:
			ent = world.getTileEntity(pos);
			if((ent instanceof TileEntityExpBank)){
				return new ContainerExpBank(player, world,x,y,z);
			}
			break;
		case ModCommon.GUIID_EXENCHANTER:
			return new ContainerExEnchantment(player.inventory, world, pos);

		case ModCommon.GUIID_EXANVIL:
			return new ContainerExRepair(player.inventory, world, new BlockPos(x,y,z), player);
		case ModCommon.GUIID_PLANTER:
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntityPlanter){
				return new ContainerAutoPlanting(player,ent,world,x,y,z);
			}
			break;

		case ModCommon.GUIID_HARVESTER:
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntityHarvester){
				return new ContainerAutoHarvest(player,ent,world,x,y,z);
			}
			break;

		case ModCommon.GUIID_WOODPLANTER:
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntityWoodPlanter){
				return new ContainerWoodPlanter(player,ent,world,x,y,z);
			}
			break;
		case ModCommon.GUIID_WOODHARVESTER:
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntityWoodHarvester){
				return new ContainerWoodHarvester(player,ent,world,x,y,z);
			}
			break;

		case ModCommon.GUIID_SETTER:
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntitySetter){
				return new ContainerSetter(player.inventory,(IInventory)ent);
			}
			break;

		case ModCommon.GUIID_KILLER:
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntityKiller){
				return new ContainerKiller(player.inventory,(IInventory)ent,world,pos);
			}
			break;

		case ModCommon.GUIID_EXPCOLLECTOR:
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntityExpCollector){
				return new ContainerExpCollector(player.inventory,(IInventory)ent);
			}
			break;

		case ModCommon.GUIID_CRUSHER:
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntityCrusher){
				return new ContainerCrusher(player.inventory,(IInventory)ent);
			}
			break;

		case ModCommon.GUIID_COMPLESSER:
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntityCompresser){
				return new ContainerComplesser(player.inventory,(IInventory)ent);
			}
			break;

		case ModCommon.GUIID_STRAW:
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntityStraw){
				return new ContainerStraw(player.inventory,(IInventory)ent);
			}
			break;
		case ModCommon.GUIID_DESTROY:
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntityDestroyer){
				return new ContainerDestroyer(player.inventory,(IInventory)ent);
			}
			break;

		case ModCommon.GUIID_LIQUIDMAKER:
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntityLiquidMaker){
				return new ContainerLiquidMaker(player.inventory,(IInventory)ent);
			}
			break;

		case ModCommon.GUID_VACUMER:
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntityVacumer){
				return new ContainerVacumer(player.inventory,(IInventory)ent);
			}
			break;
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x,y,z);
		TileEntity ent = null;
		switch(id){
		case ModCommon.GUIID_EXPBANK:
			ent = world.getTileEntity(pos);
			if ((ent instanceof TileEntityExpBank)){
				return new GuiExpBank(player, world, x, y, z);
			}
			break;
		case ModCommon.GUIID_EXENCHANTER:
			return new GuiExEnchantment(player.inventory, world);
		case ModCommon.GUIID_EXANVIL:
			return new GuiExRepair(player.inventory,world);
		case ModCommon.GUIID_PLANTER:
			ent = world.getTileEntity(pos);
			if ((ent instanceof TileEntityPlanter)){
				return new GuiAutoPlanting(player, (TileEntityPlanter)ent, world, x, y, z);
			}
			break;
		case ModCommon.GUIID_HARVESTER:
			ent = world.getTileEntity(pos);
			if ((ent instanceof TileEntityHarvester)){
				return new GuiAutoHarvest(player, (TileEntityHarvester)ent, world, x, y, z);
			}
			break;
		case ModCommon.GUIID_WOODPLANTER:
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntityWoodPlanter){
				return new GuiWoodPlanter(player,(TileEntityWoodPlanter)ent,world,x,y,z);
			}
			break;
		case ModCommon.GUIID_WOODHARVESTER:
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntityWoodHarvester){
				return new GuiWoodHarvester(player,(TileEntityWoodHarvester)ent,world,x,y,z);
			}
			break;
		case ModCommon.GUIID_SETTER:
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntitySetter){
				return new GuiSetter(player.inventory,(IInventory)ent);
			}
			break;
		case ModCommon.GUIID_KILLER:
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntityKiller){
				return new GuiKiller(player.inventory,(IInventory)ent,world,pos);
			}
			break;

		case ModCommon.GUIID_EXPCOLLECTOR:
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntityExpCollector){
				return new GuiExpCollector(player,(IInventory)ent);
			}
			break;

		case ModCommon.GUIID_CRUSHER:
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntityCrusher){
				return new GuiCrusher(player,(IInventory)ent);
			}
			break;

		case ModCommon.GUIID_COMPLESSER:
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntityCompresser){
				return new GuiCompresser(player,(IInventory)ent);
			}
			break;

		case ModCommon.GUIID_STRAW:
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntityStraw){
				return new GuiStraw(player.inventory,(IInventory)ent);
			}
			break;
		case ModCommon.GUIID_DESTROY:
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntityDestroyer){
				return new GuiDestroyer(player,(IInventory)ent);
			}
			break;

		case ModCommon.GUIID_LIQUIDMAKER:
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntityLiquidMaker){
				return new GuiLiquidMaker(player,(IInventory)ent);
			}
			break;

		case ModCommon.GUID_VACUMER:
			ent = world.getTileEntity(pos);
			if (ent instanceof TileEntityVacumer){
				return new GuiVacumer(player,(IInventory)ent);
			}
			break;
		}
		return null;
	}

}