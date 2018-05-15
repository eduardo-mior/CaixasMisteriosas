package rush.caixas;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import rush.caixas.comandos.ComandoCaixas;
import rush.caixas.comandos.ComandoCriarcaixa;
import rush.caixas.comandos.ComandoDelcaixa;
import rush.caixas.comandos.ComandoEditarcaixa;
import rush.caixas.comandos.ComandoGivecaixa;
import rush.caixas.eventos.AbrirCaixa;
import rush.caixas.eventos.ComandosListener;
import rush.caixas.utils.Caixas;
import rush.caixas.utils.DataManager;

public class Main extends JavaPlugin implements Listener {

	   public static Main aqui;

	   public void onEnable() {
		   instanceMain();
		   gerarConfigs();
		   registrarEventos();
		   registrarComandos();
		   Caixas.carregarCaixas();
	   }
	   
	   public void onDisable() {
		   HandlerList.unregisterAll((Listener) this);
	   }
	   
	   public void instanceMain() {
		   aqui = this;
	   }
	   
	   public void gerarConfigs() {
		   saveDefaultConfig();
		   DataManager.createFolder("caixas");
	   }
	   
	   public void registrarEventos() {
		   PluginManager pm = Bukkit.getServer().getPluginManager();
		   pm.registerEvents(new ComandosListener(), this);
		   pm.registerEvents(new AbrirCaixa(), this);
	   }
	   
	   public void registrarComandos() {
		   getCommand("caixas").setExecutor(new ComandoCaixas());
		   getCommand("delcaixa").setExecutor(new ComandoDelcaixa());
		   getCommand("editarcaixa").setExecutor(new ComandoEditarcaixa());
		   getCommand("givecaixa").setExecutor(new ComandoGivecaixa());
		   getCommand("criarcaixa").setExecutor(new ComandoCriarcaixa());
	   }
}