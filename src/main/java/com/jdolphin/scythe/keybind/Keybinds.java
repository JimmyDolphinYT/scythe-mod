package com.jdolphin.scythe.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class Keybinds {
    public static final String SCYTHE_MENU_CATEGORY = "key.category.scythe.scythe_menu";
    public static final String SCYTHE_MENU_KEY = "key.scythe.scythe_menu";

    public static final KeyMapping KEY_SCYTHE_MENU = new KeyMapping(SCYTHE_MENU_KEY, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, SCYTHE_MENU_CATEGORY);
}