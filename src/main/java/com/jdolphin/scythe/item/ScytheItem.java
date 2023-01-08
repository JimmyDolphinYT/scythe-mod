package com.jdolphin.scythe.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;

public class ScytheItem extends AxeItem {
	public static final String TAG_MODE = "ScytheMode";
	public static final String TAG_DIMENSION = "ScytheDimension";

	public ScytheItem(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
		super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
	}

	@Override
	public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
		return super.canPerformAction(stack, toolAction) || ToolActions.DEFAULT_SWORD_ACTIONS.contains(toolAction);
	}

	@Override
	@SuppressWarnings("SwitchStatementWithTooFewBranches")
	public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
		ItemStack useItem = pPlayer.getItemInHand(pUsedHand);
		Mode mode = getMode(useItem);
		ResourceLocation dimension = getHopDimension(useItem);

		if (mode != null) switch (mode) {
			case DIMENSION_HOP:
				if (!pLevel.isClientSide)
					dimensionHop((ServerPlayer) pPlayer, dimension);
				break;
		}

		return super.use(pLevel, pPlayer, pUsedHand);
	}

	public static ResourceLocation getHopDimension(ItemStack itemStack) {
		return new ResourceLocation(itemStack.getOrCreateTag().getString(TAG_DIMENSION));
	}

	public static void setHopDimension(ItemStack itemStack, ResourceLocation dimension) {
		CompoundTag tag = itemStack.getOrCreateTag();

		tag.putString(TAG_DIMENSION, dimension.toString());
		itemStack.setTag(tag);
	}

	public static Mode getMode(ItemStack itemStack) {
		CompoundTag tag = itemStack.getOrCreateTag();

		try {
			return Mode.valueOf(tag.getString(TAG_MODE).toUpperCase());
		} catch (IllegalArgumentException error) {
			return null;
		}
	}

	public static void setMode(ItemStack itemStack, @Nullable Mode mode) {
		CompoundTag tag = itemStack.getOrCreateTag();
		if (mode == null) tag.remove(TAG_MODE);
		else tag.putString(TAG_MODE, mode.name());

		itemStack.setTag(tag);
	}

	public enum Mode {
		DIMENSION_HOP,
		PASSIVE,
		DASH,
		INVISIBILITY;
	}

	public static boolean dimensionHop(ServerPlayer player, ResourceLocation dimension) {
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
				biome.is(Tags.Biomes.IS_END) && Objects.requireNonNull(newLevel.dragonFight()).hasPreviouslyKilledDragon())) {
			return false;
		} else {
			if (biome.is(Tags.Biomes.IS_VOID) || (
					biome.is(Tags.Biomes.IS_END)
							&& newLevel.getBlockState(player.blockPosition().below()).isAir()
							&& !player.blockPosition().closerThan(BlockPos.ZERO.above((int) y), 64))) {

				newLevel.setBlock(player.blockPosition().below(), Blocks.DIRT.defaultBlockState(), 2);
			}
		}
		return true;

	}
}
