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
    public static final String SCYTHE_MENU_DIMENSION_HOP_KEY = "key.scythe.scythe_menu.dimension_hop";
    public static final String SCYTHE_MENU_DASH_KEY = "key.scythe.scythe_menu.dash";
    public static final String SCYTHE_MENU_PASSIVE_KEY = "key.scythe.scythe_menu.passive";
    public static final String SCYTHE_MENU_INVISIBILITY_KEY = "key.scythe.scythe_menu.invisibility";

    public static final KeyMapping KEY_SCYTHE_MENU = createMapping(SCYTHE_MENU_KEY, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, SCYTHE_MENU_CATEGORY);

    public static final KeyMapping KEY_SCYTHE_MENU_DIMENSION_HOP = createMapping(SCYTHE_MENU_DIMENSION_HOP_KEY, KeyConflictContext.GUI,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_W, SCYTHE_MENU_CATEGORY);

    public static final KeyMapping KEY_SCYTHE_MENU_DASH = createMapping(SCYTHE_MENU_DASH_KEY, KeyConflictContext.GUI,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_A, SCYTHE_MENU_CATEGORY);

    public static final KeyMapping KEY_SCYTHE_MENU_INVISIBILITY = createMapping(SCYTHE_MENU_INVISIBILITY_KEY, KeyConflictContext.GUI,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_S, SCYTHE_MENU_CATEGORY);

    public static final KeyMapping KEY_SCYTHE_MENU_PASSIVE = createMapping(SCYTHE_MENU_PASSIVE_KEY, KeyConflictContext.GUI,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_D, SCYTHE_MENU_CATEGORY);

    protected static KeyMapping createMapping(String name, KeyConflictContext context, InputConstants.Type type, int binding, String category) {
        KeyMapping mapping = new KeyMapping(name, context, type, binding, category);
        keyMappings.add(mapping);
        return mapping;
    }

    public static void register() {
        keyMappings.forEach(ClientRegistry::registerKeyBinding);
    }
}