package com.jdolphin.scythe.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;

public class Keybinds {
    protected static HashSet<KeyMapping> keyMappings = new HashSet<>();

    public static final String SCYTHE_MENU_CATEGORY = "key.category.scythe";
    public static final String SCYTHE_MENU_KEY = "key.scythe.scythe_menu";

    public static final KeyMapping KEY_SCYTHE_MENU = createMapping(SCYTHE_MENU_KEY, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, SCYTHE_MENU_CATEGORY);

    protected static KeyMapping createMapping(String name, KeyConflictContext context, InputConstants.Type type, int binding, String category) {
        KeyMapping mapping = new KeyMapping(name, context, type, binding, category);
        keyMappings.add(mapping);
        return mapping;
    }

    public static void register() {
        keyMappings.forEach(ClientRegistry::registerKeyBinding);
    }
}