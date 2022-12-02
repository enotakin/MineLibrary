package com.enotakin.library.api.util;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;

import java.util.regex.Pattern;

public final class Cmpt {

    // On version 1.19, the component cannot be empty,
    // so we insert the symbol §r
    public static final Component EMPTY = Component.text(ChatColor.RESET.toString());

    public static final TextColor WHITE_COLOR = TextColor.color(0xFFFFFF);

    public static final Pattern COLOR_PATTERN = Pattern.compile("(?i)§[\\dA-F]");

    private Cmpt() {
    }

    public static boolean isEmpty(Component parent) {
        if (parent instanceof TextComponent textParent) {
            if (textParent.content().length() > 0) {
                return false;
            }

            for (Component child : textParent.children()) {
                if (child instanceof TextComponent textChild) {
                    if (textChild.content().length() > 0) {
                        return false;
                    }
                }

                if (!isEmpty(child)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static String getText(Component parent) {
        StringBuilder text = new StringBuilder();

        if (parent instanceof TextComponent textParent) {
            text.append(textParent.content());
        }

        for (Component child : parent.children()) {
            text.append(getText(child));
        }

        return text.toString();
    }

    public static Component format(String message) {
        Component component = Component.empty();

        int beginOpenIndex;
        while ((beginOpenIndex = message.indexOf("<text ")) != -1) {
            int endOpenIndex = message.indexOf(">");
            int closeIndex = message.indexOf("</text>");

            if (beginOpenIndex >= endOpenIndex) {
                throw new IllegalStateException("Wrong <text> format [" + beginOpenIndex + ", " + endOpenIndex + ", " + closeIndex + "]");
            }

            if (closeIndex <= beginOpenIndex) {
                throw new IllegalStateException("Wrong <text> format [" + beginOpenIndex + ", " + endOpenIndex + ", " + closeIndex + "]");
            }

            if (beginOpenIndex > 0) {
                component = component.append(Component.text(message.substring(0, beginOpenIndex)));
            }

            String text = message.substring(endOpenIndex + 1, closeIndex);

            text = COLOR_PATTERN
                    .matcher(text)
                    .replaceAll("");

            if (text.length() > 0) {
                String font = null;
                TextColor color = null;
                TextColor fromColor = null;
                TextColor toColor = null;

                String[] args = message
                        .substring(beginOpenIndex + 6, endOpenIndex)
                        .split(" ");

                for (String parameter : args) {
                    parameter = parameter.trim();

                    String[] parameterArgs = parameter.split("=");
                    if (parameterArgs.length != 2) {
                        throw new IllegalStateException("Parameter must contain key and value: " + parameter);
                    }

                    String key = parameterArgs[0];
                    String value = parameterArgs[1];

                    if (value.startsWith("\"") || value.startsWith("'")) {
                        value = value.substring(1);
                    }

                    if (value.endsWith("\"") || value.endsWith("'")) {
                        value = value.substring(0, value.length() - 1);
                    }

                    switch (key) {
                        case "font" -> font = value;
                        case "color" -> {
                            if (value.startsWith("#") && value.length() == 7) {
                                color = TextColor.fromHexString(value);
                            } else {
                                throw new IllegalStateException("Wrong color format specified: " + value);
                            }
                        }
                        case "gradient" -> {
                            String[] gradientArgs = value.split("-");
                            if (gradientArgs.length != 2) {
                                throw new IllegalStateException("Gradient must have two colors: " + value);
                            }

                            String gradientFrom = gradientArgs[0];
                            if (gradientFrom.startsWith("#") && gradientFrom.length() == 7) {
                                fromColor = TextColor.fromHexString(gradientFrom);
                            } else {
                                throw new IllegalStateException("Wrong color format specified: " + gradientFrom);
                            }

                            String gradientTo = gradientArgs[1];
                            if (gradientTo.startsWith("#") && gradientTo.length() == 7) {
                                toColor = TextColor.fromHexString(gradientTo);
                            } else {
                                throw new IllegalStateException("Wrong color format specified: " + gradientTo);
                            }
                        }
                    }
                }

                Component textComponent = Component.empty();
                if (font != null) {
                    textComponent = textComponent.style(
                            Style.style()
                                    .font(Key.key(font))
                                    .build()
                    );
                }

                if (color != null) {
                    textComponent = textComponent.append(Component.text(text, color));
                } else if (fromColor != null && toColor != null) {
                    textComponent = textComponent.append(gradient(text, fromColor.value(), toColor.value()));
                } else {
                    textComponent = textComponent.append(Component.text(text));
                }

                component = component.append(textComponent);
            }

            message = message.substring(closeIndex + 7);
        }

        if (message.length() > 0) {
            component = component.append(Component.text(message));
        }

        return component;
    }

    public static Component color(String message, int color) {
        return Component.text(message, TextColor.color(color));
    }

    public static Component gradient(String message, int fromColor, int toColor) {
        if (message.endsWith(String.valueOf(ChatColor.COLOR_CHAR))) {
            message = message.substring(0, message.length() - 1);
        }

        Component component = Component.empty();

        int fromR = (fromColor >> 16) & 0xFF;
        int fromG = (fromColor >> 8) & 0xFF;
        int fromB = fromColor & 0xFF;

        int toR = (toColor >> 16) & 0xFF;
        int toG = (toColor >> 8) & 0xFF;
        int toB = toColor & 0xFF;

        char[] array = message.toCharArray();
        int size = message.length();
        for (char symbol : array) {
            if (symbol == ChatColor.COLOR_CHAR) {
                size -= 2;
            }
        }

        boolean bold = false;
        boolean italic = false;
        boolean underlined = false;
        boolean strikethrough = false;
        boolean obfuscated = false;

        int step = 0;
        int index = 0;
        while (index < array.length) {
            char symbol = array[index];
            if (symbol == ChatColor.COLOR_CHAR) {
                char colorSymbol = array[index + 1];
                switch (colorSymbol) {
                    case 'l', 'L' -> bold = true;
                    case 'o', 'O' -> italic = true;
                    case 'n', 'N' -> underlined = true;
                    case 'm', 'M' -> strikethrough = true;
                    case 'k', 'K' -> obfuscated = true;
                    case 'r', 'R' -> {
                        bold = false;
                        italic = false;
                        underlined = false;
                        strikethrough = false;
                        obfuscated = false;
                    }
                }

                index += 2;
                continue;
            }

            float percent = (step * 1F) / size;
            int r = Math.round(fromR + percent * (toR - fromR)) & 0xFF;
            int g = Math.round(fromG + percent * (toG - fromG)) & 0xFF;
            int b = Math.round(fromB + percent * (toB - fromB)) & 0xFF;

            Component symbolComponent = Component.text(symbol, TextColor.color(r, g, b));

            if (bold) {
                symbolComponent = symbolComponent.decoration(TextDecoration.BOLD, true);
            }

            if (italic) {
                symbolComponent = symbolComponent.decoration(TextDecoration.ITALIC, true);
            }

            if (underlined) {
                symbolComponent = symbolComponent.decoration(TextDecoration.UNDERLINED, true);
            }

            if (strikethrough) {
                symbolComponent = symbolComponent.decoration(TextDecoration.STRIKETHROUGH, true);
            }

            if (obfuscated) {
                symbolComponent = symbolComponent.decoration(TextDecoration.OBFUSCATED, true);
            }

            component = component.append(symbolComponent);

            step += 1;
            index += 1;
        }

        return component;
    }

}
