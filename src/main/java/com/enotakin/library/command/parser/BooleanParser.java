package com.enotakin.library.command.parser;

import com.enotakin.library.api.command.parser.ArgumentParser;
import org.bukkit.command.CommandSender;

public class BooleanParser implements ArgumentParser<Boolean> {

    @Override
    public Boolean parse(String object, CommandSender sender) {
        try {
            return Boolean.parseBoolean(object);
        } catch (NumberFormatException ex) {
            sender.sendMessage(String.format("§6The argument is specified incorrectly! §c%s §6isn't a logical operator.", object));
        }
        return null;
    }
}
