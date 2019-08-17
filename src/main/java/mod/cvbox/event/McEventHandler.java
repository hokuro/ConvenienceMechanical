package mod.cvbox.event;

import mod.cvbox.gui.GuiExRepair;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class McEventHandler{

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void anvilGui(AnvilUpdateEvent e){

	}

	@OnlyIn(Dist.CLIENT)
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

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void addButtons(InitGuiEvent.Post e){
		if(e.getGui() instanceof GuiExRepair){
//			final List<GuiButton> list = new ArrayList<GuiButton>();
//			e.getButtonList().addAll(list);
		}
	}
}
