package fr.epsilon.exemple;

import fr.epsilon.api.game.EGame;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

public class PitchoutGame extends EGame<PitchoutPlayer> implements Listener {
    public PitchoutGame(Plugin plugin) {
        super(plugin, PitchoutPlayer.class);

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void handleStart() {
        Bukkit.broadcastMessage("Bonne chance à vous !");

        for (PitchoutPlayer pitchoutPlayer : getInGamePlayer()) {
            teleportGame(pitchoutPlayer);
            pitchoutPlayer.setupGame();
        }
    }

    @Override
    public void handleEnd() {
        PitchoutPlayer winner = getInGamePlayer().get(0);
        Player playerWinner = winner.getPlayer();

        Bukkit.broadcastMessage(playerWinner.getName() + "a gagné la partie !");
    }

    @Override
    public void handleTimer(int i) {
        if (i == 10)
            Bukkit.broadcastMessage("§0[§9Pitchout§0] §9La partie commence dans 10 secondes");

        if (i <= 5)
            Bukkit.broadcastMessage("§0[§9Pitchout§0] §9La partie commence dans " + i + " secondes");

        for(Player player : Bukkit.getOnlinePlayers())
            player.setLevel(i);
    }

    @Override
    public void handleJoin(Player player) {
        PitchoutPlayer pitchoutPlayer = getGamePlayer(player.getUniqueId());

        if (!isStarted()) {
            teleportLobbyGame(pitchoutPlayer);
            Bukkit.broadcastMessage(player.getName() + " a rejoint le serveur.");
        }
    }

    @Override
    public void handleQuit(Player player) {
        if (!isStarted()) {
            Bukkit.broadcastMessage(player.getName() + " a quitter le serveur.");
        }else {
            checkWin();
        }
    }

    private void checkWin() {
        if (getInGamePlayer().size() == 1)
            endGame();
    }

    private void teleportGame(PitchoutPlayer pitchoutPlayer) {
        Player player = pitchoutPlayer.getPlayer();
        player.teleport(new Location(player.getWorld(), 275,106,1205));
    }

    private void teleportLobbyGame(PitchoutPlayer pitchoutPlayer) {
        Player player = pitchoutPlayer.getPlayer();
        player.teleport(new Location(player.getWorld(),256, 106, 1086));
    }

    //|PitchoutEvents|\\

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        PitchoutPlayer pitchoutPlayer = getGamePlayer(event.getPlayer().getUniqueId());
        Player player = event.getPlayer();

        if (pitchoutPlayer.isSpectator()) return;

        if (player.getLocation().getBlockY() <= 79) {
            teleportGame(pitchoutPlayer);

            if (pitchoutPlayer.getHeart() == 1) {
                Bukkit.broadcastMessage(player.getName() + " a été éliminé !");
                pitchoutPlayer.setSpectator(true);
            }else {
                pitchoutPlayer.decreaseHeart();
            }

            checkWin();
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL || !isStarted()) {
            event.setCancelled(true);
        }else {
            event.setDamage(0);
        }
    }

    @EventHandler
    public void RegenerationDisabled(EntityRegainHealthEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void OnDrop(PlayerDropItemEvent e){
        e.setCancelled(true);
    }
}
