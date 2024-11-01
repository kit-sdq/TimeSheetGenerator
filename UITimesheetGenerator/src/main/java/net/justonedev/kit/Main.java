/*
 * Licensed under the MIT License (MIT)
 * Copyright (c) 2024 Benjamin Claus
 */
package net.justonedev.kit;

import net.justonedev.kit.json.JSONHandler;
import net.justonedev.kit.json.Month;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Main {

    private static final String APP_NAME = "Timesheet Generator";
    private static final String TITLE = "%s: %s";

    private static File currentOpenFile;
    public static boolean hasUnsavedChanges = false;

    private static JFrame frame;
    private static DefaultListModel<TimesheetEntry> listModel;
    private static int selectedItemIndex = -1;

    private static MonthlySettingsBar monthSettingsBar;
    private static ActionBar buttonActionBar;

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

        monthSettingsBar = new MonthlySettingsBar();
        monthSettingsBar.setFont(monthSettingsBar.getFont().deriveFont(14f));
        frame.add(monthSettingsBar, BorderLayout.NORTH);

        // Row of Buttons with '+'
        buttonActionBar = new ActionBar();
        monthSettingsBar.setFont(monthSettingsBar.getFont().deriveFont(14f));
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
                selectedItemIndex = index;
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
        System.out.printf("Frame height: %d, bar height: %d, settings height: %d, bar Y: %d, settings Y: %d%n", frame.getHeight(), buttonActionBar.getHeight(), monthSettingsBar.getHeight(), buttonActionBar.getLocation().y, monthSettingsBar.getLocation().y);


        JScrollPane listScrollPane = new JScrollPane(itemList);
        listScrollPane.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight() - 190));

        // Panel to hold the header and list together
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.add(tableHeader, BorderLayout.NORTH);
        listPanel.add(listScrollPane, BorderLayout.CENTER);

        frame.add(listPanel, BorderLayout.SOUTH);

        // Action Listeners
        //addButton.addActionListener(e -> showEntryDialog("Add Entry", "", -1));

        fileOptionNew.addActionListener(e -> clearWorkspace());
        fileOptionOpen.addActionListener(e -> openFile());
        fileOptionGlobalSettings.addActionListener(e -> GlobalSettingsDialog.showGlobalSettingsDialog());
        fileOptionSave.addActionListener(e -> saveFile(currentOpenFile));
        fileOptionSaveAs.addActionListener(e -> saveFileAs());

        // Add Strg + S to save
        // Define the KeyStroke for Ctrl+S or Command+S on Mac
        KeyStroke saveKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());

        // Bind the KeyStroke to the save action in the InputMap and ActionMap
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(saveKeyStroke, "saveAction");
        frame.getRootPane().getActionMap().put("saveAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile(currentOpenFile);
            }
        });

        // Show Frame
        frame.setVisible(true);

        frame.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        closeCurrentOpenFile();
                    }
                }
        );
    }

    public static File getCurrentOpenFile() {
        return currentOpenFile;
    }

    public static boolean hasUnsavedChanges() {
        return hasUnsavedChanges;
    }

    public static File generateTempMonthFile() {
        return JSONHandler.generateTemporaryJSONFile(monthSettingsBar, listModel);
    }

    public static Time getPredTime() {
        return monthSettingsBar.getPredTime();
    }

    public static int getWidth() {
        return frame.getWidth();
    }

    public static void clearWorkspace() {
        closeCurrentOpenFile();
        // Delete all content
        monthSettingsBar.reset();
        listModel.clear();
        setHasUnsavedChanges(false);
    }

    public static void closeCurrentOpenFile() {
        if (!hasUnsavedChanges) return;
        // Prompt to save
        SaveOnClosePrompt.showDialog();
    }

    public static void saveFile(File newSaveFile) {
        if (newSaveFile == null) newSaveFile = currentOpenFile;
        if (newSaveFile == null) {
            saveFileAs();
            return;
        }
        saveFileCommon(newSaveFile);
    }

    public static void saveFileAs() {
        File newSaveFile = FileChooser.chooseCreateJSONFile("Save as...");
        if (newSaveFile == null) return;
        saveFileCommon(newSaveFile);
        setEditorFile(newSaveFile);
    }

    public static void saveFileCommon(File newSaveFile) {
        JSONHandler.saveMonth(newSaveFile, monthSettingsBar, listModel);

        currentOpenFile = newSaveFile;
        setHasUnsavedChanges(false);
    }

    public static void openFile() {
        File openFile = FileChooser.chooseFile("Open a file");
        if (openFile == null) return;
        if (!setEditorFile(openFile)) return;
        clearWorkspace();

        // Open the file

        JSONHandler.loadMonth(openFile);
        setHasUnsavedChanges(false);
    }

    public static void importMonthBarSettings(Month month) {
        monthSettingsBar.importMonthSettings(month);
    }

    public static void setHasUnsavedChanges(boolean hasUnsavedChanges) {
        Main.hasUnsavedChanges = hasUnsavedChanges;
        updateTitle();
    }

    public static boolean setEditorFile(File file) {
        if (!file.exists()) return false;
        String name = file.getName();
        if (!name.endsWith(".json")) return false;
        currentOpenFile = file;
        updateTitle();
        return true;
    }

    private static void setTitle(String title) {
        if (title == null || title.isBlank()) {
            frame.setTitle(APP_NAME);
            return;
        }
        frame.setTitle(TITLE.formatted(APP_NAME, title));
    }

    private static void updateTitle() {
        String filename = currentOpenFile == null ? "" : "%s/%s".formatted(currentOpenFile.getParentFile().getName(), currentOpenFile.getName());
        if (hasUnsavedChanges) filename += '*';
        setTitle(filename);
    }

    public static void addEntry(TimesheetEntry entry) {
        for (int i = 0; i < listModel.getSize(); i++) {
            if (listModel.getElementAt(i).isLaterThan(entry)) {
                listModel.insertElementAt(entry, i);
                updateTotalTimeWorkedUI();
                return;
            }
        }
        // Add to end of list
        listModel.addElement(entry);
        updateTotalTimeWorkedUI();
    }

    public static void duplicateSelectedListEntry() {
        if (selectedItemIndex < 0) return;
        setHasUnsavedChanges(true);

        addEntry(listModel.getElementAt(selectedItemIndex).clone());
        selectedItemIndex++;    // 1 more, there is the duplicate
        editSelectedListEntry();
    }

    public static void editSelectedListEntry() {
        if (selectedItemIndex < 0) return;
        DialogHelper.showEntryDialog("Edit Entry", listModel.getElementAt(selectedItemIndex));
        listModel.removeElementAt(selectedItemIndex);
        selectedItemIndex = -1;
        updateTotalTimeWorkedUI();
    }

    public static void removeSelectedListEntry() {
        if (selectedItemIndex < 0) return;

        if (!showOKCancelDialog("Delete Entry?", "Delete Entry: %s?".formatted(listModel.getElementAt(selectedItemIndex).toShortString()))) return;

        setHasUnsavedChanges(true);
        listModel.removeElementAt(selectedItemIndex);
        selectedItemIndex = -1;
    }

    public static void updateTotalTimeWorkedUI() {
        Time worked = calculateTotalTimeWorked();
        Time succTime = buttonActionBar.updateHours(worked);
        monthSettingsBar.setSuccTime(succTime.toString());
    }

    private static Time calculateTotalTimeWorked() {
        Time workedTime = new Time();
        for (int i = 0; i < listModel.getSize(); i++) {
            TimesheetEntry entry = listModel.getElementAt(i);
            workedTime.addTime(entry.getWorkedTime());
        }
        return workedTime;
    }

    public static void showSimpleDialog(String message) {
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
                setHasUnsavedChanges(false);

                // Debug:

                //addEntry(new TimesheetEntry("Tut Vorbereitung", 24, 12, 0, 18, 0, 1, 0, false));
                //addEntry(new TimesheetEntry("Folien machen    ", 23, 11, 0, 15, 0, 0, 30, false));
                //setEditorFile(new File("C:\\Users\\Benni\\Downloads\\month.json"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
