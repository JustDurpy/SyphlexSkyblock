package net.syphlex.skyblock.util;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class StringUtil {

    public String CC(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> CC(List<String> list) {
        List<String> translated = new ArrayList<>();
        for (String str : list) {
            translated.add(CC(str));
        }
        return translated;
    }

    public boolean isDigit(String s){
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public String parseHex(String msg, String hex){
        return net.md_5.bungee.api.ChatColor.of(hex) + msg;
    }
}
