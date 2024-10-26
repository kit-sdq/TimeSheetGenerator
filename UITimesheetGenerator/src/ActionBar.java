import javax.swing.*;
import java.awt.*;

public class ActionBar extends JPanel {

    private final JButton addButton;
    private final JButton editButton;
    private final JButton removeButton;
    private final JButton printButton;

    public ActionBar() {
        addButton = new JButton("+");
        addButton.setPreferredSize(new Dimension(50, 50));
        this.add(addButton);

        editButton = new JButton("Edit");
        editButton.setPreferredSize(new Dimension(50, 50));
        this.add(editButton);

        removeButton = new JButton("-");
        removeButton.setPreferredSize(new Dimension(50, 50));
        this.add(removeButton);

        printButton = new JButton("Print");
        printButton.setPreferredSize(new Dimension(70, 50));
        this.add(printButton);

        addButton.addActionListener(e -> DialogHelper.showEntryDialog("Add Entry"));
        removeButton.addActionListener((l) -> {
            Main.removeSelectedListEntry();
        });
        editButton.addActionListener((l) -> {
            Main.editSelectedListEntry();
        });
    }

}
