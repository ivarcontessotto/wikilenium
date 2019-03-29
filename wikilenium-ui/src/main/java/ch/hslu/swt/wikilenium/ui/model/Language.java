package ch.hslu.swt.wikilenium.ui.model;

public enum Language {
    GERMAN("Deutsch", "de"), ENGLISH("English", "en"), FRENCH("Français", "fr"), ITALIAN("Italiano", "it");
    private String name;
    private String id;

    private Language(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
