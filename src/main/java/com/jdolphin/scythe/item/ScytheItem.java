package com.jdolphin.scythe.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;

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
		Random random = pPlayer.getRandom();

		if (mode != null) switch (mode) {
			case DIMENSION_HOP:
				if (!pLevel.isClientSide && !dimension.equals(pPlayer.getLevel().dimension().location()))
					dimensionHop((ServerPlayer) pPlayer, dimension);

				break;

			case INVISIBILITY:
				if (pPlayer.hasEffect(MobEffects.INVISIBILITY)) {
					pPlayer.removeEffect(MobEffects.INVISIBILITY);
					break;
				}

				for (int i = 0; i < 20; i++)
					pLevel.addParticle(ParticleTypes.CLOUD, true, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
							random.nextFloat(-0.2f, 0.2f), random.nextFloat(-0.2f, 0.2f),
							random.nextFloat(-0.2f, 0.2f));

				pPlayer.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, Integer.MAX_VALUE, 1,
						true, false, true));

				break;

			case DASH:
				float xRot = pPlayer.getXRot();
				float yRot = pPlayer.getYRot();
				float zRot = 0;

				float pX = -Mth.sin(yRot * ((float) Math.PI / 180F)) * Mth.cos(xRot * ((float) Math.PI / 180F));
				float pY = -Mth.sin((xRot + zRot) * ((float) Math.PI / 180F));
				float pZ = Mth.cos(yRot * ((float) Math.PI / 180F)) * Mth.cos(xRot * ((float) Math.PI / 180F));
				float inaccuracy = 0;
				float velocity = 1.25f;

				Vec3 vec3 = (new Vec3(pX, pY, pZ)).normalize().add(random.nextGaussian() * (double) 0.0075F * (double) inaccuracy, random.nextGaussian() * (double) 0.0075F * (double) inaccuracy, random.nextGaussian() * (double) 0.0075F * (double) inaccuracy).scale(velocity);
				pPlayer.setDeltaMovement(vec3);

				pPlayer.yRotO = pPlayer.getYRot();
				pPlayer.xRotO = pPlayer.getXRot();

				Vec3 deltaMovement = pPlayer.getDeltaMovement();
				pPlayer.setDeltaMovement(pPlayer.getDeltaMovement().add(deltaMovement.x, pPlayer.isOnGround() ? 0.0D : deltaMovement.y, deltaMovement.z));

				pPlayer.getCooldowns().addCooldown(this, (int) (Math.max(Math.abs(xRot), 20) * velocity));

				pLevel.playSound(null, pPlayer, SoundEvents.PLAYER_ATTACK_KNOCKBACK, SoundSource.PLAYERS, 1, 1);

				for (int i = 0; i < 20; i++)
					pLevel.addParticle(ParticleTypes.CLOUD, true, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
							random.nextFloat(-0.2f, 0.2f), random.nextFloat(-0.2f, 0.2f),
							random.nextFloat(-0.2f, 0.2f));
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
		INVISIBILITY
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
