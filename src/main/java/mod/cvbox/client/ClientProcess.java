package mod.cvbox.client;

import mod.cvbox.core.ModCommon;
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
import mod.cvbox.render.RenderLiquidMaker;
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
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;

@OnlyIn(Dist.CLIENT)
public class ClientProcess{
	public ClientProcess(){
	}

	public void registerEntityRender(){
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLiquidMaker.class, new RenderLiquidMaker());
	}

	public void guiHandler(){
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> (openContainer) -> {
    		ResourceLocation location = openContainer.getId();
    		World world = Minecraft.getInstance().world;
    		EntityPlayer player = Minecraft.getInstance().player;
    		TileEntity ent = null;
    		BlockPos pos = new BlockPos(
    				openContainer.getAdditionalData().readInt(),
    				openContainer.getAdditionalData().readInt(),
    				openContainer.getAdditionalData().readInt());
    		if (location.toString().equals(ModCommon.MOD_ID + ":" + ModCommon.GUIID_EXPBANK)){
    			ent = world.getTileEntity(pos);
    			if ((ent instanceof TileEntityExpBank)){
    				return new GuiExpBank(player, world, pos.getX(),pos.getY(),pos.getZ());
    			}
    		} else if (location.toString().equals(ModCommon.MOD_ID + ":" + ModCommon.GUIID_EXENCHANTER)){
    			return new GuiExEnchantment(player.inventory, world);
    		} else if (location.toString().equals(ModCommon.MOD_ID + ":" + ModCommon.GUIID_EXANVIL)){
    			return new GuiExRepair(player.inventory,world);
    		} else if (location.toString().equals(ModCommon.MOD_ID + ":" +  ModCommon.GUIID_PLANTER)){
    			ent = world.getTileEntity(pos);
    			if ((ent instanceof TileEntityPlanter)){
    				return new GuiAutoPlanting(player, (TileEntityPlanter)ent, world, pos.getX(),pos.getY(),pos.getZ());
    			}
    		} else if (location.toString().equals(ModCommon.MOD_ID + ":" +  ModCommon.GUIID_HARVESTER)){
    			ent = world.getTileEntity(pos);
    			if ((ent instanceof TileEntityHarvester)){
    				return new GuiAutoHarvest(player, (TileEntityHarvester)ent, world, pos.getX(),pos.getY(),pos.getZ());
    			}
			}else if (location.toString().equals(ModCommon.MOD_ID + ":" +  ModCommon.GUIID_WOODPLANTER)){
				ent = world.getTileEntity(pos);
				if (ent instanceof TileEntityWoodPlanter){
					return new GuiWoodPlanter(player,(TileEntityWoodPlanter)ent,world,pos.getX(),pos.getY(),pos.getZ());
				}
			}else if (location.toString().equals(ModCommon.MOD_ID + ":" +  ModCommon.GUIID_WOODHARVESTER)){
				ent = world.getTileEntity(pos);
				if (ent instanceof TileEntityWoodHarvester){
					return new GuiWoodHarvester(player,(TileEntityWoodHarvester)ent,world, pos.getX(),pos.getY(),pos.getZ());
				}
			}else if (location.toString().equals(ModCommon.MOD_ID + ":" +  ModCommon.GUIID_SETTER)){
				ent = world.getTileEntity(pos);
				if (ent instanceof TileEntitySetter){
					return new GuiSetter(player.inventory,(IInventory)ent);
				}
			}else if (location.toString().equals(ModCommon.MOD_ID + ":" +  ModCommon.GUIID_KILLER)){
				ent = world.getTileEntity(pos);
				if (ent instanceof TileEntityKiller){
					return new GuiKiller(player.inventory,(IInventory)ent,world,pos);
				}
			}else if (location.toString().equals(ModCommon.MOD_ID + ":" +  ModCommon.GUIID_EXPCOLLECTOR)){
				ent = world.getTileEntity(pos);
				if (ent instanceof TileEntityExpCollector){
					return new GuiExpCollector(player,(IInventory)ent);
				}
			}else if (location.toString().equals(ModCommon.MOD_ID + ":" +  ModCommon.GUIID_CRUSHER)){
				ent = world.getTileEntity(pos);
				if (ent instanceof TileEntityCrusher){
					return new GuiCrusher(player,(IInventory)ent);
				}
			}else if (location.toString().equals(ModCommon.MOD_ID + ":" +  ModCommon.GUIID_COMPLESSER)){
				ent = world.getTileEntity(pos);
				if (ent instanceof TileEntityCompresser){
					return new GuiCompresser(player,(IInventory)ent);
				}
			}else if (location.toString().equals(ModCommon.MOD_ID + ":" +  ModCommon.GUIID_STRAW)){
				ent = world.getTileEntity(pos);
				if (ent instanceof TileEntityStraw){
					return new GuiStraw(player.inventory,(IInventory)ent);
				}
			}else if (location.toString().equals(ModCommon.MOD_ID + ":" +  ModCommon.GUIID_DESTROY)){
				ent = world.getTileEntity(pos);
				if (ent instanceof TileEntityDestroyer){
					return new GuiDestroyer(player,(IInventory)ent);
				}
			}else if (location.toString().equals(ModCommon.MOD_ID + ":" +  ModCommon.GUIID_LIQUIDMAKER)){
				ent = world.getTileEntity(pos);
				if (ent instanceof TileEntityLiquidMaker){
					return new GuiLiquidMaker(player,(IInventory)ent);
				}
			}else if (location.toString().equals(ModCommon.MOD_ID + ":" +  ModCommon.GUID_VACUMER)){
				ent = world.getTileEntity(pos);
				if (ent instanceof TileEntityVacumer){
					return new GuiVacumer(player,(IInventory)ent);
				}
			}
    		return null;
    	});
	}
}