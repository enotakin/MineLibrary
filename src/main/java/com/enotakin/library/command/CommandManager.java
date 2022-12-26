package com.enotakin.library.command;

import com.enotakin.library.LibraryManager;
import com.enotakin.library.api.command.annotation.*;
import com.enotakin.library.api.command.parser.ArgumentParser;
import com.enotakin.library.command.parser.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class CommandManager implements LibraryManager {

    private CommandMap commandMap;
    private final Map<Class, ArgumentParser> parserMap = new HashMap<>();

    @Override
    public void register() {
        registerArgumentParser(String[].class, new ArrayStringParser());
        registerArgumentParser(Boolean.class, new BooleanParser());
        registerArgumentParser(Double.class, new DoubleParser());
        registerArgumentParser(Float.class, new FloatParser());
        registerArgumentParser(GameMode.class, new GameModeParser());
        registerArgumentParser(Integer.class, new IntegerParser());
        registerArgumentParser(Long.class, new LongParser());
        registerArgumentParser(Player.class, new PlayerParser());
    }

    @Override
    public void unregister() {
    }

    public <T> void registerCommand(Plugin plugin, Class<T> command) {
        for (Annotation annotation : command.getDeclaredAnnotations()) {
            if (annotation.annotationType() == Command.class) {
                String name = command.getDeclaredAnnotation(Command.class).value();

                Permission permission = command.getDeclaredAnnotation(Permission.class);

                String[] aliases = new String[]{};
                if (command.isAnnotationPresent(Aliases.class)) {
                    String[] annotatedAliases = command.getDeclaredAnnotation(Aliases.class).value();
                    if (annotatedAliases != null)
                        aliases = annotatedAliases;
                }

                getCommandMap().register(plugin.getName().toLowerCase(), new org.bukkit.command.Command(name, "Command registered by MineLibrary", ("/").concat(name), Arrays.asList(aliases)) {
                    @Override
                    public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
                        if (permission != null && !sender.hasPermission(permission.value())) {
                            sender.sendMessage("§6You don't have enough permission to execute this command");
                            return true;
                        }

                        for (Method method : command.getDeclaredMethods()) {
                            for (Annotation annotation : method.getDeclaredAnnotations()) {
                                if (annotation.annotationType() == CommandHandler.class) {
                                    if (Arrays.stream(method.getParameters()).anyMatch(parameter -> parameter.isAnnotationPresent(Argument.class))) {

                                        List<Object> methodArgs = new ArrayList<>();
                                        methodArgs.add(sender);

                                        List<Parameter> parameters = Arrays.stream(method.getParameters())
                                                .filter(parameter -> parameter.isAnnotationPresent(Argument.class))
                                                .toList();

                                        for (int i = 0; i < parameters.size(); i++) {
                                            ArgumentParser parser = getParser(parameters.get(i).getType());
                                            if (parser != null) {
                                                if (args.length >= parameters.size()) {
                                                    if (parser.getClass() != ArrayStringParser.class) {
                                                        methodArgs.add(parser.parse(args[i], sender));
                                                    } else {
                                                        methodArgs.add(parser.parse(String.join(" ", Arrays.copyOfRange(args, i, args.length)), sender));
                                                    }
                                                }
                                            } else {
                                                throw new RuntimeException("Parser for " + parameters.get(i).getType() + " not found!");
                                            }
                                        }

                                        for (Object object : methodArgs)
                                            if (object == null)
                                                return false;

                                        if (args.length < parameters.size()) {
                                            sender.sendMessage("§6Not enough arguments!");
                                            return false;
                                        }

                                        try {
                                            method.invoke(command.newInstance(), methodArgs.toArray());
                                        } catch (IllegalAccessException | InvocationTargetException | InstantiationException ex) {
                                            ex.printStackTrace();
                                        }
                                    } else {
                                        try {
                                            method.invoke(command.newInstance(), sender, args);
                                        } catch (IllegalAccessException | InvocationTargetException | InstantiationException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                        return true;
                    }
                });
            }
        }
    }

    public <T> void registerArgumentParser(Class clazz, ArgumentParser<T> parser) {
        parserMap.put(clazz, parser);
    }

    private <T> ArgumentParser getParser(Class<T> type) {
        return switch (type.getSimpleName()) {
            case "boolean" -> parserMap.get(Boolean.class);
            case "double" -> parserMap.get(Double.class);
            case "float" -> parserMap.get(Float.class);
            case "int" -> parserMap.get(Integer.class);
            case "long" -> parserMap.get(Long.class);
            default -> parserMap.get(type);
        };
    }

    public CommandMap getCommandMap() {
        if (commandMap != null)
            return commandMap;
        try {
            Field field = CraftServer.class.getDeclaredField("commandMap");
            field.setAccessible(true);
            commandMap = (CommandMap) field.get(Bukkit.getServer());
            field.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return commandMap;
    }
}
