package me.rowwyourboat.services;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.Random;

public class RoleService {

    public static ServerPlayerEntity draftRandomPlayer(List<ServerPlayerEntity> candidates) {
        Random random = new Random();

        ServerPlayerEntity randomPlayer = candidates.get(random.nextInt(candidates.size()));
        candidates.remove(randomPlayer);

        return randomPlayer;
    }

}
