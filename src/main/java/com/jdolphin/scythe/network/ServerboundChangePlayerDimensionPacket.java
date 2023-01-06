package com.jdolphin.scythe.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundChangePlayerDimensionPacket {
	ResourceLocation dimension;

	public void write(FriendlyByteBuf pBuffer) {
		pBuffer.writeResourceLocation(dimension);
	}

	public ServerboundChangePlayerDimensionPacket(ResourceLocation dimensionLocation) {
		dimension = dimensionLocation;
	}

	public static ServerboundChangePlayerDimensionPacket read(FriendlyByteBuf pBuffer) {
		ResourceLocation location = pBuffer.readResourceLocation();

		return new ServerboundChangePlayerDimensionPacket(location);
	}

	public boolean handle(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
		//WE ON DA SERVER
		ServerPlayer player = context.getSender();
		assert player != null;

		ServerLevel level = player.getLevel();

		ServerLevel newLevel = level.getServer().getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, dimension));
		if (newLevel == null)
			return false;

		double y = player.getY();

		BlockPos headPos = player.blockPosition().above();
		newLevel.getChunkAt(headPos).runPostLoad();
		if (newLevel.getBlockState(headPos).isSuffocating(level, headPos) || y <= newLevel.getMinBuildHeight()) {
			for (int i = 0; y + i <= level.getHeight(); i++) {
				BlockPos pos = new BlockPos(player.getX(), y + i, player.getZ());
				if (newLevel.isOutsideBuildHeight(pos)) continue;

				if (!newLevel.getBlockState(pos).isSuffocating(level, pos)) {
					y += i;
					break;
				}
			}
		}

		player.teleportTo(newLevel, player.getX(), y, player.getZ(), player.getYRot(), player.getXRot());

		Holder<Biome> biome = newLevel.getBiome(player.blockPosition());
		if (biome.is(Tags.Biomes.IS_VOID) || (
				biome.is(Tags.Biomes.IS_END)
						&& newLevel.getBlockState(player.blockPosition().below()).isAir()
						&& !player.blockPosition().closerThan(BlockPos.ZERO.above((int) y), 64))) {

			newLevel.setBlock(player.blockPosition().below(), Blocks.GLASS.defaultBlockState(), 2);
		}
		return true;
	}
}
