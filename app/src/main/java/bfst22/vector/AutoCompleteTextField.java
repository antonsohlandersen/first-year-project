package bfst22.vector;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.Serializable;
import java.util.*;

/**
 * This class is a TextField which implements an "autocomplete" functionality,
 * based on a supplied list of entries.
 * 
 * @author Caleb Brinkman
 */
public class AutoCompleteTextField extends TextField implements Serializable {
    public static final long serialVersionUID = 9082426;
    /** The existing autocomplete entries. */
    private final SortedSet<String> entries;
    /** The popup used to select an entry. */
    private ContextMenu entriesPopup;

    /** Construct a new AutoCompleteTextField. */
    public AutoCompleteTextField() {
        super();
        entries = new TreeSet<>();
        entriesPopup = new ContextMenu();

    }

    /**
     * Get the existing set of autocomplete entries.
     * 
     * @return The existing autocomplete entries.
     */
    public SortedSet<String> getEntries() {
        return entries;
    }

    /**
     * Populate the entry set with the given search results. Display is limited to
     * 10 entries, for performance.
     * 
     * @param searchResult The set of matching strings.
     */
    private void populatePopup(List<String> searchResult, Controller controller) {
        List<CustomMenuItem> menuItems = new LinkedList<>();
        // If you'd like more entries, modify this line.
        int maxEntries = 10;
        int count = Math.min(searchResult.size(), maxEntries);
        for (int i = 0; i < count; i++) {
            final String result = searchResult.get(i);
            Label entryLabel = new Label(result);
            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            item.setOnAction(actionEvent -> {
                setText(result);
                controller.searchAddress();
                entriesPopup.hide();
            });
            menuItems.add(item);
        }
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);

    }

    public void addSuggestions(ArrayList<String> streets, Controller controller) {
        getEntries().clear();
        getEntries().addAll(streets);
        LinkedList<String> searchResult = new LinkedList<>();
        searchResult.addAll(entries);
        if (entries.size() > 0) {
            populatePopup(searchResult, controller);
            if (!entriesPopup.isShowing()) {
                entriesPopup.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0);
            }
        } else {
            entriesPopup.hide();
        }
    }

    public SortedSet<String> getSuggestions(){
        return entries;
    }
}
