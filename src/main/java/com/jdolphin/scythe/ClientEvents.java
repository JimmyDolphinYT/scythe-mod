package com.jdolphin.scythe;

import com.jdolphin.scythe.keybind.Keybinds;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {

    @Mod.EventBusSubscriber(modid = ScytheMod.MOD_ID, value = Dist.CLIENT)
        public static class ClientForgeEvents {

            @SubscribeEvent
            public static void onKeyInput(InputEvent event) {
                if (Keybinds.KEY_SCYTHE_MENU.consumeClick()) {

                    new TranslatableComponent(String.format("b"));
                }
            }
        }
    @Mod.EventBusSubscriber(modid = ScytheMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(KeyMapping event) {
            event.register(Keybinds.KEY_SCYTHE_MENU);
        }
    }
}