package me.rowwyourboat.events;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockEvents {

    public static void register() {
        PlayerBlockBreakEvents.BEFORE.register(BlockEvents::onBlockBreak);
    }

    private static boolean onBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity entity) {
        return player.hasPermissionLevel(4) || state.getBlock() == Blocks.OAK_SIGN;
    }

}
