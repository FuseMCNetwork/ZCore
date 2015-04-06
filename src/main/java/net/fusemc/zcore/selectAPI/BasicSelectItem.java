package net.fusemc.zcore.selectAPI;

import net.fusemc.zcore.util.NamedItem;

/**
 * Copyright by michidk
 * Created: 21.09.2014.
 */
public class BasicSelectItem implements SelectItem {

    private NamedItem itemStack;

    public BasicSelectItem(NamedItem itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public NamedItem getItemStack() {
        return itemStack;
    }
}
