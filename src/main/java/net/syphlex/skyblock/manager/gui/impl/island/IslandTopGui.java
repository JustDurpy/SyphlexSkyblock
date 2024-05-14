package net.syphlex.skyblock.manager.gui.impl.island;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.gui.type.ClickEvent;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.manager.leaderboard.LeaderData;
import net.syphlex.skyblock.util.ItemBuilder;
import net.syphlex.skyblock.util.simple.SimpleGui;
import net.syphlex.skyblock.util.utilities.IslandUtil;
import net.syphlex.skyblock.util.utilities.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class IslandTopGui extends SimpleGui {
    public IslandTopGui() {
        super("Top Islands", 27);

        fill(new ItemBuilder()
                .setMaterial(Material.BLACK_STAINED_GLASS_PANE)
                .setName(" ")
                .build());

        int slot = 0;
        int size = Math.min(10, Skyblock.get().getLeaderboardHandler().getTopIslands().getSize()-1);
        for (int i = size;
             i >= 0; i--) {
            if (i == size) {
                slot = 17;
            }
            if (i == 0) {
                slot = 4;
            }

            LeaderData leaderData = Skyblock.get().getLeaderboardHandler().getTopIslands().get(i);

            int[] islandId = IslandUtil.getId(leaderData.getKey().toString());
            Island island = IslandUtil.getIsland(islandId);

            if (island == null) continue;

            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) item.getItemMeta();

            String title = (i + 1) + ". " + island.getLeader().getUsername();
            String[] colors = new String[]{"#A482F8", "#580EAA"};

            meta.setDisplayName(StringUtil.CC("&l" + StringUtil.createGradFromString(title, colors)));
            meta.setLore(StringUtil.CC(Arrays.asList(
                    "",
                    StringUtil.createGradFromString("Information:", colors),
                    StringUtil.parseHex(" ➥ ", "#2D0758")
                            + StringUtil.createGradFromString("Worth: ", colors)
                            + "&f$" + String.format("%,d", (int)island.getWorth()),
                    StringUtil.parseHex(" ➥ ", "#2D0758")
                            + StringUtil.createGradFromString("Members: ", colors)
                            + "&f" + island.getMembers().size() + "/" + island.getUpgrades().getTeamSize().get(),
                    "",
                    "&7&o(( Click to view island information ))",
                    ""
            )));
            meta.setOwnerProfile(Bukkit.getOfflinePlayer(island.getLeader().getUuid()).getPlayerProfile());
            item.setItemMeta(meta);
            inventory.setItem(slot, item);
            //slots.add(new Pair<>(slot, clan));
            slot--;
        }

        setIslandPanelButton(22);
    }

    @Override
    public void onClickEvent(ClickEvent e) {
    }
}
