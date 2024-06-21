package de.floxyii.challengeplugin.challenge.utils;

import org.bukkit.Material;

public interface Module {

    String getName();

    String getDescription();

    Material getDisplayItem();

    boolean setActive(boolean bool);

    boolean isActive();
}
