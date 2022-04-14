package de.floxyii.challengeplugin.challenge.challenges;

public interface Challenge {

    String getName();
    String getPrefix();
    String getDescription();

    void activateChallenge();
    void addListeners();

    void stopChallenge();
    void resumeChallenge();

    String getDeathMessage();
}
