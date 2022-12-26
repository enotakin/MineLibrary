package com.enotakin.library.api.command.parser;

import org.bukkit.command.CommandSender;

public interface ArgumentParser<T> {

    T parse(String object, CommandSender sender);

}
