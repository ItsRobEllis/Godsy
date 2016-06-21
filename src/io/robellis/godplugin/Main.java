package io.robellis.godplugin;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Sound;

public class Main extends JavaPlugin {

    @Override
    public void onEnable(){
        //Fired when the server enables the plugin

        try
        {
            File f = new File("plugins/Godsy/config.yml");
            File dir = new File("plugins/Godsy");
            if (!dir.exists()) {
                dir.mkdir();
            }
            if (!f.exists()) {
                saveDefaultConfig();
            }
        }
        catch(Exception f)
        {
            f.printStackTrace();
        }

        getConfig().createSection("players");
        Bukkit.broadcastMessage("[" + ChatColor.AQUA + "Godsy" + ChatColor.RESET + "] " + "Godsy v" + ChatColor.GREEN + getDescription().getVersion() + ChatColor.RESET + " enabled!");
    }

    @Override
    public void onDisable() {
        try
        {
            saveConfig();
        }
        catch(Exception d)
        {
            d.printStackTrace();
        }

        //Fired when the server stops and disables all plugins
    }

    @Override   // Check if commands are triggered
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            if (command.getName().equalsIgnoreCase("worship")) {
                if(!getConfig().contains("players." + ((Player) sender).getUniqueId()))
                {
                    //Send a confirmation message
                    try{
                        sender.sendMessage("[" + ChatColor.AQUA + "Godsy" + ChatColor.RESET + "] : You are now worshipping §" + getConfig().getString("gods." + args[0].toLowerCase() + ".colour")
                                + getConfig().getString("gods." + args[0].toLowerCase() + ".name") + ChatColor.RESET + "!");

                        //sender.sendMessage("[" + ChatColor.RED + "DEBUG" + ChatColor.RESET + "] : " + getConfig().getString("gods." + args[0].toLowerCase() + ".player"));

                        Player isOnline = Bukkit.getPlayerExact(getConfig().getString("gods." + args[0].toLowerCase() + ".player"));
                        Player god = Bukkit.getServer().getPlayer(getConfig().getString("gods." + args[0].toLowerCase() + ".player"));
                        if(isOnline != null)
                        {
                            god.sendMessage("[" + ChatColor.AQUA + "Godsy" + ChatColor.RESET + "] : " + ChatColor.GREEN + sender.getName() + ChatColor.RESET + " is now worshipping you!");
                        }

                        //Get the player's location and play a sound
                        Player player=(Player) sender;
                        player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 1, 0);
                        //Give the player nausea
                        player.addPotionEffect(PotionEffectType.CONFUSION.createEffect((int) 500L, 0), true);

                        getConfig().createSection("players." + ((Player) sender).getUniqueId());

                        getConfig().set("players." + ((Player) sender).getUniqueId() + ".username", sender.getName());
                        getConfig().set("players." + ((Player) sender).getUniqueId() + ".god", args[0]);
                        getConfig().set("players." + ((Player) sender).getUniqueId() + ".faith", 0);

                        //sender.sendMessage(getConfig().getString(getConfig().get("players." + ((Player) sender).getUniqueId() + ".god", getConfig().getString("gods." + args[0].toLowerCase() + ".name"))));

                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
                else
                {
                    String senderGod = getConfig().getString("players." + ((Player) sender).getUniqueId() + ".god").toLowerCase();
                    sender.sendMessage(
                        "[" + ChatColor.AQUA + "Godsy" + ChatColor.RESET + "] " + ": You would have to forsake §"
                                + getConfig().getString
                            (
                                "gods." + senderGod + ".colour"
                            )
                                + getConfig().getString(
                                "gods." + senderGod + ".name"
                            )
                        + ChatColor.RESET + " to do that!"
                    );
                }
                return true;
            }
        }
        return false;
    }
}