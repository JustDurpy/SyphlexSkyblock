package net.syphlex.skyblock.manager.customenchant;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import net.syphlex.skyblock.Skyblock;
import net.syphlex.skyblock.database.flat.CustomEnchantsFile;
import net.syphlex.skyblock.util.data.Pair;
import net.syphlex.skyblock.util.utilities.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

@Getter
public class EnchantHandler {

    private final ArrayList<CustomEnchant> enchants = new ArrayList<>();
    private final ArrayList<Category> categories = new ArrayList<>();
    private CustomEnchantsFile customEnchantsFile;

    public final static String RUNE_ITEM_NBT_KEY = "runeitem";
    public final static String SHARD_ITEM_NBT_KEY = "sharditem";
    public final static String WHITE_SCROLL_NBT_KEY = "whitescroll";
    public final static String ANGEL_DUST_NBT_KEY = "angeldust";

    public void onEnable() {
        Skyblock.get().getThreadHandler().fire(() -> {
            this.customEnchantsFile = new CustomEnchantsFile();
            this.customEnchantsFile.read();
        });
    }

    public void purchaseRune(Player player){
        float cost = Skyblock.get().getSettingsFile().getCeConfigData().runeCost;
        float exp = player.getExp();
        if (exp >= cost) {
            player.setExp(player.getExp() - cost);
            player.sendMessage(StringUtil.CC("&aYou have purchased an enchanted rune."));
            giveRune(player);
        } else {
            player.sendMessage(StringUtil.CC("&cYou do not have enough EXP to purchase an enchanted rune."));
        }
    }

    public boolean applyAngelDust(Player player, ItemStack dust, ItemStack item){

        if (!isAngelDust(dust))
            return false;

        if (!isShard(item))
            return false;

        int success = getShardSuccessRate(item) + getAngelSuccess(dust);

        if (success - getAngelSuccess(dust) > 100) {
            player.sendMessage(StringUtil.CC("&cThis item already has a 100% success rate."));
            return false;
        }

        NBTItem nbt = new NBTItem(item);
        nbt.setInteger(SHARD_ITEM_NBT_KEY + "successrate",
                success);
        nbt.applyNBT(item);

        int destroy = getShardDestroyRate(item);

        ItemMeta meta = item.getItemMeta();
        if (meta == null || meta.getLore() == null)
            return false;
        if (Skyblock.get().getSettingsFile().getCeConfigData().shardItem.getItemMeta() == null
                || Skyblock.get().getSettingsFile().getCeConfigData().shardItem.getItemMeta().getLore() == null)
            return false;
        List<String> lore = Skyblock.get().getSettingsFile().getCeConfigData().shardItem.getItemMeta().getLore();
        lore.replaceAll(s -> s
                .replace("%success%", String.valueOf(success))
                .replace("%destroy%", String.valueOf(destroy)));
        meta.setLore(lore);
        item.setItemMeta(meta);

        return true;
    }

    public void giveAngelDust(Player player) {
        ItemStack item = Skyblock.get().getSettingsFile().getCeConfigData().angelDust.clone();
        ItemMeta meta = item.getItemMeta();
        if (meta == null || meta.getLore() == null)
            return;
        List<String> lore = meta.getLore();
        int max = Skyblock.get().getSettingsFile().getCeConfigData().angelDustMax;
        int min = Skyblock.get().getSettingsFile().getCeConfigData().angelDustMin;
        int success = (int) (Math.random() * (max - min) + min);
        meta.setDisplayName(meta.getDisplayName()
                .replace("%success%",
                        String.valueOf(success)));
        lore.replaceAll(s -> s.replace("%success%",
                String.valueOf(success)));
        meta.setLore(lore);
        item.setItemMeta(meta);
        NBTItem nbt = new NBTItem(item);
        nbt.setInteger(ANGEL_DUST_NBT_KEY, success);
        nbt.applyNBT(item);
        player.getInventory().addItem(item);
        //player.updateInventory();
    }

    public int getAngelSuccess(ItemStack item){
        NBTItem nbt = new NBTItem(item);
        return nbt.getInteger(ANGEL_DUST_NBT_KEY);
    }

    public boolean isAngelDust(ItemStack item){
        NBTItem nbt = new NBTItem(item);
        return nbt.getBoolean(ANGEL_DUST_NBT_KEY) != null
                && nbt.getInteger(ANGEL_DUST_NBT_KEY) != 0;
    }

