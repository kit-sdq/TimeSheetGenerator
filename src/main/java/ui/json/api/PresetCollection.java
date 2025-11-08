package ui.json.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PresetCollection {
    @Getter
    @JsonProperty("version")
    private String newestVersion;
    @JsonProperty("presets")
    private List<Preset> presets;

    /**
     * Creates a new {@link PresetCollection}. Values are initialized as follows:<br/>
     * {@link PresetCollection#newestVersion}: Empty String<br/>
     * {@link PresetCollection#presets}: Empty List
     */
    public PresetCollection() {
        newestVersion = "";
        presets = new ArrayList<>();
    }

    /**
     * Creates a new preset collection. Notably, the presets list is saved as reference and not
     * copied. This is on purpose to minimize overhead, this constructor should in turn remain
     * private and only used in the {@link PresetCollection#merge(PresetCollection, PresetCollection)} method.
     * @param newestVersion The newest version.
     * @param presetsRef All presets. Will be saved as reference.
     */
    private PresetCollection(String newestVersion, List<Preset> presetsRef) {
        this.newestVersion = newestVersion;
        this.presets = presetsRef;
    }

    /**
     * Gets a copy of the presets list.
     * @return copy of the presets.
     */
    public List<Preset> getPresets() {
        return new ArrayList<>(presets);
    }

    /**
     * Merges two {@link PresetCollection}, intended for merging the local collection with the online collection.
     * Presets are uniquely identified by their {@link Preset#getPresetId()}; so if two presets collide, the
     * preset from the API (second parameter) will be preferred in the merge.<br/>
     * This way, we cleanly merge the preset collections while recognizing that the second collection will be
     * more recent.
     * <p>
     *     The {@link PresetCollection#newestVersion} will be set to the version of the second (API) collection.
     * </p>
     * @param fromFile the first preset collection, in the intended use case should be the one loaded locally.
     * @param fromAPI the second preset collection, in the intended use case should be the one loaded from the internet.
     * @return The merged preset collection, with colliding Ids having their data from the fromAPI (second) collection.
     */
    public static PresetCollection merge(PresetCollection fromFile, PresetCollection fromAPI) {
        Map<String, Preset> presetsPerId = new HashMap<>();
        // Order matters, fromAPI should override existing
        fromFile.presets.forEach(preset -> presetsPerId.put(preset.getPresetId(), preset));
        fromAPI.presets.forEach(preset -> presetsPerId.put(preset.getPresetId(), preset));
        // Still expected O(n) time complexity.
        return new PresetCollection(fromAPI.getNewestVersion(), presetsPerId.values().stream().toList());
    }
}
