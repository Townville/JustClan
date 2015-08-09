package me.loganbwde.Clan;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class main extends JavaPlugin implements Listener
{
	protected Manager man;
	
	String prefix = getConfig().getString("Config.prefix");
	String rl = getConfig().getString("Config.rl");
	String noperm = getConfig().getString("Config.noperm");
	String noclan = getConfig().getString("Config.noclan");
	String remclan = getConfig().getString("Config.remclan");
	String notyourclan = getConfig().getString("Config.notyourclan");
	String joinedclan = getConfig().getString("Config.joinedclan");
	String crclan = getConfig().getString("Config.crclan");
	String prefixClan = getConfig().getString("Config.prefixClan");
	String chatprefix = getConfig().getString("Config.chatprefix");
	String claninv1 = getConfig().getString("Config.claninv1");
	String claninv2 = getConfig().getString("Config.claninv2");
	
	public void onEnable()
	{
		this.man = new Manager(this);
		this.getServer().getPluginManager().registerEvents(this, this);
		loadConfig();
		System.out.println(prefix + " Plugin aktiviert.");	
	}
	public void onDisable()
	{
		System.out.println(prefix + " Plugin deaktiviert.");	
	}
	public void loadConfig()
    {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	public boolean onCommand(CommandSender sender,Command cmd,String cmdlabel,String[] args)
	{
		Player p = (Player) sender;
		String pName = p.getName();
		if(cmd.getName().equalsIgnoreCase("clreload"))
		{
			if(p.hasPermission("Clan.reload"))
			{
				reloadConfig();
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', rl));
			}
			else
			{
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', noperm));
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("clan"))
		{
		  if(args.length == 0)
		  {
			if(p.hasPermission("Clan.Base"))
			{
				menu(p);
			}
		  }
		  if(args[0].equalsIgnoreCase("info"))
		  {
			if(p.hasPermission("Clan.Base"))
			{
				if(man.HaveClan(pName) == true)
				{
					ClanMenu(p);
				}
				else
				{
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + " " + noclan));
				}
				
			}
			else
			{
			    p.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + " " + noclan));
			}
		  }
			
		  if(args[0].equalsIgnoreCase("create"))
		  {
			  man.ClanCreate(args[1], p.getName(), args[2]);
			  p.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + " " + crclan));
		  }
		  
		  if(args[0].equalsIgnoreCase("delete"))
		  {
			  if(man.HaveClan(pName) == true)
			  {
				  if(man.isOwner(pName) == true)
				  {
					  man.remClan(man.getClan(pName),pName);
					  p.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + " " + remclan));
				  }
				  else
				  {
					  p.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + " " + notyourclan));
				  }
			  }
			  else
			  {
				  p.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + " " + noclan));
			  }
		  }
		  
		  if(args[0].equalsIgnoreCase("leave"))
		  {
			  man.leaveClan(man.getClan(pName), pName);
		  }
		  
		  if(args[0].equalsIgnoreCase("invite"))
		  {
			  man.invClan(pName,args[1]);
			  Player p2 = Bukkit.getServer().getPlayer(args[1]);
			  p2.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + " " + claninv1 + man.getClan(pName) + claninv2));
		  }
		  
		  if(args[0].equalsIgnoreCase("accept"))
		  {
			  man.joinClan(args[1], p.getName());
		  }
		  
		  if(args[0].equalsIgnoreCase("deny"))
		  {
		  }
		  
		  if(args[0].equalsIgnoreCase("kick"))
		  {
		  }
		  
		  if(args[0].equalsIgnoreCase("pay"))
		  {
		  }
		  
		  if(args[0].equalsIgnoreCase("rankup"))
		  {
		  }
		  
		  if(args[0].equalsIgnoreCase("join"))
		  {
			  man.joinClan(args[1], pName);
		  }
		  
	    }
		return true;
	}
	
	public void menu(CommandSender sender)
	{
		Player p = (Player) sender;
		
		p.sendMessage("§3[======================§cClan§3=====================]");
		p.sendMessage("§6/clan : Zeigt diesen Text an");
		p.sendMessage("§6/clan info: Zeigt Infos über deinen Clan");
		p.sendMessage("§6/clan create <Name> <Tag> : Erstellt einen Clan");
		p.sendMessage("§6/clan delete : Löscht deinen Clan");
		p.sendMessage("§6/clan leave : Verlässt den Clan");
		p.sendMessage("§6/clan invite <Player>: Lädt jmd zu deinem Clan ein");
		p.sendMessage("§6/clan accept : Nimmt die Clan-Anfrage an");
		p.sendMessage("§6/clan deny : Lehnt die Clan-Anfrage ab");
		p.sendMessage("§6/clan kick <Player> : Kickt einen Spieler aus dem Clan");
		p.sendMessage("§6/clan pay <Level> : Zahle Level an den Clan");
		p.sendMessage("§6/clan rankup : Erhöht den Clan Level");
		p.sendMessage("§6#<Nachricht> : Schreibt eine Nachricht im Clan Chat");
		p.sendMessage("§3[===================================================]");
		
	}
   public void ClanMenu(CommandSender sender)
   {
	   Player p = (Player) sender;
	   String pName = p.getName();
	   p.sendMessage("§3[====================§cDein Clan§3===================]");
	   p.sendMessage("§3Clan-Name: §c" + man.getClan(pName));
	   p.sendMessage("§3Clan-Tag : §c" + man.getClanTag(pName));
	   p.sendMessage("§3Clan-Inhaber: §c" + man.getClanOwner(man.getClan(pName)));
	   p.sendMessage("§3Clan-Mitglieder: §c" + man.getMember(man.getClan(pName),pName));
	   p.sendMessage("§3[===============================================]");
	   
	   
   }
   
   @EventHandler
   public void ClanChat(AsyncPlayerChatEvent e)
	{
		man.ClanChat(e);
	}
}
