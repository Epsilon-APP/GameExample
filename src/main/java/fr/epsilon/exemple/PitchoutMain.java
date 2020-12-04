package fr.epsilon.exemple;

import fr.epsilon.api.EpsilonAPI;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.plugin.java.JavaPlugin;

public class PitchoutMain extends JavaPlugin {
    @Override
    public void onEnable() {
        EpsilonAPI.get().getGameManager().registerGame(new PitchoutGame(this));
        EpsilonAPI.get().getGameManager().setGameMode(GameMode.SURVIVAL);
        EpsilonAPI.get().getGameManager().setDifficulty(Difficulty.PEACEFUL);
        EpsilonAPI.get().getGameManager().setRequiredPlayer(2);
    }
}
