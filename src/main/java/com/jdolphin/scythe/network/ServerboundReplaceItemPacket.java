package com.jdolphin.scythe.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundReplaceItemPacket {
	protected ItemStack itemStack;
	protected EquipmentSlot slot;

	public ServerboundReplaceItemPacket(EquipmentSlot slot, ItemStack itemStack) {
		this.slot = slot;
		this.itemStack = itemStack;
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeEnum(this.slot);
		buf.writeItemStack(itemStack, false);
	}

	public ServerboundReplaceItemPacket(FriendlyByteBuf buf) {
		this.slot = buf.readEnum(EquipmentSlot.class);
		this.itemStack = buf.readItem();
	}


	public boolean handle(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();

		ServerPlayer player = context.getSender();
		assert player != null;

		try {
			player.setItemSlot(slot, itemStack);
			return true;
		} catch (NullPointerException err) {
			err.printStackTrace();
			return false;
		}
	}
}
