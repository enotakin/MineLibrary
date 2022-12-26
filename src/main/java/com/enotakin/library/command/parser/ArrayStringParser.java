package com.enotakin.library.command.parser;

import com.enotakin.library.api.command.parser.ArgumentParser;
import org.bukkit.command.CommandSender;

public class ArrayStringParser implements ArgumentParser<String[]> {

    @Override
    public String[] parse(String object, CommandSender sender) {
        return object.split(" ");
    }
}
