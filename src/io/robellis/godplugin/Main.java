package io.robellis.godplugin;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Sound;

public class Main extends JavaPlugin {

    YamlConfiguration yaml = new YamlConfiguration();
    @Override
    public void onEnable(){
        //Fired when the server enables the plugin

        try
        {
            File p = new File("plugins/Godsy/players.yml");
            File f = new File("plugins/Godsy/config.yml");
            File dir = new File("plugins/Godsy");
            if (!dir.exists()) {
                dir.mkdir();
            }
            if (!p.exists()) {
                p.createNewFile();
            }
            if (!f.exists()) {
                saveDefaultConfig();
            }
            yaml.loadConfiguration(p);
        }
        catch(Exception f)
        {
            f.printStackTrace();
        }

        yaml.createSection("players");
        Bukkit.broadcastMessage("[" + ChatColor.AQUA + "Godsy" + ChatColor.RESET + "] " + "Godsy v" + ChatColor.GREEN + getDescription().getVersion() + ChatColor.RESET + " enabled!");
    }

    @Override
    public void onDisable() {
        try
        {
            yaml.save("plugins/Godsy/players.yml");
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
                if(!yaml.contains("players." + ((Player) sender).getUniqueId()))
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

                        yaml.createSection("players." + ((Player) sender).getUniqueId());

                        yaml.set("players." + ((Player) sender).getUniqueId() + ".username", sender.getName());
                        yaml.set("players." + ((Player) sender).getUniqueId() + ".god", getConfig().getString("gods." + args[0].toLowerCase() + ".name"));
                        yaml.set("players." + ((Player) sender).getUniqueId() + ".faith", 0);

                        //sender.sendMessage(getConfig().getString(yaml.get("players." + ((Player) sender).getUniqueId() + ".god", getConfig().getString("gods." + args[0].toLowerCase() + ".name"))));

                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
                else
                {

                    String senderGod = yaml.getString("players." + ((Player) sender).getUniqueId() + ".god");
                    sender.sendMessage(
                            "[" + ChatColor.AQUA + "Godsy" + ChatColor.RESET + "] " + ": You would have to forsake §"
                            + getConfig().getString(
                                    "gods." + senderGod + ".colour"
                            )
                            + getConfig().getString(
                                    "gods." + senderGod + ".name"
                            )
                    );

                }
                return true;
            }
        }
        return false;
    }
}