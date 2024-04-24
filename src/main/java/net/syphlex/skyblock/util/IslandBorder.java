package net.syphlex.skyblock.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@UtilityClass
public class IslandBorder {

    public void setPlayerBorder(Player player, int size, Location center) {
        try {
            Object worldBorderPacket = createWorldBorderPacket(size, center);
            sendPacket(player, worldBorderPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object createWorldBorderPacket(int size, Location center) throws Exception {
        Object worldBorderPacket = getNMSClass("PacketPlayOutWorldBorder").getClass().newInstance();

        // Set action to set size and center
        setField(worldBorderPacket, "a", getEnumConstant("PacketPlayOutWorldBorder$EnumWorldBorderAction", "SET_SIZE"));

        // Set border size
        setField(worldBorderPacket, "i", size);

        // Set border center
        setField(worldBorderPacket, "f", center.getX());
        setField(worldBorderPacket, "g", center.getZ());

        // Optional: Set transition time and other properties if needed

        return worldBorderPacket;
    }

    public void sendPacket(Player player, Object packet) throws Exception {
        Object playerHandle = getPlayerHandle(player);
        Field playerConnectionField = playerHandle.getClass().getDeclaredField("playerConnection");
        playerConnectionField.setAccessible(true);
        Object playerConnection = playerConnectionField.get(playerHandle);
        Method sendPacketMethod = playerConnection.getClass().getMethod("sendPacket", (Class<?>) getNMSClass("Packet"));
        sendPacketMethod.invoke(playerConnection, packet);
    }

    public Object getPlayerHandle(Player player) throws Exception {
        Method getHandle = player.getClass().getMethod("getHandle");
        return getHandle.invoke(player);
    }

    public Object getNMSClass(String className) throws Exception {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        String fullName = "net.minecraft.server." + version + "." + className;
        return Class.forName(fullName);
    }

    public Object getEnumConstant(String enumClassName, String constantName) throws Exception {
        Class<?> enumClass = (Class<?>) getNMSClass(enumClassName);
        return Enum.valueOf((Class<? extends Enum>) enumClass, constantName);
    }

    public void setField(Object object, String fieldName, Object value) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }
}
