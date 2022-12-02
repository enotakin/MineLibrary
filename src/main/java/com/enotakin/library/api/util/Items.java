package com.enotakin.library.api.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Items {

    public static final ItemFlag[] FLAGS = ItemFlag.values();

    private Items() {
    }

    public static ItemStack head(String value) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        itemStack = MCHelper.setSkullValue(itemStack, value);
        return itemStack;
    }

    public static ItemStack head(String value, String displayName, String... lore) {
        return head(value, displayName, Arrays.asList(lore));
    }

    public static ItemStack head(String value, String displayName, List<String> lore) {
        Component wrappedDisplayName = Component.text(displayName);

        List<Component> wrappedLore = new ArrayList<>();
        for (String line : lore) {
            wrappedLore.add(Component.text(line));
        }

        return head(value, wrappedDisplayName, wrappedLore);
    }

    public static ItemStack head(String value, Component displayName, Component... lore) {
        return head(value, displayName, Arrays.asList(lore));
    }

    public static ItemStack head(String value, Component displayName, List<Component> lore) {
        ItemStack itemStack = name(Material.PLAYER_HEAD, displayName, lore);
        itemStack = MCHelper.setSkullValue(itemStack, value);
        return itemStack;
    }

    public static ItemStack name(Material type, String name, String... lore) {
        return name(type, name, Arrays.asList(lore));
    }

    public static ItemStack name(ItemStack itemStack, String name, String... lore) {
        return name(itemStack, name, Arrays.asList(lore));
    }

    public static ItemStack name(Material type, String displayName, List<String> lore) {
        return name(new ItemStack(type), displayName, lore);
    }

    public static ItemStack name(ItemStack itemStack, String displayName, List<String> lore) {
        Component wrappedDisplayName = Component.text(displayName);

        List<Component> wrappedLore = new ArrayList<>();
        for (String line : lore) {
            wrappedLore.add(Component.text(line));
        }

        return name(itemStack, wrappedDisplayName, wrappedLore);
    }

    public static ItemStack name(Material type, Component displayName, Component... lore) {
        return name(type, displayName, Arrays.asList(lore));
    }

    public static ItemStack name(ItemStack itemStack, Component displayName, Component... lore) {
        return name(itemStack, displayName, Arrays.asList(lore));
    }

    public static ItemStack name(Material type, Component displayName, List<Component> lore) {
        return name(new ItemStack(type), displayName, lore);
    }

    public static ItemStack name(ItemStack itemStack, Component displayName, List<Component> lore) {
        Component formattedName = Component.empty();
        formattedName = formattedName.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
        formattedName = formattedName.color(Cmpt.WHITE_COLOR);
        formattedName = formattedName.append(displayName);

        List<Component> formattedLore = new ArrayList<>();
        for (Component line : lore) {
            Component formattedLine = Component.empty();
            formattedLine = formattedLine.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
            formattedLine = formattedLine.color(Cmpt.WHITE_COLOR);
            formattedLine = formattedLine.append(line);
            formattedLore.add(formattedLine);
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(formattedName);
        itemMeta.lore(formattedLore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static ItemStack lore(Material type, String... lore) {
        return lore(new ItemStack(type), lore);
    }

    public static ItemStack lore(ItemStack itemStack, String... lore) {
        List<Component> wrappedLore = new ArrayList<>();
        for (String line : lore) {
            wrappedLore.add(Component.text(line));
        }

        return lore(itemStack, wrappedLore);
    }

    public static ItemStack lore(Material type, Component... lore) {
        return lore(type, Arrays.asList(lore));
    }

    public static ItemStack lore(ItemStack itemStack, Component... lore) {
        return lore(itemStack, Arrays.asList(lore));
    }

    public static ItemStack lore(Material type, List<Component> lore) {
        return lore(new ItemStack(type), lore);
    }

    public static ItemStack lore(ItemStack itemStack, List<Component> lore) {
        List<Component> formattedLore = new ArrayList<>();
        for (Component line : lore) {
            Component formattedLine = Component.empty();
            formattedLine = formattedLine.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
            formattedLine = formattedLine.color(Cmpt.WHITE_COLOR);
            formattedLine = formattedLine.append(line);
            formattedLore.add(formattedLine);
        }

        itemStack.lore(formattedLore);

        return itemStack;
    }

}
