package com.enotakin.library.command.parser;

import com.enotakin.library.api.command.parser.ArgumentParser;
import org.bukkit.command.CommandSender;

public class DoubleParser implements ArgumentParser<Double> {

    @Override
    public Double parse(String object, CommandSender sender) {
        try {
            return Double.parseDouble(object);
        } catch (NumberFormatException ex) {
            sender.sendMessage(String.format("§6The argument is specified incorrectly! §c%s §6isn't a fractional number.", object));
        }
        return null;
    }
}
