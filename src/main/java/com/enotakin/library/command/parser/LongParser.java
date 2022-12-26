package com.enotakin.library.command.parser;

import com.enotakin.library.api.command.parser.ArgumentParser;
import org.bukkit.command.CommandSender;

public class LongParser implements ArgumentParser<Long> {

    @Override
    public Long parse(String object, CommandSender sender) {
        try {
            return Long.parseLong(object);
        } catch (NumberFormatException e) {
            sender.sendMessage(String.format("§6The argument is specified incorrectly! §c%s §6isn't a integer.", object));
        }
        return null;
    }
}