    public void removeWhiteScroll(Player player, ItemStack item){

        if (!hasWhiteScroll(item))
            return;

        NBTItem nbt = new NBTItem(item);
        nbt.clearCustomNBT();
        nbt.applyNBT(item);

        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return;
        List<String> lore = meta.getLore() == null
                ? new ArrayList<>() : meta.getLore();
        lore.remove(lore.size() - 1);
        meta.setLore(lore);
        item.setItemMeta(meta);

        player.sendMessage(StringUtil.CC("item is no longer protected by white scroll."));
    }

    public void applyWhiteScroll(Player player, ItemStack item){

        // todo
        //if (appliable()) {
        //}

        if (hasWhiteScroll(item)) {
            player.sendMessage(StringUtil.CC("&cThis item already has a white scroll"));
            return;
        }

        NBTItem nbt = new NBTItem(item);
        nbt.setString(WHITE_SCROLL_NBT_KEY, "onitem");
        nbt.applyNBT(item);

        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return;
        List<String> lore = meta.getLore() == null
                ? new ArrayList<>() : meta.getLore();
        lore.add(StringUtil.CC("PROTECTED"));
        meta.setLore(lore);
        item.setItemMeta(meta);

        player.sendMessage(StringUtil.CC("&aSuccessfully applied a whitescroll to this item."));
    }

    public boolean hasWhiteScroll(ItemStack item) {
        NBTItem nbt = new NBTItem(item);
        return nbt.getBoolean(WHITE_SCROLL_NBT_KEY) != null
                && nbt.getString(WHITE_SCROLL_NBT_KEY)
                .equalsIgnoreCase("onitem");
    }

    public boolean isWhiteScroll(ItemStack item){
        NBTItem nbt = new NBTItem(item);
        return nbt.getBoolean(WHITE_SCROLL_NBT_KEY) != null
                && nbt.getString(WHITE_SCROLL_NBT_KEY)
                .equalsIgnoreCase("item");
    }

    public void giveWhiteScroll(Player player) {
        ItemStack item = Skyblock.get().getSettingsFile().getCeConfigData().whiteScrollItem.clone();
        NBTItem nbt = new NBTItem(item);
        nbt.setString(WHITE_SCROLL_NBT_KEY, "item");
        nbt.applyNBT(item);
        player.getInventory().addItem(item);
        //player.updateInventory();
    }

    public List<CustomEnchant> getAllEnchantsInCategory(Category category){
        List<CustomEnchant> enchantList = new ArrayList<>();
        for (CustomEnchant e : this.enchants) {
            if (e.getCategory() == category)
                enchantList.add(e);
        }
        return enchantList;
    }

    public int getShardSuccessRate(ItemStack item){
        if (!isShard(item))
            return -1;
        NBTItem nbt = new NBTItem(item);
        return nbt.getInteger(SHARD_ITEM_NBT_KEY + "successrate");
    }

    public int getShardDestroyRate(ItemStack item){
        if (!isShard(item))
            return -1;
        return 100 - getShardSuccessRate(item);
    }

    public Pair<CustomEnchant, Integer> getShardInfo(ItemStack item){
        if (!isShard(item))
            return null;
        NBTItem nbt = new NBTItem(item);
        String[] value = nbt.getString(SHARD_ITEM_NBT_KEY).split(":");
        String nbtName = value[0];
        String nbtLevel = value[1];
        CustomEnchant enchant = getEnchantFromName(nbtName);
        return new Pair<>(enchant, Integer.parseInt(nbtLevel));
    }

    public void giveShard(Player player) {
        Category random = getRandomCategory();
        while (random == null)
            random = getRandomCategory();
        List<CustomEnchant> enchantsInCategory = getAllEnchantsInCategory(random);
        if (getAllEnchantsInCategory(random).isEmpty()) {
            Skyblock.log("ERROR: THERE IS NO ENCHANTMENTS IN THIS CATEGORY!");
            Skyblock.log("ERROR: FAILED TO GIVE SHARD!");
            return;
        }
        Collections.shuffle(enchantsInCategory);
        int r = (int) (Math.random() * enchantsInCategory.size());
        CustomEnchant randomEnchant = enchantsInCategory.get(r);
        player.getInventory().addItem(createShard(randomEnchant));
        //player.updateInventory();
    }

