package de.floxyii.challengeplugin.challenge.modules;

public interface Module {

    String getName();
    String getDescription();
    boolean setActive(boolean bool);
    boolean isActive();
}
