package net.syphlex.skyblock.manager.cooldown.type;

import lombok.Getter;
import net.syphlex.skyblock.manager.profile.Profile;
import net.syphlex.skyblock.util.config.Messages;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public abstract class Cooldown {

    protected final Map<UUID, Long> cache = new ConcurrentHashMap<>();
    protected final long duration;

    public Cooldown(final long duration){
        this.duration = duration;
    }

    public void start(final UUID uuid) {
        this.cache.put(uuid, System.currentTimeMillis());
    }

    public void stop(final UUID uuid) {
        this.cache.remove(uuid);
    }

    public int getTimeLeft(UUID uuid){
        if (!isUnderCooldown(uuid)) return -1;
        return (int)((System.currentTimeMillis() - this.cache.get(uuid)) / 1000L);
    }

    public boolean isUnderCooldown(final UUID uuid){

        if (this.cache.containsKey(uuid))
            if (isExpired(uuid)) return false;

        return this.cache.containsKey(uuid);
    }

    public boolean isExpired(final UUID uuid) {
        if (!this.cache.containsKey(uuid)) return true;
        return ((System.currentTimeMillis() - duration) / 1000L) <= 0L;
    }

    public void sendUnderCooldown(final Profile profile){
        final String timeLeft = String.valueOf(getTimeLeft(profile.getPlayer().getUniqueId()));
        profile.sendMessage(Messages.UNDER_COOLDOWN.get()
                .replace("%time_left%", timeLeft));
    }
}
