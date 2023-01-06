package com.jdolphin.scythe.client.gui.screens;

import com.google.common.collect.Lists;
import com.jdolphin.scythe.ScytheMod;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ScytheAbilityScreen extends Screen {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ScytheMod.MOD_ID, "textures/gui/clockwork_gui.png");
    int backgroundWidth = 256;
    int backgroundHeight = 256;
    private final List<Button> abilityButtons = Lists.newArrayList();

    public ScytheAbilityScreen() {
        super(new TranslatableComponent("menu.scythe.ability"));
    }

    @Override
    protected void init() {
        this.abilityButtons.add(this.addRenderableWidget(new Button(this.width / 2 - 45, this.height / 4 - 23, 85, 20,
                new TranslatableComponent("scythe.ability.dim_hop"), (p_95930_) -> {
            Object ScytheDimensionScreen = new ScytheDimensionScreen();
            this.minecraft.setScreen((Screen)ScytheDimensionScreen);

        })));
        this.abilityButtons.add(this.addRenderableWidget(new Button(this.width / 2 - 150, this.height / 2 - 10, 85, 20,
                new TranslatableComponent("scythe.ability.dash"), (p_95930_) -> {

        })));
        this.abilityButtons.add(this.addRenderableWidget(new Button(this.width / 2 - 45, this.height / 2 + 90, 85, 20,
                new TranslatableComponent("scythe.ability.invisibility"), (p_95930_) -> {

        })));
        this.abilityButtons.add(this.addRenderableWidget(new Button(this.width / 2 + 55, this.height / 2 - 10, 85, 20,
                new TranslatableComponent("scythe.ability.passive"), (p_95930_) -> {

        })));
        super.init();
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        renderBackground(pPoseStack, 1, 1, 1);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    protected void renderBackground(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        GL11.glEnable(GL11.GL_BLEND);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        int x = (width - backgroundWidth) / 2 + 2;
        int y = (height - backgroundHeight) / 2 + 2;

        this.blit(pPoseStack, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }
    public boolean isPauseScreen() {
        return false;
    }

}