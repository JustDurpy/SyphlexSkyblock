package net.syphlex.skyblock.util.utilities;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
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

    public boolean isNumber(String s){
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

    private final static TreeMap<Integer, String> map = new TreeMap<Integer, String>();

    static {

        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");

    }

    public final static String intToRoman(int number) {
        int l =  map.floorKey(number);
        if ( number == l ) {
            return map.get(number);
        }
        return map.get(l) + intToRoman(number-l);
    }

    private static int value(char r) {
        if (r == 'I')
            return 1;
        if (r == 'V')
            return 5;
        if (r == 'X')
            return 10;
        if (r == 'L')
            return 50;
        if (r == 'C')
            return 100;
        if (r == 'D')
            return 500;
        if (r == 'M')
            return 1000;
        return -1;
    }

    public static int romanToInt(String s) {
        int total = 0;

        for (int i = 0; i < s.length(); i++) {
            int s1 = value(s.charAt(i));
            if (i + 1 < s.length()) {
                int s2 = value(s.charAt(i + 1));

                if (s1 >= s2) {
                    total = total + s1;
                } else {
                    total = total - s1;
                }
            } else {
                total = total + s1;
            }
        }

        return total;
    }
}
