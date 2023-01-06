package com.jdolphin.scythe.init;

import com.jdolphin.scythe.ScytheMod;
import com.jdolphin.scythe.network.ServerboundChangePlayerDimensionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPackets {
	static int index = 0;

	private static final String PROTOCOL_VERSION = "1";

	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(ScytheMod.MOD_ID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals);

	public static void init() {
		INSTANCE.messageBuilder(ServerboundChangePlayerDimensionPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
				.encoder(ServerboundChangePlayerDimensionPacket::write)
				.decoder(ServerboundChangePlayerDimensionPacket::read)
				.consumer(ServerboundChangePlayerDimensionPacket::handle).add();

	}
}
