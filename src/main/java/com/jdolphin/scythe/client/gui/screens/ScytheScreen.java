package com.jdolphin.scythe.client.gui.screens;

import com.jdolphin.scythe.init.ModPackets;
import com.jdolphin.scythe.network.ServerboundChangePlayerDimensionPacket;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class ScytheScreen extends Screen {
	private EditBox dimensionInput;
	protected String suggestion;

	protected ScytheScreen(Component pTitle) {
		super(pTitle);
	}

	public ScytheScreen() {
		super(new TranslatableComponent("menu.scythe.scythe"));
	}

	@Override
	protected void init() {
		super.init();

		this.dimensionInput = new EditBox(this.font,
				4, this.height - 12, this.width - 4, 12,
				new TranslatableComponent("chat.editBox"));

		LocalPlayer player = minecraft.player;

		this.dimensionInput.setMaxLength(256);
		this.dimensionInput.setBordered(false);

		if (player != null) {
			ResourceLocation location = player.level.dimension().location();
			this.suggestion = location.getNamespace().equals("minecraft") ?
					location.getPath() : location.toString();

			this.dimensionInput.setSuggestion(suggestion);
		}

		this.dimensionInput.setResponder(this::onEdited);
		this.addWidget(this.dimensionInput);
	}

	private void onEdited(String p_95611_) {
		String value = dimensionInput.getValue();
		if (this.suggestion.startsWith(dimensionInput.getValue())) {
			this.dimensionInput.setSuggestion(this.suggestion.substring(value.length()));
		} else this.dimensionInput.setSuggestion("");

		dimensionInput.setTextColor(0xffffff);

		// TODO: suggestions or something
	}

	@Override
	public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
		this.setFocused(this.dimensionInput);
		this.dimensionInput.setFocus(true);
		fill(pPoseStack, 2, this.height - 14, this.width - 2,
				this.height - 2, this.minecraft.options.getBackgroundColor(Integer.MIN_VALUE));
		this.dimensionInput.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

		Style style = this.minecraft.gui.getChat().getClickedComponentStyleAt(pMouseX, pMouseY);
		if (style != null && style.getHoverEvent() != null) {
			this.renderComponentHoverEffect(pPoseStack, style, pMouseX, pMouseY);
		}

		super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
	}

	@Override
	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
		switch (pKeyCode) {
			case GLFW.GLFW_KEY_ENTER:

				try {
					ServerboundChangePlayerDimensionPacket packet = new ServerboundChangePlayerDimensionPacket(new ResourceLocation(dimensionInput.getValue()));

					ModPackets.INSTANCE.sendToServer(packet);

//					minecraft.getToasts().addToast(new SystemToast(SystemToast.SystemToastIds.TUTORIAL_HINT,
//							new TextComponent("enter pressed or something"), null));

				} catch (Exception error) {
					dimensionInput.setSuggestion(" \u00a7c" + error.getLocalizedMessage());
				}

				break;

			case GLFW.GLFW_KEY_TAB:
				dimensionInput.setValue(suggestion);
				break;
		}

		return super.keyPressed(pKeyCode, pScanCode, pModifiers);
	}
}