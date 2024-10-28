package net.justonedev.kit;

import net.justonedev.kit.json.JSONHandler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Main {

    private static final String APP_NAME = "Timesheet Generator";
    private static final String TITLE = "%s: %s";

    private static File editorFile;

    private static JFrame frame;
    private static DefaultListModel<TimesheetEntry> listModel;
    private static int selectedItemIndex = -1;

    private GlobalSettingsBar globalSettingsBar;
    private ActionBar buttonActionBar;

    public Main() {
        initialize();
    }

    private void initialize() {
        // Main Frame
        frame = new JFrame();
        setTitle(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
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
        globalSettingsBar.setFont(globalSettingsBar.getFont().deriveFont(14f));
        frame.add(globalSettingsBar, BorderLayout.NORTH);

        // Row of Buttons with '+'
        buttonActionBar = new ActionBar();
        globalSettingsBar.setFont(globalSettingsBar.getFont().deriveFont(14f));
        frame.add(buttonActionBar, BorderLayout.WEST);

        // Main Content Area with Vertical List
        listModel = new DefaultListModel<>();
        JList<TimesheetEntry> itemList = new JList<>(listModel);
        itemList.setBorder(new EmptyBorder(10, 10, 10, 10));
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemList.setFont(itemList.getFont().deriveFont(16f));

        // Table header setup
        JLabel tableHeader = new JLabel();
        tableHeader.setText(String.format(TimesheetEntry.TIMESHEET_FORMAT_HEADER,
                "Activity",
                "Day",
                "Start Time",
                "End Time",
                "Break Time",
                "Vacation",
                "Total Time Worked"));
        tableHeader.setFont(tableHeader.getFont().deriveFont(16f));
        tableHeader.setBorder(new EmptyBorder(5, 10, 5, 10));

        // Double-click to Edit Entry
        itemList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int index = itemList.locationToIndex(e.getPoint());
                selectedItemIndex = index < 0 ? -1 : index;
                if (e.getClickCount() == 2) {
                    if (index >= 0) {
                        DialogHelper.showEntryDialog("Edit Entry", listModel.getElementAt(index));
                        listModel.removeElementAt(index);
                    }
                }
            }
        });
        /* For the remove button
        itemList.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                selectedItemIndex = previousSelectedItemIndex;
            }
            public void focusLost(FocusEvent e) {
                previousSelectedItemIndex = selectedItemIndex;
                selectedItemIndex = -1;
            }
        });*/
        System.out.printf("Frame height: %d, bar height: %d, settings height: %d, bar Y: %d, settings Y: %d%n", frame.getHeight(), buttonActionBar.getHeight(), globalSettingsBar.getHeight(), buttonActionBar.getLocation().y, globalSettingsBar.getLocation().y);


        JScrollPane listScrollPane = new JScrollPane(itemList);
        listScrollPane.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight() - 190));

        // Panel to hold the header and list together
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.add(tableHeader, BorderLayout.NORTH);
        listPanel.add(listScrollPane, BorderLayout.CENTER);

        frame.add(listPanel, BorderLayout.SOUTH);

        // Action Listeners
        //addButton.addActionListener(e -> showEntryDialog("Add Entry", "", -1));

        fileOptionNew.addActionListener(e -> showSimpleDialog("File Option 1"));
        fileOptionOpen.addActionListener(e -> showSimpleDialog("File Option 2"));
        fileOptionGlobalSettings.addActionListener(e -> GlobalSettingsDialog.showGlobalSettingsDialog());

        // Show Frame
        frame.setVisible(true);
    }

    public static void setEditorFilepath(String editorFilepath) {
        File file = new File(editorFilepath);
        if (!file.exists()) return;
        String name = file.getName();
        if (!name.endsWith(".json")) return;
        setTitle("%s/%s".formatted(file.getParentFile().getName(), file.getName()));
    }

    private static void setTitle(String title) {
        if (title == null || title.isBlank()) {
            frame.setTitle(APP_NAME);
            return;
        }
        frame.setTitle(TITLE.formatted(APP_NAME, title));
    }

    public static void addEntry(TimesheetEntry entry) {
        for (int i = 0; i < listModel.getSize(); i++) {
            if (listModel.getElementAt(i).isLaterThan(entry)) {
                listModel.insertElementAt(entry, i);
                return;
            }
        }
        // Add to end of list
        listModel.addElement(entry);
    }

    public static void removeSelectedListEntry() {
        if (selectedItemIndex < 0) return;

        if (!showOKCancelDialog("Delete Entry?", "Delete Entry: %s?".formatted(listModel.getElementAt(selectedItemIndex).toShortString()))) return;

        listModel.removeElementAt(selectedItemIndex);
        selectedItemIndex = -1;
    }

    public static void editSelectedListEntry() {
        if (selectedItemIndex < 0) return;
        DialogHelper.showEntryDialog("Edit Entry", listModel.getElementAt(selectedItemIndex));
        listModel.removeElementAt(selectedItemIndex);
        selectedItemIndex = -1;
    }

    private void showSimpleDialog(String message) {
        JOptionPane.showMessageDialog(frame, message, "Dialog", JOptionPane.INFORMATION_MESSAGE);
    }

    private static boolean showOKCancelDialog(String title, String message) {
        // Result:
        // OK: 0
        // Cancel: 2
        // Close via X or Esc: -1
        return 0 == JOptionPane.showConfirmDialog(frame, message, title, JOptionPane.OK_CANCEL_OPTION);
    }

    public static void main(String[] args) {
        JSONHandler.initialize();
        // Ensure the application uses the system look and feel
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new Main();

                // Debug:

                addEntry(new TimesheetEntry("Tut Vorbereitung", 24, 12, 0, 18, 0, 1, 0, false));
                addEntry(new TimesheetEntry("Folien machen    ", 23, 11, 0, 15, 0, 0, 30, false));
                setEditorFilepath("C:\\Users\\Benni\\Downloads\\month.json");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
