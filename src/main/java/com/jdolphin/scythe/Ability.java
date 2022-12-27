package com.jdolphin.scythe;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Optional;

public enum Ability {
        DASH("dash", 80, ParticleTypes.ELECTRIC_SPARK),
        PORTAL("portal", 500, ParticleTypes.FLAME),
        INVIS("invisibility", ParticleTypes.ENCHANT),
        NONE("none", ParticleTypes.SMOKE);

        public final String id;
        public boolean creativeOnly = false;
        public int cooldown = 10;
        public ParticleOptions particleOptions = ParticleTypes.GLOW_SQUID_INK;

        public static MutableComponent getName(@Nullable String id) {
            return fromId(id).getName();
        }

        public MutableComponent getName() {
            return new TranslatableComponent(String.format("scythe.ability.%s", this.id));
        }

        Ability(String id) {
            this.id = id;
        }

        Ability(String id, boolean creativeOnly) {
            this.id = id;
            this.creativeOnly = creativeOnly;
        }

        Ability(String id, int cooldown) {
            this.id = id;
            this.cooldown = cooldown;
        }

        Ability(String id, ParticleOptions particleOptions) {
            this.id = id;
            this.particleOptions = particleOptions;
        }

        Ability(String id, int cooldown, ParticleOptions particleOptions) {
            this.id = id;
            this.cooldown = cooldown;
            this.particleOptions = particleOptions;
        }

        Ability(String id, boolean creativeOnly, ParticleOptions particleOptions) {
            this.id = id;
            this.creativeOnly = creativeOnly;
            this.particleOptions = particleOptions;
        }

        Ability(String id, int cooldown, boolean creativeOnly, ParticleOptions particleOptions) {
            this.id = id;
            this.cooldown = cooldown;
            this.creativeOnly = creativeOnly;
            this.particleOptions = particleOptions;
        }

        public static Ability fromId(@Nullable String id) {
            Optional<Ability> result = Arrays.stream(Ability.values()).filter(modAbility -> modAbility.id.equals(id)).findFirst();

            if (result.isEmpty()) return NONE;
            return result.get();
        }

        public String getId() {
            return id;
        }

}
