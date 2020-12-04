package fr.epsilon.exemple;

import fr.epsilon.api.game.EGamePlayer;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class PitchoutPlayer extends EGamePlayer {
    private int heart;

    public PitchoutPlayer(Player player) {
        super(player);

        this.heart = 3;
    }

    private void updateHeart() {
        player.setHealth(2 * heart);
    }

    public void setupGame() {
        ItemStack shovel = new ItemBuilder(Material.IRON_SPADE).flags(ItemFlag.HIDE_ATTRIBUTES).enchant(Enchantment.KNOCKBACK, 10).build();

        updateHeart();
        player.setMaxHealth(2 * heart);

        player.getInventory().clear();
        player.getInventory().addItem(shovel);
    }

    public void decreaseHeart() {
        this.heart--;
        updateHeart();
    }

    public int getHeart() {
        return this.heart;
    }

    @Override
    public void spectatorMode() {
        player.setGameMode(GameMode.SPECTATOR);
    }
}
