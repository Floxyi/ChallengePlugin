package de.floxyii.challengeplugin.challenge.utils;

import org.bukkit.Material;

public interface Challenge {

    String getName();

    String getPrefix();

    String getDescription();

    Material getDisplayItem();

    void activateChallenge();

    boolean isActive();

    void registerListener();

    void stopChallenge();

    void resumeChallenge();

    String getDeathMessage();

    void saveContents(String path);

    void loadContents(String path);
}
