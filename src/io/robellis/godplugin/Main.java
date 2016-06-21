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
               // if(true/*TODO check if player has alignment*/)
                //{
                    //Send a confirmation message
                    try{
                        sender.sendMessage("[" + ChatColor.AQUA + "Godsy" + ChatColor.RESET + "] : You are now worshipping §" + getConfig().getInt("gods." + args[0].toLowerCase() + ".colour")
                                + getConfig().getString("gods." + args[0].toLowerCase() + ".name") + ChatColor.RESET + "!");

                        Player god = Bukkit.getServer().getPlayer(getConfig().getString("gods." + args[0].toLowerCase() + ".player"));
                        god.sendMessage("[" + ChatColor.AQUA + "Godsy" + ChatColor.RESET + "] : " + ChatColor.GREEN + sender.getName() + ChatColor.RESET + " is now worshipping you!");

                        //Get the player's location and play a sound
                        Player player=(Player) sender;
                        player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 1, 0);
                        //Give the player nausea
                        player.addPotionEffect(PotionEffectType.CONFUSION.createEffect((int) 500L, 0), true);
                    }
                    catch(Exception e){
                        Bukkit.broadcastMessage(e.getMessage());
                    }

                    // TODO write player alignment to file
                //}
                //else
                //{
                    /*sender.sendMessage("[" + ChatColor.AQUA + "Godsy" + ChatColor.RESET + "] " + ": You would have to forsake " + '§' + getConfig().getInt("gods." + TODO + ".colour")
                            + getConfig().getString("gods." + TODO + ".name") + ChatColor.RESET + " first!");*/
                //}
                return true;
            }
        }
        return false;
    }
}