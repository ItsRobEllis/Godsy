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
        yaml.loadConfiguration("plugins/Godsy/players.yml"); //ERRORING - Cannot Resolve method

        File f = new File("plugins/Godsy/config.yml");
        if(!f.exists() && !f.isDirectory()) {
            File dir = new File("plugins/Godsy");
            boolean successful = dir.mkdir();
            if (successful)
            {
                //Successfully created a directory
                if(!f.exists())
                {
                    this.saveDefaultConfig();
                }
                getLogger().info("Godsy config created");
            }
            else
            {
                // creating the directory failed
                getLogger().severe("Failed to create config file, using default");
            }
        }

        yaml.createSection("players");
        Bukkit.broadcastMessage("[" + ChatColor.AQUA + "Godsy" + ChatColor.RESET + "] " + "Godsy v" + ChatColor.GREEN + getDescription().getVersion() + ChatColor.RESET + " enabled!");
    }

    @Override
    public void onDisable(){
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
                        yaml.createSection("players." + ((Player) sender).getUniqueId() + ".username");
                        yaml.createSection("players." + ((Player) sender).getUniqueId() + ".god");
                        yaml.createSection("players." + ((Player) sender).getUniqueId() + ".faith");
                        yaml.set("players." + ((Player) sender).getUniqueId() + ".username", "\"" + sender.getName() + "\"");
                        yaml.set("players." + ((Player) sender).getUniqueId() + ".god", "\"" + getConfig().getString("gods." + args[0].toLowerCase() + ".name" + "\""));
                        yaml.set("players." + ((Player) sender).getUniqueId() + ".faith", 0);
                        yaml.save("plugins/Godsy/players.yml");

                        //sender.sendMessage(getConfig().getString(yaml.get("players." + ((Player) sender).getUniqueId() + ".god", getConfig().getString("gods." + args[0].toLowerCase() + ".name"))));

                    }
                    catch(Exception e){
                        Bukkit.broadcastMessage(e.getMessage());
                    }
                }
                else
                {
                    sender.sendMessage(
                            "[" + ChatColor.AQUA + "Godsy" + ChatColor.RESET + "] " + ": You would have to forsake §"
                            + getConfig().getString(
                                    "gods." + yaml.get("plugins/Godsy/players.yml", ("players." + ((Player) sender).getUniqueId() + ".god") + ".colour")
                            )
                            + getConfig().getString(
                                    "gods." + yaml.get("plugins/Godsy/players.yml", ("players." + ((Player) sender).getUniqueId() + ".god").toLowerCase())
                            )
                    );
                    //yaml.get("players." + ((Player) sender).getUniqueId() + ".god");
                }
                return true;
            }
        }
        return false;
    }
}