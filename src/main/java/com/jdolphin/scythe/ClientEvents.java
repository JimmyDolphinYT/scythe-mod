package com.jdolphin.scythe;

import com.jdolphin.scythe.keybind.Keybinds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientEvents {

	@Mod.EventBusSubscriber(modid = ScytheMod.MOD_ID, value = Dist.CLIENT)
	public static class ClientForgeEvents {

		@SubscribeEvent
		public static void onKeyInput(InputEvent event) {
			if (Keybinds.KEY_SCYTHE_MENU.consumeClick()) {
				Minecraft.getInstance().getToasts().addToast(new SystemToast(SystemToast.SystemToastIds.TUTORIAL_HINT, new TextComponent("b"), null));
			}
		}
	}

	@Mod.EventBusSubscriber(modid = ScytheMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class ClientModBusEvents {
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {
			Keybinds.register();
		}
	}
}