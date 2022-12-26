package com.enotakin.library.command.parser;

import com.enotakin.library.api.command.parser.ArgumentParser;
import org.bukkit.command.CommandSender;

public class IntegerParser implements ArgumentParser<Integer> {

    @Override
    public Integer parse(String object, CommandSender sender) {
        try {
            return Integer.parseInt(object);
        } catch (NumberFormatException e) {
            sender.sendMessage(String.format("§6The argument is specified incorrectly! §c%s §6isn't a integer.", object));
        }
        return null;
    }
}
