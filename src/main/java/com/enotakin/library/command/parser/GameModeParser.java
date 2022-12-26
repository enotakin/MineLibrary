package com.enotakin.library.command.parser;

import com.enotakin.library.api.command.parser.ArgumentParser;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;

public class GameModeParser implements ArgumentParser<GameMode> {

    @Override
    @SuppressWarnings("deprecation")
    public GameMode parse(String object, CommandSender sender) {
        try {
            return GameMode.getByValue(Integer.parseInt(object));
        } catch (NumberFormatException ignored) {}

        for (GameMode gameMode : GameMode.values())
            if (gameMode.name().startsWith(object.toUpperCase()))
                return gameMode;

        sender.sendMessage(String.format("§6The argument is specified incorrectly! §c%s §6isn't a game mode.", object));
        return null;
    }
}
