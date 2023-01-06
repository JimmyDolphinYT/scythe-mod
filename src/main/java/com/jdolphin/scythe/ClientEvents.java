package com.jdolphin.scythe;

import com.jdolphin.scythe.client.gui.screens.ScytheScreen;
import com.jdolphin.scythe.init.ModItems;
import com.jdolphin.scythe.keybind.Keybinds;
import net.minecraft.client.Minecraft;
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
				Minecraft minecraft = Minecraft.getInstance();
				if (minecraft.player.getMainHandItem().is(ModItems.SCYTHE.get())) {
					minecraft.setScreen(new ScytheScreen());
				}
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