    private ItemStack createShard(CustomEnchant enchant){

        Category category = enchant.getCategory();

        ItemStack configItem = Skyblock.get().getSettingsFile().getCeConfigData().shardItem.clone();
        ItemMeta meta = configItem.getItemMeta();

        if (meta == null)
            return null;

        List<String> lore = meta.getLore();

        if (lore == null)
            return null;

        int randomLvl = (int) (Math.random() * enchant.getMaxLvl()) + 1;
        int success = ((int)(Math.random() * 100) + 1);

        String name = meta.getDisplayName()
                .replace("%rarity%", enchant.getCategory().getName())
                .replace("%color%", enchant.getCategory().getColor())
                .replace("%enchantment%", enchant.getName())
                .replace("%level%", StringUtil.intToRoman(randomLvl))
                .replace("%category%", enchant.getCategory().getName());

        lore.replaceAll(s -> s.replace("%rarity%", category.getName())
                .replace("%color%", category.getColor())
                .replace("%rarity%", enchant.getCategory().getName())
                .replace("%category%", category.getName())
                .replace("%success_rate%", String.valueOf(success))
                .replace("%destroy_rate%", String.valueOf((100 - success)))
                .replace("%level%", StringUtil.intToRoman(randomLvl)));

        //Glow glow = new Glow();
        //meta.addEnchant(glow, 0, true);
        meta.setLore(lore);
        meta.setDisplayName(StringUtil.CC(name));
        configItem.setItemMeta(meta);

        NBTItem nbt = new NBTItem(configItem);
        nbt.setString(SHARD_ITEM_NBT_KEY, enchant.getName() + ":" + randomLvl);
        nbt.setInteger(SHARD_ITEM_NBT_KEY + "successrate", success);
        nbt.applyNBT(configItem);

        return configItem;
    }

    public boolean isShard(ItemStack item){
        NBTItem nbt = new NBTItem(item);
        return nbt.getString(SHARD_ITEM_NBT_KEY) != null
                && !nbt.getString(SHARD_ITEM_NBT_KEY).isEmpty();
    }

    public boolean isRune(ItemStack item){
        NBTItem nbt = new NBTItem(item);
        return nbt.getBoolean(RUNE_ITEM_NBT_KEY) != null
                && nbt.getBoolean(RUNE_ITEM_NBT_KEY);
    }

    public void giveRune(Player player){

        ItemStack configItem = Skyblock.get().getSettingsFile().getCeConfigData().runeItem.clone();
        ItemMeta meta = configItem.getItemMeta();

        if (meta == null)
            return;

        //Glow glow = new Glow();
        //meta.addEnchant(glow, 0, true);
        configItem.setItemMeta(meta);
        NBTItem nbt = new NBTItem(configItem);
        nbt.setBoolean(RUNE_ITEM_NBT_KEY, true);
        nbt.applyNBT(configItem);

        player.getInventory().addItem(configItem);
        //player.updateInventory();
    }

    public boolean enchant(Player player, ItemStack shard, ItemStack item, CustomEnchant enchant) {

        if (item == null || item.getType() == Material.AIR)
            return false;

        if (!appliable(enchant.getAppliances(), item)) {
            //player.sendMessage(Messages.ENCHANT_CANT_APPLY_ITEM.get());
            return false;
        }

        double success = getShardSuccessRate(shard);
        double destroy = getShardDestroyRate(shard);

        int random = (int) (Math.random() * 100) + 1;

        double successDiff = Math.max(success, random) - Math.min(success, random);
        double destroyDiff = Math.max(destroy, random) - Math.min(destroy, random);

        ItemMeta meta = item.getItemMeta();

        if (meta == null)
            return false;

        List<String> lore = !meta.hasLore() || meta.getLore() == null ? new ArrayList<>() : meta.getLore();

        if (lore.isEmpty()) lore.add(" ");

        int nbtLvl = new NBTItem(item).getInteger(enchant.getName());
        int shardLvl = getShardInfo(shard).getY();
        int updateLvl = shardLvl; //nbtLvl + shardLvl;

        if (nbtLvl > shardLvl) {
            player.sendMessage(StringUtil.CC("&cFailed to upgrade enchant"));
            return false;
        }

        if (updateLvl > enchant.getMaxLvl()) {
            //player.sendMessage(Messages.ENCHANT_ALREADY_MAX_LVL.get());
            return false;
        }

        int index = lore.size() + 1;
        for (int i = 0; i < lore.size(); i++) {

            String line = lore.get(i);

            if (line.length() < enchant.getName().length())
                continue;

            if (line.substring(0, enchant.getName().length()).trim().equalsIgnoreCase(enchant.getName())) {
                index = i;
                break;
            }
        }

        if (destroyDiff < successDiff) {
            //player.sendMessage(Messages.ENCHANT_UNSUCCESSFUL.get());
            return true;
        }

        String add = StringUtil.CC(enchant.getCategory().getColor() + enchant.getName()
                + " " + StringUtil.intToRoman(updateLvl));

        if (itemHasEnchant(item, enchant)) {
            lore.set(index, add);
        } else {
            lore.add(add);
        }

        //Glow glow = new Glow();
        //meta.addEnchant(glow, 0, true);
        meta.setLore(StringUtil.CC(lore));
        item.setItemMeta(meta);

        NBTItem nbt = new NBTItem(item);
        nbt.setInteger(enchant.getName(), updateLvl);
        nbt.applyNBT(item);

        //player.updateInventory();
        //player.sendMessage(Messages.ENCHANT_SUCCESS.get());
        return true;
    }

