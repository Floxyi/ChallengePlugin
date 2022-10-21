package de.floxyii.challengeplugin.challenge.challenges;

public interface Challenge {

    String getName();
    String getPrefix();
    String getDescription();

    void activateChallenge();

    boolean isActive();
    void registerListener();

    void stopChallenge();
    void resumeChallenge();

    String getDeathMessage();

    void saveContents(String path);

    void loadContents(String path);
}
