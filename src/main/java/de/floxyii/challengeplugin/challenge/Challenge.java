package de.floxyii.challengeplugin.challenge;

public interface Challenge {

    String getName();

    void onActivate();
    void addListeners();

    void stopChallenge();
    void resumeChallenge();
}