    public List<Pair<CustomEnchant, Integer>> getEnchantmentsOnItem(ItemStack item){

        List<Pair<CustomEnchant, Integer>> enchants = new ArrayList<>();

        if (item == null)
            return enchants;

        NBTItem nbt = new NBTItem(item);

        for (CustomEnchant e : this.enchants) {
            if (!itemHasEnchant(item, e))
                continue;
            enchants.add(new Pair<>(e, nbt.getInteger(e.getName())));
        }

        return enchants;
    }

    public Pair<CustomEnchant, Integer> getEnchantFromItem(ItemStack item, String enchantName){

        Pair<CustomEnchant, Integer> pair = null;

        for (Pair<CustomEnchant, Integer> ce : getEnchantmentsOnItem(item)) {

            if (!ce.getX().getName().equalsIgnoreCase(enchantName))
                pair = ce;

        }

        return pair;
    }

    public boolean itemHasEnchant(ItemStack item, CustomEnchant enchant){
        if (item == null
                || item.getType() == Material.AIR
                || item.getAmount() <= 0) return false;
        NBTItem nbt = new NBTItem(item);
        return nbt.getInteger(enchant.getName()) != null
                && nbt.getInteger(enchant.getName()) > 0;
    }

    public CustomEnchant getEnchantFromName(String s){
        for (CustomEnchant e : this.enchants) {
            if (e.getName().equalsIgnoreCase(s))
                return e;
        }
        return null;
    }

    public boolean appliable(Set<Appliance> appliances, ItemStack stack){

        String name = stack.getType().name();

        if (appliances.contains(Appliance.SWORD)
                && name.contains("SWORD"))
        {
            return true;
        }

        if (appliances.contains(Appliance.AXE)
                && name.contains("AXE"))
        {
            return true;
        }

        if (appliances.contains(Appliance.PICKAXE)
                && name.contains("PICKAXE"))
        {
            return true;
        }

        if (appliances.contains(Appliance.SHOVEL)
                && name.contains("SHOVEL"))
        {
            return true;
        }

        if (appliances.contains(Appliance.ARMOR)
                && (name.contains("HELMET")
                || name.contains("CHESTPLATE")
                || name.contains("LEGGINGS")
                || name.contains("BOOTS")))
        {
            return true;
        }

        if (appliances.contains(Appliance.BOOTS)
                && name.contains("BOOTS"))
        {
            return true;
        }

        if (appliances.contains(Appliance.HELMET)
                && name.contains("HELMET"))
        {
            return true;
        }

        if (appliances.contains(Appliance.TOOLS)
                && (name.contains("AXE")
                || name.contains("PICKAXE")
                || name.contains("SHOVEL")))
        {
            return true;
        }

        return false;
    }

    public Category getCategoryFromName(String name){
        for (Category category : this.categories) {
            if (category.getName().equalsIgnoreCase(name))
                return category;
        }
        return null;
    }

    public boolean hasEnchants(ItemStack stack){
        return getEnchantmentsOnItem(stack).size() > 0;
    }

    private Category getRandomCategory() {
        List<Category> compositions = new ArrayList<>(this.categories);
        int totalPercentage = 0;
        for(Category categoryComposition : compositions) {
            totalPercentage = totalPercentage + (int)Math.round(categoryComposition.getChance());
        }

        if(totalPercentage == 0) return null;

        Random random = new Random();
        int index =  random.nextInt(totalPercentage);
        int sum = 0, i = 0;
        while (sum < index) {
            sum = sum + (int)Math.round(compositions.get(i++).getChance());
        }
        return compositions.get(Math.max(0, i - 1));
    }
}
