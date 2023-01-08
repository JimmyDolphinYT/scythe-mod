package com.jdolphin.scythe.client.gui.screens;

import com.jdolphin.scythe.init.ModItems;
import com.jdolphin.scythe.init.ModPackets;
import com.jdolphin.scythe.item.ScytheItem;
import com.jdolphin.scythe.network.ServerboundReplaceItemPacket;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class ScytheDimensionScreen extends Screen {
	private EditBox dimensionInput;
	protected String suggestion;

	protected ScytheDimensionScreen(Component pTitle) {
		super(pTitle);
	}

	public ScytheDimensionScreen() {
		super(new TranslatableComponent("menu.scythe.dimension"));
	}

	@Override
	protected void init() {
		super.init();

		this.dimensionInput = new EditBox(this.font,
				256, 160, 128, 24,
				new TranslatableComponent("chat.editBox"));

		LocalPlayer player = minecraft.player;

		this.dimensionInput.setMaxLength(256);
		this.dimensionInput.setBordered(true);

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
		this.dimensionInput.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

		Style style = this.minecraft.gui.getChat().getClickedComponentStyleAt(pMouseX, pMouseY);
		if (style != null && style.getHoverEvent() != null) {
			this.renderComponentHoverEffect(pPoseStack, style, pMouseX, pMouseY);
		}

		super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
	}

	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
		switch (pKeyCode) {
			case GLFW.GLFW_KEY_ENTER:

				try {
					assert minecraft != null && minecraft.player != null;

					LocalPlayer player = minecraft.player;

					ResourceLocation resourceLocation = new ResourceLocation(dimensionInput.getValue());

					ItemStack itemStack = player.getMainHandItem();
					if (!itemStack.is(ModItems.SCYTHE.get())) // TODO: add an item tag for the scythes (assuming there's gonna be multiple of them)
						throw new Exception("Couldn't find scythe");

					ScytheItem.setMode(itemStack, ScytheItem.Mode.DIMENSION_HOP);
					ScytheItem.setHopDimension(itemStack, resourceLocation);

					EquipmentSlot equipmentSlot = EquipmentSlot.MAINHAND;

					ServerboundReplaceItemPacket packet = new ServerboundReplaceItemPacket(equipmentSlot, itemStack);
					ModPackets.INSTANCE.sendToServer(packet);

					this.onClose();

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
