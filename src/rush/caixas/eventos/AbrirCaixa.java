package rush.caixas.eventos;

import java.io.File;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import rush.caixas.Main;
import rush.caixas.utils.Caixas;
import rush.caixas.utils.DataManager;

public class AbrirCaixa implements Listener {
			
	@EventHandler
	public void PlayerInteract(PlayerInteractEvent e) {
		if (e.getItem() == null) return;
		if (!e.getItem().hasItemMeta()) return;
		if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			for (ItemStack item : Caixas.caixas.keySet()) {
				if (e.getItem().isSimilar(item)) {
					e.setCancelled(true);
					Player p = e.getPlayer();
					ItemStack caixa = e.getItem().clone();
					caixa.setAmount(1);
					String id = Caixas.caixas.get(caixa);
					File file = DataManager.getFile(id, "caixas");
					FileConfiguration config = DataManager.getConfiguration(file);
					abrirCaixa(config, p);
					removerCaixa(p);
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void InventoryClick(InventoryClickEvent e) {
		if (e.getInventory().getName().contains("§0ABRINDO CAIXA!")) {
			e.setCancelled(true);
		}
	}
	
	private void abrirCaixa(FileConfiguration config, Player p) {
		p.closeInventory();
		Set<String> ITENS = config.getConfigurationSection("Itens").getKeys(false);
		int nitens = ITENS.size();
		
		if (nitens < 1) {
			p.sendMessage("§cEsta caixa estava vazia!");
			return;
		}
		
		Inventory inv = Bukkit.createInventory(null, 45, "§0ABRINDO CAIXA!");
		inv.setItem(22, config.getItemStack("Itens.0"));
		p.openInventory(inv);
		goTask(inv,p,config,nitens);
	}
	
	private void goTask(Inventory inv, Player p, FileConfiguration config, int nitens) {
		new BukkitRunnable() {
			Random rnd = new Random();
			int slot1 = 0;
			int slot2 = 44;
			public void run() {
				if (slot1 == 22) {
					finalizarCaixa(p, inv);
					cancel();
				} else {
					p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
					inv.setItem(slot1, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) (rnd.nextInt(14) + 1)));
					inv.setItem(slot2, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) (rnd.nextInt(14) + 1)));
					inv.setItem(22, config.getItemStack("Itens." + rnd.nextInt(nitens)));
					slot1++;
					slot2--;
				}
			}
		}.runTaskTimer(Main.aqui, 0L, 5L);
	}
	
	private void finalizarCaixa(Player p, Inventory inv) {
		ItemStack ganhou = inv.getItem(22);
		p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
		p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
		if (p.getInventory().firstEmpty() == -1) p.getWorld().dropItem(p.getLocation(), ganhou);
		else p.getInventory().addItem(ganhou);
		p.sendMessage("§aVocê ganhou x" + ganhou.getAmount() + " "+ ganhou.getType() + "§a!");
	}
	
	private void removerCaixa(Player p) {
		ItemStack ap = p.getItemInHand();
		if(ap.getAmount() == 1) {
		   p.setItemInHand(new ItemStack(Material.AIR));
		} else { 
		   ap.setAmount(ap.getAmount() - 1);
		   p.setItemInHand(ap);
		}
	}
}
