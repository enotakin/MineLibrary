package com.enotakin.library.command.parser;

import com.enotakin.library.api.command.parser.ArgumentParser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerParser implements ArgumentParser<Player> {

    @Override
    public Player parse(String object, CommandSender sender) {
        Player player = Bukkit.getPlayerExact(object);
        if (player == null)
            sender.sendMessage(String.format("§6The argument is specified incorrectly! Player §c%s §6offline.", object));
        return player;
    }
}
