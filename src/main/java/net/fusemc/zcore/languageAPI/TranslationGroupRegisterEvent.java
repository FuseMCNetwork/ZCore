package net.fusemc.zcore.languageAPI;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class TranslationGroupRegisterEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Collection<String> groups = new HashSet<>();

    public void addGroup(String group) {
        groups.add(group);
    }

    public Collection<String> getGroups() {
        return Collections.unmodifiableCollection(groups);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
