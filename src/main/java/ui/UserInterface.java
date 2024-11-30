/* Licensed under MIT 2024. */
package ui;

import ui.fileexplorer.FileChooser;
import ui.fileexplorer.FileChooserType;
import ui.json.JSONHandler;
import ui.json.Month;
import ui.json.OtherSettings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class UserInterface {

	public static final int MAX_ENTRIES = 22;

	private static final String APP_NAME = "Timesheet Generator";
	private static final String TITLE = "%s: %s";

	private File currentOpenFile;
	public boolean hasUnsavedChanges = false;

	private JFrame frame;
	private JPanel listPanel;
	private JList<TimesheetEntry> itemList;
	private DefaultListModel<TimesheetEntry> listModel;

	private MonthlySettingsBar monthSettingsBar;
	private ActionBar buttonActionBar;

	public UserInterface() {
		initialize();
	}

	private void initialize() {
		// Main Frame
		frame = new DragDropJFrame(this);
		setTitle(null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // terminates when no saved changes
		frame.setSize(1200, 800);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new BorderLayout());
		frame.setResizable(false);

		// Initialize JSONHandler. It needs the frame to exist to display error messages
		JSONHandler.initialize(this);

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

		monthSettingsBar = new MonthlySettingsBar(this);
		monthSettingsBar.setFont(monthSettingsBar.getFont().deriveFont(14f));
		frame.add(monthSettingsBar, BorderLayout.NORTH);

		// Row of Buttons with '+'
		buttonActionBar = new ActionBar(this);
		monthSettingsBar.setFont(monthSettingsBar.getFont().deriveFont(14f));
		frame.add(buttonActionBar, BorderLayout.WEST);

		// Main Content Area with Vertical List
		listModel = new DefaultListModel<>();
		itemList = new JList<>(listModel);
		itemList.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

				String text = ((TimesheetEntry) value).toHtmlString();
				return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
			}
		});
		itemList.setBorder(new EmptyBorder(10, 10, 10, 10));
		itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		itemList.setFont(itemList.getFont().deriveFont(16f));

		// Table header setup
		JLabel tableHeader = new JLabel();
		tableHeader.setText(String.format(TimesheetEntry.TIMESHEET_FORMAT_HEADER, "Activity", "Day", "Start Time", "End Time", "Break Time", "Vacation",
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
		/*
		 * For the remove button itemList.addFocusListener(new FocusAdapter() { public
		 * void focusGained(FocusEvent e) { selectedItemIndex =
		 * previousSelectedItemIndex; } public void focusLost(FocusEvent e) {
		 * previousSelectedItemIndex = selectedItemIndex; selectedItemIndex = -1; } });
		 */
		System.out.printf("Frame height: %d, bar height: %d, settings height: %d, bar Y: %d, settings Y: %d%n", frame.getHeight(), buttonActionBar.getHeight(),
				monthSettingsBar.getHeight(), buttonActionBar.getLocation().y, monthSettingsBar.getLocation().y);

		JScrollPane itemListPane = new JScrollPane(itemList);
		itemListPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		itemListPane.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight() - 190));

		// Panel to hold the header and list together
		listPanel = new JPanel(new BorderLayout());
		listPanel.add(tableHeader, BorderLayout.NORTH);
		listPanel.add(itemListPane, BorderLayout.CENTER);

		frame.add(listPanel, BorderLayout.SOUTH);

		// Action Listeners
		// addButton.addActionListener(e -> showEntryDialog("Add Entry", "", -1));

		fileOptionNew.addActionListener(e -> clearWorkspace());
		fileOptionOpen.addActionListener(e -> openFile());
		fileOptionGlobalSettings.addActionListener(e -> GlobalSettingsDialog.showGlobalSettingsDialog(this));
		fileOptionSave.addActionListener(e -> saveFile(currentOpenFile));
		fileOptionSaveAs.addActionListener(e -> saveFileAs());

		// Add Strg + S to save
		// Define the KeyStroke for Ctrl+S or Command+S on Mac
		KeyStroke saveKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());

		// Bind the KeyStroke to the save action in the InputMap and ActionMap
		frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(saveKeyStroke, "saveAction");
		frame.getRootPane().getActionMap().put("saveAction", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveFile(currentOpenFile);
			}
		});

		// Show Frame
		frame.setVisible(true);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (closeCurrentOpenFile()) {
					frame.dispose();
					System.exit(0);
				}
			}
		});
	}

	public void setBackgroundColor(Color color) {
		if (listPanel != null)
			listPanel.setBackground(color);
		if (itemList != null)
			itemList.setBackground(color);
	}

	public Month getCurrentMonth() {
		return JSONHandler.getMonth(monthSettingsBar, listModel);
	}

	public String getCurrentMonthName() {
		return monthSettingsBar.getSelectedMonthName();
	}

	public int getCurrentMonthNumber() {
		return monthSettingsBar.getSelectedMonthNumber();
	}

	public String getYear() {
		return monthSettingsBar.getYear();
	}

	public File getCurrentOpenFile() {
		return currentOpenFile;
	}

	public boolean hasUnsavedChanges() {
		return hasUnsavedChanges;
	}

	public File generateTempMonthFile() {
		return JSONHandler.generateTemporaryJSONFile(this, monthSettingsBar, listModel);
	}

	public Time getPredTime() {
		return monthSettingsBar.getPredTime();
	}

	public int getWidth() {
		return frame.getWidth();
	}

	/**
	 * Clears the workspace. Will prompt to save changes if there are any. Returns
	 * if to proceed (true) or not (false). If false is returned, there are unsaved
	 * changes and the cancel button was pressed.
	 * 
	 * @return If proceed or not.
	 */
	public boolean clearWorkspace() {
		if (!closeCurrentOpenFile())
			return false;
		// Delete all content
		monthSettingsBar.reset();
		listModel.clear();
		setHasUnsavedChanges(false);
		return true;
	}

	/**
	 * Closes the currently open file. Will prompt to save changes if there are any.
	 * Returns if to proceed (true) or not (false). If false is returned, there are
	 * unsaved changes and the cancel button was pressed.
	 * 
	 * @return If proceed or not.
	 */
	public boolean closeCurrentOpenFile() {
		if (!hasUnsavedChanges)
			return true;
		// Prompt to save
		return SaveOnClosePrompt.showDialog(this);
	}

	public void saveFile(File newSaveFile) {
		if (newSaveFile == null)
			newSaveFile = currentOpenFile;
		if (newSaveFile == null) {
			saveFileAs();
			return;
		}
		saveFileCommon(newSaveFile);
	}

	public void saveFileAs() {
		File newSaveFile = FileChooser.chooseCreateJSONFile(this, "Save as...");
		if (newSaveFile == null)
			return;
		saveFileCommon(newSaveFile);
		setEditorFile(newSaveFile);
	}

	public void saveFileCommon(File newSaveFile) {
		JSONHandler.saveMonth(this, newSaveFile, monthSettingsBar, listModel);

		currentOpenFile = newSaveFile;
		setHasUnsavedChanges(false);
	}

	public void openFile() {
		File openFile = FileChooser.chooseFile(this, "Open a file", FileChooserType.MONTH_PATH);
		openFile(openFile);
	}

	public void openFile(File openFile) {
		if (openFile == null)
			return;
		if (!setEditorFile(openFile))
			return;
		boolean proceed = clearWorkspace();
		if (!proceed)
			return;

		// Open the file

		JSONHandler.loadMonth(this, openFile);
		setHasUnsavedChanges(false);
	}

	public void importMonthBarSettings(Month month) {
		monthSettingsBar.importMonthSettings(month);
	}

	public void setHasUnsavedChanges(boolean hasUnsavedChanges) {
		this.hasUnsavedChanges = hasUnsavedChanges;
		updateTitle();
	}

	public boolean setEditorFile(File file) {
		if (!file.exists())
			return false;
		String name = file.getName();
		if (!name.endsWith(".json"))
			return false;
		if (!JSONHandler.isFileValidMonth(file)) {
			showError("Invalid JSON File", "The file is not a valid month.json file or could not be parsed.");
			return false;
		}
		currentOpenFile = file;
		updateTitle();
		return true;
	}

	private void setTitle(String title) {
		if (title == null || title.isBlank()) {
			frame.setTitle(APP_NAME);
			return;
		}
		frame.setTitle(TITLE.formatted(APP_NAME, title));
	}

	private void updateTitle() {
		String filename = currentOpenFile == null ? "" : "%s/%s".formatted(currentOpenFile.getParentFile().getName(), currentOpenFile.getName());
		if (hasUnsavedChanges)
			filename += '*';
		setTitle(filename);
	}

	public boolean isSpaceForNewEntry() {
		return listModel.size() < MAX_ENTRIES;
	}

	public void addEntry(TimesheetEntry entry) {
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

	public void duplicateSelectedListEntry() {
		final int selectedItemIndex = itemList.getSelectedIndex();
		if (selectedItemIndex < 0)
			return;
		setHasUnsavedChanges(true);

		addEntry(listModel.getElementAt(selectedItemIndex).clone());
		itemList.setSelectedIndex(selectedItemIndex + 1); // 1 more, there is the duplicate
		editSelectedListEntry();
	}

	public void editSelectedListEntry() {
		final int selectedItemIndex = itemList.getSelectedIndex();
		if (selectedItemIndex < 0)
			return;
		TimesheetEntry entry = listModel.getElementAt(selectedItemIndex);
		DialogHelper.showEntryDialog(this, "Edit Entry", entry);
		listModel.removeElement(entry);
		itemList.setSelectedIndex(-1);
		updateTotalTimeWorkedUI();
	}

	public void removeSelectedListEntry() {
		final int selectedItemIndex = itemList.getSelectedIndex();
		if (selectedItemIndex < 0)
			return;

		if (!showOKCancelDialog("Delete Entry?", "Delete Entry: %s?".formatted(listModel.getElementAt(selectedItemIndex).toShortString())))
			return;

		setHasUnsavedChanges(true);
		listModel.removeElementAt(selectedItemIndex);
		itemList.setSelectedIndex(-1);
	}

	public void updateTotalTimeWorkedUI() {
		Time worked = calculateTotalTimeWorked();
		Time succTime = buttonActionBar.updateHours(worked);
		monthSettingsBar.setSuccTime(succTime.toString());
	}

	private Time calculateTotalTimeWorked() {
		Time workedTime = new Time();
		for (int i = 0; i < listModel.getSize(); i++) {
			TimesheetEntry entry = listModel.getElementAt(i);
			workedTime.addTime(entry.getWorkedTime());
		}
		return workedTime;
	}

	public void showError(String title, String error) {
		JOptionPane.showMessageDialog(frame, error, title, JOptionPane.ERROR_MESSAGE);
	}

	private boolean showOKCancelDialog(String title, String message) {
		int result = JOptionPane.showConfirmDialog(frame, message, title, JOptionPane.OK_CANCEL_OPTION);
		return JOptionPane.OK_OPTION == result;
	}

	public static void main(String[] args) {

		File file = args.length == 1 ? new File(args[0]) : null;

		// Ensure the application uses the system look and feel
		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				UserInterface ui = new UserInterface();
				ui.setHasUnsavedChanges(false);
				if (file != null && file.exists())
					ui.openFile(file);
			} catch (Exception e) {
				JFrame frame = new JFrame();
				JOptionPane.showMessageDialog(frame, e.getMessage(), "An error occurred", JOptionPane.ERROR_MESSAGE);
				// Exit, because if the setup fails, the process shouldn't continue (but it sometimes does otherwise)
				System.exit(1);
			}
		});
	}
}
