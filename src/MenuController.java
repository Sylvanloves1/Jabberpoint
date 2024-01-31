import java.awt.MenuBar;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/** <p>The controller for the menu</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */
public class MenuController extends MenuBar {
	
	private Frame parent; //The frame, only used as parent for the Dialogs
	private Presentation presentation; //Commands are given to the presentation
	
	private static final long serialVersionUID = 227L;
	
	protected static final String IOEX = "IO Exception: ";

	private MenuItem menuItem;

	public MenuController(Frame frame, Presentation pres) {
		parent = frame;
		presentation = pres;
		
		this.fileMenu();
		this.viewMenu();
		this.helpMenu();
	}

	//Creating a menu-item
	public MenuItem mkMenuItem(String name) {
		return new MenuItem(name, new MenuShortcut(name.charAt(0)));
	}

	/*=====================================================================
	 * FILEMENU
	 ====================================================================*/

	 //Creates file menu
	 public void fileMenu(){
		Menu fileMenu = new Menu(JabberPoint.text("file"));

		this.open(fileMenu);
		this.newfile(fileMenu);
		this.save(fileMenu);
		fileMenu.addSeparator();
		this.exit(fileMenu);

		add(fileMenu);
	}

	//Opens a file
	public void open(Menu fileMenu) {
		fileMenu.add(menuItem = mkMenuItem("Open"));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Bestanden", "xml");
				fileChooser.setFileFilter(filter);
	
				int result = fileChooser.showOpenDialog(parent);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					String fileName = selectedFile.getAbsolutePath();
	
					XMLAccessor xmlAccessor = new XMLAccessor();
					try {
						presentation.clear();
						xmlAccessor.loadFile(presentation, fileName);
						presentation.setSlideNumber(0);
					} catch (IOException exc) {
						JOptionPane.showMessageDialog(parent, JabberPoint.text("IOERR") + exc,
						JabberPoint.text("loadError"), JOptionPane.ERROR_MESSAGE);
					}
					parent.repaint();
				}
			}
		});
	}

	//Creates a new file
	public void newfile(Menu fileMenu){
		fileMenu.add(menuItem = mkMenuItem(JabberPoint.text("new")));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				presentation.clear();
				parent.repaint();
			}
		});
	}


	//Saves the file
	public void save(Menu fileMenu){
		fileMenu.add(menuItem = mkMenuItem(JabberPoint.text("save")));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fileName = JOptionPane.showInputDialog((Object)JabberPoint.text("fileName"));
				XMLAccessor xmlAccessor = new XMLAccessor();
				try {
					xmlAccessor.saveFile(presentation, fileName);
				} catch (IOException exc) {
					JOptionPane.showMessageDialog(parent, IOEX + exc, 
					JabberPoint.text("saveError"), JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	//Closes the file
	public void exit(Menu fileMenu) {
		fileMenu.add(menuItem = mkMenuItem(JabberPoint.text("exit")));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				presentation.exit(0);
			}
		});
	}

	/*=====================================================================
	 * VIEWMENU
	 ====================================================================*/

	//Create the view menu 
	public void viewMenu(){
		Menu viewMenu = new Menu(JabberPoint.text("view"));

		this.next(viewMenu);
		this.prev(viewMenu);
		this.goTo(viewMenu);

		add(viewMenu);
	}

	//Goes to the next slide
	public void next(Menu viewMenu) {
		viewMenu.add(menuItem = mkMenuItem(JabberPoint.text("next")));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				presentation.nextSlide();
			}
		});
	}

	//Goes to the previous slide
	public void prev(Menu viewMenu) {
		viewMenu.add(menuItem = mkMenuItem(JabberPoint.text("prev")));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				presentation.prevSlide();
			}
		});
	}

	//Goes to the input 
	public void goTo(Menu viewMenu) {
		viewMenu.add(menuItem = mkMenuItem(JabberPoint.text("goTo")));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				int numberOfSlides = presentation.getSize();
				String[] pageNumbers = new String[numberOfSlides];
				for (int i = 0; i < numberOfSlides; i++) {
					pageNumbers[i] = Integer.toString(i + 1);
				}

				JComboBox<String> comboBox = new JComboBox<>(pageNumbers);
				JOptionPane.showMessageDialog(null, comboBox, JabberPoint.text("selectPageNumber"), JOptionPane.QUESTION_MESSAGE);

				String selectedPageNumberStr = (String) comboBox.getSelectedItem();
				if (selectedPageNumberStr != null) {
					int selectedPageNumber = Integer.parseInt(selectedPageNumberStr);
					presentation.setSlideNumber(selectedPageNumber - 1);
				}
			}
		});
	}

	/*=====================================================================
	 * HELPMENU
	 ====================================================================*/

	//Creates Help menu dropdown
	public void helpMenu(){
		Menu helpMenu = new Menu("Help");
		this.about(helpMenu);
		setHelpMenu(helpMenu);		//Needed for portability (Motif, etc.).
	}

	//Adds about option to help menu
	public void about(Menu helpMenu){
		helpMenu.add(this.menuItem = mkMenuItem(JabberPoint.text("about")));
		this.menuItem.addActionListener(actionEvent -> openAboutBox());
	}

	//opens about box
	public void openAboutBox(){
		JOptionPane.showMessageDialog(parent,
				JabberPoint.text("aboutText"),
				JabberPoint.text("aboutJab"),
				JOptionPane.INFORMATION_MESSAGE
		);
	}
}
