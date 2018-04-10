package mod.cvbox.event;

import java.util.ArrayList;
import java.util.List;

import mod.cvbox.gui.GuiExRepair;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class McEventHandler{

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void anvilGui(AnvilUpdateEvent e){

	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onButtonPress(ActionPerformedEvent.Pre e){
		if (e.getGui() instanceof GuiExRepair){
			final GuiExRepair repair = (GuiExRepair)e.getGui();

			switch(e.getButton().id){
			default:
				break;
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void addButtons(InitGuiEvent.Post e){
		if(e.getGui() instanceof GuiExRepair){
			final List<GuiButton> list = new ArrayList<GuiButton>();
			e.getButtonList().addAll(list);
		}
	}
}
