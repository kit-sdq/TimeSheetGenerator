/* Licensed under MIT 2024-2025. */
package ui;

import lombok.Getter;
import ui.fileexplorer.FileChooser;
import ui.fileexplorer.FileChooserType;
import ui.json.JSONHandler;
import ui.json.Month;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class UserInterface {

	/**
	 * Maximum number of entries on the time sheet. Not a technical limitation, but
	 * the PDF has 22 rows, and it's always supposed to be only one (1) page.
	 */
	public static final int MAX_ENTRIES = 22;

	private static final String APP_NAME = "Timesheet Generator";
	private static final String TITLE = "%s: %s";

	@Getter
	private File currentOpenFile;
	private boolean hasUnsavedChanges = false;

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
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); // terminates when no saved changes
		frame.setSize(1200, 800);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new BorderLayout());
		frame.setResizable(false);
		ErrorHandler.setParentComponent(frame);

		// Initialize JSONHandler. It needs the frame to exist to display error messages
		JSONHandler.initialize();

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
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = itemList.locationToIndex(e.getPoint());
				if (e.getClickCount() == 2 && index >= 0) {
					editSelectedListEntry();
				}
			}
		});

		JScrollPane itemListPane = new JScrollPane(itemList);
		itemListPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		itemListPane.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight() - 190));

		// Panel to hold the header and list together
		listPanel = new JPanel(new BorderLayout());
		listPanel.add(tableHeader, BorderLayout.NORTH);
		listPanel.add(itemListPane, BorderLayout.CENTER);

		frame.add(listPanel, BorderLayout.SOUTH);

		// Action Listeners

		fileOptionNew.addActionListener(e -> clearWorkspace());
		fileOptionOpen.addActionListener(e -> openFile());
		fileOptionGlobalSettings.addActionListener(e -> GlobalSettingsDialog.showGlobalSettingsDialog(this));
		fileOptionSave.addActionListener(e -> saveFile(currentOpenFile));
		fileOptionSaveAs.addActionListener(e -> saveFileAs());

		addHotkeys(itemList);

		// Show Frame
		frame.setVisible(true);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (closeCurrentOpenFile()) {
					frame.dispose();
					System.exit(0);
				}
			}
		});
		itemList.requestFocusInWindow();
	}

	/**
	 * Adds all hotkeys to the current {@link UserInterface#frame}. Most Hotkeys
	 * just perform actions from buttons in the {@link ActionBar}. Current Hotkeys
	 * are:
	 * <li>Ctrl S - Saves the file to JSON</li>
	 * <li>Ctrl + Shift + S - Saves the file to JSON with a prompt for a new
	 * file</li>
	 * <li>Ctrl A - Add a new entry</li>
	 * <li>Ctrl D - Duplicate the selected entry</li>
	 * <li>Ctrl E - Exports to PDF</li>
	 * <li>Ctrl P - Same as Ctrl E, synonym</li>
	 * <li>Ctrl T - Compiles to LaTeX</li>
	 * <li>Ctrl N - Opens a new file</li>
	 * <li>Ctrl O - Opens an existing file</li>
	 *
	 * @param itemList The main item list, this will override the Ctrl + A keybind
	 *                 to add entry as well.
	 */
	private void addHotkeys(final JList<?> itemList) {
		// Ctrl + S to save
		addHotkey(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx(), "saveAction", () -> saveFile(currentOpenFile));

		// Ctrl + Shift + S to save As ...
		addHotkey(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | InputEvent.SHIFT_DOWN_MASK, "saveAsAction", this::saveFileAs);

		// Ctrl + A to add a new entry
		addHotkey(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx(), "addEntryAction", buttonActionBar::addEntryButtonClicked);
		// Replace the default “selectAll” for the list itself by overriding selectAll
		// action
		itemList.getActionMap().put("selectAll", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonActionBar.addEntryButtonClicked();
			}
		});

		// Remove selected entry with backspace key
		itemList.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "removeListEntryBackspace");
		itemList.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "removeListEntryDelete");
		itemList.getActionMap().put("removeListEntry", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeSelectedListEntry();
			}
		});

		// Ctrl + D to duplicate the selected entry
		addHotkey(KeyEvent.VK_D, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx(), "duplicateEntryAction", this::duplicateSelectedListEntry);

		// Ctrl + E and Ctrl + P should both print (export) to PDF
		addHotkey(KeyEvent.VK_E, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx(), "exportActionE", buttonActionBar::exportPdfButtonClicked);
		addHotkey(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx(), "exportActionP", buttonActionBar::exportPdfButtonClicked);

		// Ctrl + T to compile to LaTeX
		addHotkey(KeyEvent.VK_T, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx(), "compileAction", buttonActionBar::compileTexButtonClicked);

		// Ctrl + O to open an existing file
		addHotkey(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx(), "openExistingFile", this::openFile);

		// Ctrl + N to open a new file
		addHotkey(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx(), "openNewFile", this::clearWorkspace);
	}

	/**
	 * Adds a hotkey to the main JFrame. The key should come from {@link KeyEvent},
	 * and the keyModifiers should be from {@link InputEvent}. The action is the
	 * method performed when the key combination is pressed.
	 * <p>
	 * For proper Ctrl,
	 * {@code Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()} is okay too.
	 * </p>
	 * 
	 * @param key          The key / letter itself. Should be from
	 *                     {@link KeyEvent}.VK_<?>
	 * @param keyModifiers The key modifiers, like Shift or Ctrl. Should be from
	 *                     {@link InputEvent}
	 * @param actionName   The name for the action. Should be unique, as this is the
	 *                     map key.
	 * @param action       The action to run when the combination is pressed.
	 */
	@SuppressWarnings("all") // for usage of keyModifiers
	private void addHotkey(int key, int keyModifiers, String actionName, Runnable action) {
		frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key, keyModifiers), actionName);
		frame.getRootPane().getActionMap().put(actionName, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				action.run();
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
		return monthSettingsBar.getYearStr();
	}

	public String getFullYear() {
		return monthSettingsBar.getFullYearStr();
	}

	public boolean hasUnsavedChanges() {
		return hasUnsavedChanges;
	}

	public File generateTempMonthFile() {
		return JSONHandler.generateTemporaryJSONFile(monthSettingsBar, listModel);
	}

	public Time getPredTime() {
		return monthSettingsBar.getPredTime();
	}

	public boolean hasWorkedHoursMismatch() {
		// Option 1: worked too little -> Time of action bar will not match
		Time targetWorkingTime = Time.parseTime(JSONHandler.getGlobalSettings().getWorkingTime());
		Time actualWorkingTime = calculateTotalTimeWorked();
		return !targetWorkingTime.sameLengthAs(actualWorkingTime);
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
		currentOpenFile = null;
		listModel.clear();
		monthSettingsBar.reset();
		buttonActionBar.reset();
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
	private boolean closeCurrentOpenFile() {
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
		JSONHandler.saveMonth(newSaveFile, monthSettingsBar, listModel);

		currentOpenFile = newSaveFile;
		setHasUnsavedChanges(false);
	}

	public void openFile() {
		File openFile = FileChooser.chooseFile("Open a file", FileChooserType.MONTH_PATH);
		openFile(openFile);
	}

	public void openFile(File openFile) {
		if (openFile == null)
			return;
		if (!clearWorkspace()) // Don't proceed: Clearing was cancelled
			return;
		if (!setEditorFile(openFile))
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
			ErrorHandler.showError("Invalid JSON File", "The file is not a valid month.json file or could not be parsed.");
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
		// Loop and exclude holiday entries. Keeping track live invite bugs, and for <=
		// 22 entries + reasonable holidays, this works. If you really want to just
		// break the program, there are easier ways of doing it
		int entries = listModel.size();
		for (int i = 0; i < itemList.getModel().getSize(); i++) {
			if (listModel.getElementAt(i).isVacation())
				entries--;
		}
		return entries < MAX_ENTRIES;
	}

	public void addEntry(TimesheetEntry entry) {
		if (entry.isNone())
			return;
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

		addEntry(new TimesheetEntry(listModel.getElementAt(selectedItemIndex)));
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
		updateTotalTimeWorkedUI();
	}

	public void updateTotalTimeWorkedUI() {
		Time worked = calculateTotalTimeWorked();
		Time succTime = buttonActionBar.updateHours(worked);
		monthSettingsBar.setSuccTime(succTime);
	}

	private Time calculateTotalTimeWorked() {
		Time workedTime = new Time();
		for (int i = 0; i < listModel.getSize(); i++) {
			TimesheetEntry entry = listModel.getElementAt(i);
			workedTime.addTime(entry.getWorkedTime());
		}
		return workedTime;
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public boolean showOKCancelDialog(String title, String message) {
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
				// Exit, because if the setup fails, the process shouldn't continue (but it
				// sometimes does otherwise)
				System.exit(1);
			}
		});
	}
}
