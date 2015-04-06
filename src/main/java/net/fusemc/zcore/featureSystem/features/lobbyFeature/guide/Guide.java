package net.fusemc.zcore.featureSystem.features.lobbyFeature.guide;

/**
 * Copyright by michidk
 * Created: 21.08.2014.
 */
public class Guide {

    private String title;
    private String objective;
    private String controls;
    private String other;
    private String hints;

    public Guide() {

    }

    public Guide(String title, String objective, String controls, String other, String hints) {
        this.title = title;
        this.objective = objective;
        this.controls = controls;
        this.other = other;
        this.hints = hints;
    }

    public String getTitle() {
        return title;
    }

    public String getObjective() {
        return objective;
    }

    public String getControls() {
        return controls;
    }

    public String getOther() {
        return other;
    }

    public String getHints(){
        return hints;
    }
}
