import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;

public class Main {

    private JFrame frame;
    private DefaultListModel<String> listModel;

    private GlobalSettingsBar globalSettingsBar;

    public Main() {
        initialize();
    }

    private void initialize() {
        // Main Frame
        frame = new JFrame("Custom Swing Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem fileOptionNew = new JMenuItem("New...");
        JMenuItem fileOptionOpen = new JMenuItem("Open");
        JMenuItem fileOptionGlobalSettings = new JMenuItem("Edit Global Settings");
        JMenuItem fileOptionSave = new JMenuItem("Save");
        JMenuItem fileOptionSaveAs = new JMenuItem("Save as...");
        fileMenu.add(fileOptionNew);
        fileMenu.add(fileOptionOpen);
        fileMenu.add(fileOptionGlobalSettings);
        fileMenu.add(fileOptionSave);
        fileMenu.add(fileOptionSaveAs);
        menuBar.add(fileMenu);

        /* Edit Menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem editOption1 = new JMenuItem("Option 1");
        JMenuItem editOption2 = new JMenuItem("Option 2");
        editMenu.add(editOption1);
        editMenu.add(editOption2);
        menuBar.add(editMenu);
        */

        frame.setJMenuBar(menuBar);

        globalSettingsBar = new GlobalSettingsBar();

        frame.add(globalSettingsBar, BorderLayout.NORTH);

        // Row of Buttons with '+'
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("+");
        buttonPanel.add(addButton);
        frame.add(buttonPanel, BorderLayout.CENTER);

        // Main Content Area with Vertical List
        listModel = new DefaultListModel<>();
        JList<String> itemList = new JList<>(listModel);
        itemList.setBorder(new EmptyBorder(10, 10, 10, 10));
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Double-click to Edit Entry
        itemList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = itemList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        showEntryDialog("Edit Entry", listModel.getElementAt(index), index);
                    }
                }
            }
        });

        JScrollPane listScrollPane = new JScrollPane(itemList);
        frame.add(listScrollPane, BorderLayout.SOUTH);

        // Action Listeners
        addButton.addActionListener(e -> showEntryDialog("Add Entry", "", -1));

        fileOptionNew.addActionListener(e -> showSimpleDialog("File Option 1"));
        fileOptionOpen.addActionListener(e -> showSimpleDialog("File Option 2"));

        // Show Frame
        frame.setVisible(true);
    }

    private void showEntryDialog(String title, String existingText, int index) {
        JDialog dialog = new JDialog(frame, title, true);
        dialog.setSize(300, 250);
        dialog.setLayout(new GridLayout(6, 2, 5, 5));
        dialog.setLocationRelativeTo(frame);

        // Day Text Field
        dialog.add(new JLabel("Day:"));
        JTextField dayField = new JTextField(existingText);
        dialog.add(dayField);

        // Start Time
        dialog.add(new JLabel("Start Time:"));
        JTextField startTimeField = new JTextField();
        dialog.add(startTimeField);

        // End Time
        dialog.add(new JLabel("End Time:"));
        JTextField endTimeField = new JTextField();
        dialog.add(endTimeField);

        // Tick Box
        dialog.add(new JLabel("Option:"));
        JCheckBox optionCheckBox = new JCheckBox();
        dialog.add(optionCheckBox);

        // Save and Discard Buttons
        JButton saveButton = new JButton("Save");
        JButton discardButton = new JButton("Discard");
        dialog.add(saveButton);
        dialog.add(discardButton);

        // Action Listeners for Buttons
        saveButton.addActionListener(e -> {
            String entry = dayField.getText();
            if (!entry.isEmpty()) {
                if (index >= 0) {
                    listModel.set(index, entry);
                } else {
                    listModel.addElement(entry);
                }
            }
            dialog.dispose();
        });

        discardButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void showSimpleDialog(String message) {
        JOptionPane.showMessageDialog(frame, message, "Dialog", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        // Ensure the application uses the system look and feel
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new Main();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
