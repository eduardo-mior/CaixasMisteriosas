package rush.caixas.comandos;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import rush.caixas.utils.DataManager;

public class ComandoDelcaixa implements Listener, CommandExecutor {
	
	public boolean onCommand(CommandSender s, Command cmd, String lbl, String[] args) {
		if (cmd.getName().equalsIgnoreCase("delcaixa")) {
			 
			if (args.length != 1) {
				s.sendMessage("§cComando incorreto, use /delcaixa <id>");
				return false;
			} 
				     
			String caixa = args[0].toLowerCase();
			File file = DataManager.getFile(caixa, "caixas");
			if (!file.exists()) {
		        s.sendMessage("§cA caixa '" + caixa + "' não existe!");
		        ComandoCaixas.ListCaixas(s);
				return false;
			}
			 
			DataManager.deleteFile(file);
			s.sendMessage("§aCaixa '" + caixa + "' deletada com sucesso.");
		}
		return false;
	}
}
