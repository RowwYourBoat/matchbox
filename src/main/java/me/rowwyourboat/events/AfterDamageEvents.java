package me.rowwyourboat.events;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;

public class AfterDamageEvents {

    public static void register() {
        ServerLivingEntityEvents.AFTER_DAMAGE.register(AfterDamageEvents::onAfterDamage);
    }

    private static void onAfterDamage(Entity entity, DamageSource source, float baseDamageTaken, float damageTaken, boolean blocked) {
        if (!(entity instanceof PlayerEntity player)) { return; }
        player.setHealth(player.getMaxHealth());
    }

}
