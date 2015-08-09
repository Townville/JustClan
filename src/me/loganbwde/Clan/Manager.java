package me.loganbwde.Clan;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Manager 
{
	main m;
	FileConfiguration clan;
	FileConfiguration config;
	File file;
	File file2;
	Map<String, String> invites = new HashMap();
	
	
	public Manager(main m)
	{
		this.m = m;
		file = new File(m.getDataFolder(), "Clans.yml");
		file2 = new File(m.getDataFolder(), "Config.yml");
		clan = YamlConfiguration.loadConfiguration(file);
		config = YamlConfiguration.loadConfiguration(file2);
		saveDefaultClans();
	}
	
	public void ClanCreate(String clan, String owner, String tag)
	{
		this.clan.set("Clans." + clan + ".Tag", tag);
		this.clan.set("Clans." + clan + ".Owner", owner);
		this.clan.set("Member." + owner + ".Clan", clan);
		joinClan(clan,owner);
		saveClans();
	}
	
	public void saveClans()
    {
		try
	    {
	      this.clan.save(this.file);
	    }
	    catch (Exception localException) {}
	}
	  
	public void saveDefaultClans()
	{
	    InputStream defConfigStream = this.m.getResource("Clans.yml");
	    if (defConfigStream != null)
	    {
	      @SuppressWarnings("deprecation")
		YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	      this.clan.setDefaults(defConfig);
	      this.clan.options().copyDefaults(true);
	    }
	    saveClans();
	}
	
	public boolean HaveClan(String p)
	{
		if(clan.get("Clans." + getClan(p) + ".Owner" ) != null || clan.get("Member." + p + ".Clan" ) != null)
		{
			return true;
		}
		return false;
	}
	public String getClan(String p)
	{
		String clanName = null;
		clanName = clan.getString("Member." + p + ".Clan");
		return clanName;
	}
	public String getClanTag(String p)
	{
		String clanTag = null;
		String clanName = getClan(p);
		clanTag = clan.getString("Clans." + clanName + ".Tag");
		return clanTag;
	}
	
	public String getTagColour()
	{
		return "§b";
	}
	public void remClan(String clan, String p)
	{
		@SuppressWarnings("rawtypes")
		List member = this.clan.getStringList("Clans." + clan + ".Member");
		for(int i = 0;i < member.size();i++)
		{
			Player me = Bukkit.getPlayer((String)member.get(i));
			if(me != null)
			{
				me.sendMessage(m.prefix + " Der Clan wurde geloescht");
			}
		}
		for(int j = 0;j < member.size();j++)
		{
			this.clan.set("Clans." + clan + ".Member", null);
		}
		this.clan.set("Clans." + clan, null);
		this.clan.set("Member." + p,null);
		saveClans();
	}
	
	public void joinClan(String cl, String p)
	{
		List member = this.clan.getStringList("Clans." + cl + ".Member");
		this.clan.set("Member." + p + ".Clan", cl);
		member.add(p);
		this.clan.set("Clans." + cl + ".Member", member);
		saveClans();
	}
	
	public boolean isOwner(String p)
	{
		if(clan.getString("Clans." + getClan(p) + ".Owner").matches(p))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public String getClanOwner(String cl)
	{
		String owner = null;
		owner = clan.getString("Clans." + cl + ".Owner");
		return owner;
	}
	
	@EventHandler
	public void ClanChat(AsyncPlayerChatEvent e)
	{
		Player p = e.getPlayer();
		String pName = p.getName();
		if(HaveClan(pName))
		{
			e.setFormat(getTagColour() + getClanTag(pName) + " §r§a[" + pName + "] §c: §e" + e.getMessage());
		}
		else
		{
			e.setFormat("§a[" + pName + "] §c: §e" + e.getMessage());
		}
		
		
		if(e.getMessage().startsWith(m.chatprefix))
		{
			if(HaveClan(pName))
			{
				for(int i = 0;i<getMember(getClan(pName), pName).size();i++)
				{
					Player pl = Bukkit.getPlayer((String)getMember(getClan(pName), pName).get(i));
					if(pl != null)
					{
						pl.sendMessage(ChatColor.translateAlternateColorCodes('&',m.prefixClan + " §a" + e.getPlayer().getDisplayName() + ": " + e.getMessage().substring(1, e.getMessage().length())));
					}
				}
				e.setCancelled(true);
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<String> getMember(String cl, String p)
	  {
		List list = clan.getStringList("Clans." + getClan(p) + ".Member");
	    return list;
	  }
	
	public void invClan(String invited, String inviter)
	{
		this.invites.put(invited, inviter);
	}
	
	public void leaveClan(String cl, String p)
	{
		List member = this.clan.getStringList("Clans." + cl + ".Member");
		member.remove(p);
		this.clan.set("Clans." + cl + ".Member", member);
		saveClans();
	}
}
