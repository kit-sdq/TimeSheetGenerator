/*
 * Licensed under the MIT License (MIT)
 * Copyright (c) 2024 Benjamin Claus
 */
package net.justonedev.kit;

import net.justonedev.kit.fileexplorer.FileChooserType;
import net.justonedev.kit.json.JSONHandler;
import net.justonedev.kit.json.Month;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Main {

    public static final int MAX_ENTRIES = 22;

    private static final String APP_NAME = "Timesheet Generator";
    private static final String TITLE = "%s: %s";

    private static File currentOpenFile;
    public static boolean hasUnsavedChanges = false;

    private static JFrame frame;
    private static JPanel listPanel;
    private static JList<TimesheetEntry> itemList;
    private static DefaultListModel<TimesheetEntry> listModel;

    private static MonthlySettingsBar monthSettingsBar;
    private static ActionBar buttonActionBar;

    public Main() {
        initialize();
    }

    private void initialize() {
        // Main Frame
        frame = new DragDropJFrame();
        setTitle(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // terminates when no saved changes
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

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
        itemList = new JList<>(listModel);
        itemList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

                String text = ((TimesheetEntry) value).toHtmlString();
                return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
            }
        });
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
                if (e.getClickCount() == 2) {
                    if (index >= 0) {
                        editSelectedListEntry();
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


        JScrollPane itemListPane = new JScrollPane(itemList);
        itemListPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        itemListPane.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight() - 190));

        // Panel to hold the header and list together
        listPanel = new JPanel(new BorderLayout());
        listPanel.add(tableHeader, BorderLayout.NORTH);
        listPanel.add(itemListPane, BorderLayout.CENTER);

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
                        if (closeCurrentOpenFile()) {
                            frame.dispose();
                            System.exit(0);
                        }
                    }
                }
        );
    }

    public static void setBackgroundColor(Color color) {
        if (listPanel != null) listPanel.setBackground(color);
        if (itemList != null) itemList.setBackground(color);
    }

    public static Month getCurrentMonth() {
        return JSONHandler.getMonth(monthSettingsBar, listModel);
    }

    public static String getCurrentMonthName() {
        return monthSettingsBar.getSelectedMonthName();
    }

    public static int getCurrentMonthNumber() {
        return monthSettingsBar.getSelectedMonthNumber();
    }

    public static String getYear() {
        return monthSettingsBar.getYear();
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

    /**
     * Clears the workspace. Will prompt to save changes if there are any.
     * Returns if to proceed (true) or not (false). If false is returned, there are unsaved
     * changes and the cancel button was pressed.
     * @return If proceed or not.
     */
    public static boolean clearWorkspace() {
        if (!closeCurrentOpenFile()) return false;
        // Delete all content
        monthSettingsBar.reset();
        listModel.clear();
        setHasUnsavedChanges(false);
        return true;
    }

    /**
     * Closes the currently open file. Will prompt to save changes if there are any.
     * Returns if to proceed (true) or not (false). If false is returned, there are unsaved
     * changes and the cancel button was pressed.
     * @return If proceed or not.
     */
    public static boolean closeCurrentOpenFile() {
        if (!hasUnsavedChanges) return true;
        // Prompt to save
        return SaveOnClosePrompt.showDialog();
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
        File openFile = FileChooser.chooseFile("Open a file", FileChooserType.MONTH_PATH);
        openFile(openFile);
    }

    public static void openFile(File openFile) {
        if (openFile == null) return;
        if (!setEditorFile(openFile)) return;
        boolean proceed = clearWorkspace();
        if (!proceed) return;

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
        if (!JSONHandler.isFileValidMonth(file)) {
            showError("The file is not a valid month.json file or could not be parsed.");
            return false;
        }
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

    public static boolean isSpaceForNewEntry() {
        return listModel.size() < MAX_ENTRIES;
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
        final int selectedItemIndex = itemList.getSelectedIndex();
        if (selectedItemIndex < 0) return;
        setHasUnsavedChanges(true);

        addEntry(listModel.getElementAt(selectedItemIndex).clone());
        itemList.setSelectedIndex(selectedItemIndex + 1);    // 1 more, there is the duplicate
        editSelectedListEntry();
    }

    public static void editSelectedListEntry() {
        final int selectedItemIndex = itemList.getSelectedIndex();
        if (selectedItemIndex < 0) return;
        TimesheetEntry entry = listModel.getElementAt(selectedItemIndex);
        DialogHelper.showEntryDialog("Edit Entry", entry);
        listModel.removeElement(entry);
        itemList.setSelectedIndex(-1);
        updateTotalTimeWorkedUI();
    }

    public static void removeSelectedListEntry() {
        final int selectedItemIndex = itemList.getSelectedIndex();
        if (selectedItemIndex < 0) return;

        if (!showOKCancelDialog("Delete Entry?", "Delete Entry: %s?".formatted(listModel.getElementAt(selectedItemIndex).toShortString()))) return;

        setHasUnsavedChanges(true);
        listModel.removeElementAt(selectedItemIndex);
        itemList.setSelectedIndex(-1);
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
        JOptionPane.showMessageDialog(frame, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showError(String error) {
        JOptionPane.showMessageDialog(frame, error, "Unknown error", JOptionPane.ERROR_MESSAGE);
    }

    private static boolean showOKCancelDialog(String title, String message) {
        // Result:
        // OK: 0
        // Cancel: 2
        // Close via X or Esc: -1
        return 0 == JOptionPane.showConfirmDialog(frame, message, title, JOptionPane.OK_CANCEL_OPTION);
    }

    public static void main(String[] args) {

        File file;
        if (args.length == 1) {
            File f = new File(args[0]);
            if (f.exists()) file = f;
            else file = null;
        } else {
            file = null;
        }

        JSONHandler.initialize();
        // Ensure the application uses the system look and feel
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new Main();
                setHasUnsavedChanges(false);
                if (file != null) openFile(file);
            } catch (Exception e) {
                showError(e.getMessage());
            }
        });
    }
}
