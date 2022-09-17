package zatribune.spring.pps.data.entities;

import java.util.HashMap;
import java.util.Map;

public enum PicCategory {
    LIVING_THING("Living Thing","Anything that's alive e.g animals, trees and insects."),
    MACHINE("Machine","Man-made inventions and tools."),
    NATURE("Nature","Mother nature.");

    private final String label;
    private final String description;

    PicCategory(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public static final Map<String, PicCategory> LABELS = new HashMap<>();

    static {
        for (PicCategory e: values()) {
            LABELS.put(e.label, e);
        }
    }

    public static PicCategory valueOfLabel(String label) {
        return LABELS.get(label);
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }
}
