package com.jdolphin.scythe;

import com.jdolphin.scythe.init.ModItems;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {
    public static final DeferredRegister<Item> ITEMS;
    public static ModItems modItems;


    public static void init() {
        ModItems.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    static {
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "maous_scythe_mod");
    }
}
