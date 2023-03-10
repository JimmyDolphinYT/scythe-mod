package com.jdolphin.scythe.client.gui.screens;

import com.google.common.collect.Lists;
import com.jdolphin.scythe.ScytheMod;
import com.jdolphin.scythe.init.ModPackets;
import com.jdolphin.scythe.item.ScytheItem;
import com.jdolphin.scythe.keybind.Keybinds;
import com.jdolphin.scythe.network.ServerboundReplaceItemPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ScytheAbilityScreen extends Screen {
	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ScytheMod.MOD_ID, "textures/gui/clockwork_gui.png");
	int backgroundWidth = 256;
	int backgroundHeight = 256;
	private final List<Button> abilityButtons = Lists.newArrayList();

	protected final KeyMapping[] keyMappings = new KeyMapping[]{
			Keybinds.KEY_SCYTHE_MENU_DIMENSION_HOP,
			Keybinds.KEY_SCYTHE_MENU_DASH,
			Keybinds.KEY_SCYTHE_MENU_INVISIBILITY,
			Keybinds.KEY_SCYTHE_MENU_PASSIVE
	};

	public ScytheAbilityScreen() {
		super(new TranslatableComponent("menu.scythe.ability"));
	}

	@Override
	protected void init() {
		this.abilityButtons.add(this.addRenderableWidget(new Button(this.width / 2 - 45, this.height / 4 - 23, 85, 20,
				new TranslatableComponent("scythe.ability.dim_hop"), (p_95930_) -> {
			Screen ScytheDimensionScreen = new ScytheDimensionScreen();
			assert this.minecraft != null;
			this.minecraft.setScreen(ScytheDimensionScreen);
		})));
		this.abilityButtons.add(this.addRenderableWidget(new Button(this.width / 2 - 150, this.height / 2 - 10, 85, 20,
				new TranslatableComponent("scythe.ability.dash"), (p_95930_) -> {
			setMode(ScytheItem.Mode.DASH);

			this.onClose();
		})));
		this.abilityButtons.add(this.addRenderableWidget(new Button(this.width / 2 - 45, this.height / 2 + 90, 85, 20,
				new TranslatableComponent("scythe.ability.invisibility"), (p_95930_) -> {
			setMode(ScytheItem.Mode.INVISIBILITY);

			this.onClose();
		})));
		this.abilityButtons.add(this.addRenderableWidget(new Button(this.width / 2 + 55, this.height / 2 - 10, 85, 20,
				new TranslatableComponent("scythe.ability.passive"), (p_95930_) -> {
			setMode(ScytheItem.Mode.PASSIVE);

			this.onClose();
		})));
		super.init();
	}

	void setMode(@Nullable ScytheItem.Mode mode) {
		ItemStack stack = minecraft.player.getMainHandItem();
		ScytheItem.setMode(stack, mode);

		ServerboundReplaceItemPacket packet = new ServerboundReplaceItemPacket(EquipmentSlot.MAINHAND, stack);
		ModPackets.INSTANCE.sendToServer(packet);
	}

	@Override
	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
		for (int i = 0; i < keyMappings.length; i++) {
			if (keyMappings[i].matches(pKeyCode, pScanCode)) {
				this.abilityButtons.get(i).onPress();
				break;
			}
		}

		return super.keyPressed(pKeyCode, pScanCode, pModifiers);
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