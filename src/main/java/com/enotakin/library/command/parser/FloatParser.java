package com.enotakin.library.command.parser;

import com.enotakin.library.api.command.parser.ArgumentParser;
import org.bukkit.command.CommandSender;

public class FloatParser implements ArgumentParser<Float> {

    @Override
    public Float parse(String object, CommandSender sender) {
        try {
            return Float.parseFloat(object);
        } catch (NumberFormatException e) {
            sender.sendMessage(String.format("§6The argument is specified incorrectly! §c%s §6isn't a fractional number.", object));
        }
        return null;
    }
}
