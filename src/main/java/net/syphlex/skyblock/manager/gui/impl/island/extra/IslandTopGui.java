package net.syphlex.skyblock.manager.gui.impl.island.extra;

import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.manager.gui.type.ClickEvent;
import net.syphlex.skyblock.manager.gui.type.GuiItem;
import net.syphlex.skyblock.manager.island.data.Island;
import net.syphlex.skyblock.manager.leaderboard.LeaderData;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.config.ConfigMenu;
import net.syphlex.skyblock.util.data.Pair;
import net.syphlex.skyblock.util.simple.SimpleGui;
import net.syphlex.skyblock.util.utilities.IslandUtil;
import net.syphlex.skyblock.util.utilities.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class IslandTopGui extends SimpleGui {

    private final ArrayList<Pair<Integer, Island>> slotCache = new ArrayList<>();

    public IslandTopGui() {
        super(ConfigMenu.TOP_ISLANDS_MENU.getMenuSetting().getMenuTitle(),
                ConfigMenu.TOP_ISLANDS_MENU.getMenuSetting().getMenuSize());

        fill(ConfigMenu.TOP_ISLANDS_MENU);

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

            GuiItem guiItem = ConfigMenu.TOP_ISLANDS_MENU.getMenuSetting().getItems().get(0);

            if (guiItem == null) {
                // todo setup error system with codes for error catching
                return;
            }

            if (guiItem.getId() == 99) break;

            ItemStack item = guiItem.getItem();
            ItemMeta meta = item.getItemMeta();

            //String title = (i + 1) + ". " + island.getLeader().getUsername();
            //String[] colors = new String[]{"#A482F8", "#580EAA"};

            meta.setDisplayName(StringUtil.HexCC(item.getItemMeta().getDisplayName()
                    .replace("%position%",
                            String.valueOf((i+1)))
                    .replace("%leader_name%",
                            island.getLeader().getUsername())));

            //meta.setDisplayName(StringUtil.CC("&l" + StringUtil.createGradFromString(title, colors)));
            meta.getLore().replaceAll(s -> s.replace("%worth%", String.format("%,d", (int)island.getWorth()))
                    .replace("%team_size%", String.valueOf(island.getMembers().size()))
                    .replace("%max_team_size%", String.valueOf(island.getUpgrades().getTeamSize().getAsInt())));
           /*
            meta.setLore(StringUtil.CC(Arrays.asList(
                    "",
                    StringUtil.createGradFromString("Information:", colors),
                    StringUtil.parseHex(" ➥ ", "#2D0758")
                            + StringUtil.createGradFromString("Worth: ", colors)
                            + "&f$" + String.format("%,d", (int)island.getWorth()),
                    StringUtil.parseHex(" ➥ ", "#2D0758")
                            + StringUtil.createGradFromString("Members: ", colors)
                            + "&f" + island.getMembers().size() + "/" + island.getUpgrades().getTeamSize().getAsInt(),
                    "",
                    "&7&o(( Click to view island information ))",
                    ""
            )));
            */
            if (item.getType() == Material.PLAYER_HEAD) {
                SkullMeta skullMeta = (SkullMeta)meta;
                skullMeta.setOwnerProfile(Bukkit.getOfflinePlayer(island.getLeader().getUuid()).getPlayerProfile());
                item.setItemMeta(skullMeta);
            } else {
                item.setItemMeta(meta);
            }
            inventory.setItem(slot, item);
            this.slotCache.add(new Pair<>(slot, island));
            //slots.add(new Pair<>(slot, clan));
            slot--;
        }

        for (GuiItem guiItem : ConfigMenu.TOP_ISLANDS_MENU.getMenuSetting().getItems()) {
            if (guiItem.getId() == 0) continue;
            setItem(guiItem.getItem(), guiItem.getSlot());
        }
    }

    @Override
    public void onCloseEvent(){
        this.slotCache.clear();
    }

    @Override
    public void onClickEvent(ClickEvent e) {

        final Profile profile = e.getProfile();

        for (Pair<Integer, Island> pair : this.slotCache) {

            if (e.getSlot() != pair.getX()) continue;

            final Island island = pair.getY();

            if (island == null) continue;

            Skyblock.get().getGuiHandler().openGui(profile, new IslandInfoGui(island));
            break;
        }

        for (GuiItem guiItem : ConfigMenu.TOP_ISLANDS_MENU.getMenuSetting().getItems()) {

            if (e.getSlot() != guiItem.getSlot()) continue;

            if (guiItem.hasCmd())
                profile.getPlayer().performCommand(guiItem.getCommand());
            break;
        }
    }
}
