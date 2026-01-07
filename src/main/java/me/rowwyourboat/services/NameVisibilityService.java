package me.rowwyourboat.services;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class NameVisibilityService {

    private static Scoreboard scoreboard;

    private static Team TEAM_VISIBLE;
    private static Team TEAM_HIDDEN;

    public static void registerEvents() {
        ServerLifecycleEvents.SERVER_STARTED.register(NameVisibilityService::registerTeams);
    }

    public static void show(ServerPlayerEntity player) {
        scoreboard.addScoreHolderToTeam(player.getNameForScoreboard(), TEAM_VISIBLE);
    }

    public static void hide(ServerPlayerEntity player) {
        scoreboard.addScoreHolderToTeam(player.getNameForScoreboard(), TEAM_HIDDEN);
    }

    public static void clearTeams() {
        scoreboard.clearTeam(TEAM_VISIBLE.getName());
        scoreboard.clearTeam(TEAM_HIDDEN.getName());
    }

    private static void registerTeams(MinecraftServer server) {
        scoreboard = server.getScoreboard();

        TEAM_VISIBLE = scoreboard.getTeam("NAME_VISIBLE");
        TEAM_HIDDEN = scoreboard.getTeam("NAME_HIDDEN");

        if (TEAM_VISIBLE == null) {
            TEAM_VISIBLE = scoreboard.addTeam("NAME_VISIBLE");
        }

        if (TEAM_HIDDEN == null) {
            TEAM_HIDDEN = scoreboard.addTeam("NAME_HIDDEN");
        }

        setTeamRules();
    }

    private static void setTeamRules() {
        TEAM_VISIBLE.setNameTagVisibilityRule(AbstractTeam.VisibilityRule.ALWAYS);
        TEAM_HIDDEN.setNameTagVisibilityRule(AbstractTeam.VisibilityRule.NEVER);
    }

}
