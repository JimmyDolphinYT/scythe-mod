package com.jdolphin.scythe.init;

import com.jdolphin.scythe.ScytheMod;
import com.jdolphin.scythe.RegistryHandler;

import com.jdolphin.scythe.item.ScytheItem;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import static com.jdolphin.scythe.item.MaousCreativeModeTab.TAB_MAOU;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ScytheMod.MOD_ID);

    public static RegistryObject<Item> SCYTHE = RegistryHandler.ITEMS.register("scythe", () -> {
        return new ScytheItem(Tiers.NETHERITE, 6.0F, -2.5F, (new Item.Properties().tab(TAB_MAOU)));
    });

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
