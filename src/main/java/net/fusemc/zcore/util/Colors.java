package net.fusemc.zcore.util;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

/**
 * Copyright by michidk
 * Created: 16.10.2014.
 */
public class Colors {

    public static final String PARAGRAPH = "\u00A7";
    public static final String PRIMARY = PARAGRAPH + "a";
    public static final String SECONDARY = PARAGRAPH + "3";
    public static final String WARNING = PARAGRAPH + "c";
    public static final String ERROR = PARAGRAPH + "4";

    //gives you somethink like a ampel.. with 100% it is green with 0 percent its red
    public static final List<ChatColor> colorLevel = Arrays.asList(new ChatColor[]{ChatColor.GREEN, ChatColor.DARK_GREEN, ChatColor.RED, ChatColor.DARK_RED});
    public static ChatColor getWarningColor(double percentage)
    {

        if (percentage <= 0)
        {
            return colorLevel.get(colorLevel.size() - 1);
        }

        int i = (int) (percentage * colorLevel.size());

        if (i >= colorLevel.size())
        {
            i = colorLevel.size() - 1;
        }

        i = colorLevel.size() - i - 1;

        return colorLevel.get(i);
    }

}
