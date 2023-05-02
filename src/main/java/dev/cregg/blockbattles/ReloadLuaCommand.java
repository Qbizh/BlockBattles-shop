package dev.cregg.blockbattles;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReloadLuaCommand implements CommandExecutor {




    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        BlockListener.reloadLua();
        sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Reloaded lua at " + BlockListener.datapath);
        return true;
    }




}