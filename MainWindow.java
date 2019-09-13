// Copyright Eric Chauvin 2019.


// Make a menu: Alt-K for Keyboard.
//  Make it move to tabs in the tab control and all that.
// Set the focus in to the current page, etc.


// https://docs.oracle.com/javase/7/docs/api/javax/swing/text/DefaultCaret.html





import javax.swing.JFrame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowStateListener;
import java.awt.Container;
// import java.awt.Insets;
// import javax.swing.JButton;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.KeyStroke;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.Timer;
// import javax.swing.text.DefaultCaret;




// black, blue, cyan, darkGray, gray, green, lightGray,
// magenta, orange, pink, red, white, yellow.



public class MainWindow extends JFrame implements
                                WindowListener,
                                WindowFocusListener,
                                WindowStateListener,
                                ActionListener
  {
  // It needs to have a version UID since it's
  // serializable.
  public static final long serialVersionUID = 1;
  private MainApp mApp;
  private Font mainFont;
  private Font menuFont;
  private JTabbedPane mainTabbedPane;
  private JLabel statusLabel;
  private JTextArea statusTextArea;
  private EditorTabPage[] tabPagesArray;
  private int tabPagesArrayLast = 0;
  private String showProjectText = "";
  // private String searchText = "";
  private String statusFileName = "";
  private final int maximumTabsOpen = 30;
  private Timer keyboardTimer;
  private boolean windowIsClosing = false;


  private MainWindow()
    {
    }


  public MainWindow( MainApp useApp, String showText )
    {
    super( showText );

    mApp = useApp;

    tabPagesArray = new EditorTabPage[2];

    mainFont = new Font( Font.MONOSPACED, Font.PLAIN, 40 );
    menuFont = new Font( Font.MONOSPACED, Font.PLAIN, 46 );

    setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );

    setSize( 1200, 600 );
    addComponents( getContentPane() );
    // pack();
    // setExtendedState( JFrame.MAXIMIZED_BOTH );

    setupMenus();

    openRecentFiles();

    addWindowListener( this );
    addWindowFocusListener( this );
    addWindowStateListener( this );

    setShowProjectText();

    // Center it.
    setLocationRelativeTo( null );
    setVisible( true );

    showStatus( "Programming by Eric Chauvin." );
    showStatus( "Version date: " + MainApp.versionDate );

    setupTimer();
    }



  public void windowStateChanged( WindowEvent e )
    {
    // showStatus( "windowStateChanged" );
    }


  public void windowGainedFocus( WindowEvent e )
    {
    // showStatus( "windowGainedFocus" );
    }


  public void windowLostFocus( WindowEvent e )
    {
    // showStatus( "windowLostFocus" );
    }



  public void windowOpened( WindowEvent e )
    {
    // showStatus( "windowOpened" );
    }



  public void windowClosing( WindowEvent e )
    {
    windowIsClosing = true;
    }


  public void windowClosed( WindowEvent e )
    {
    // showStatus( "windowClosed" );
    }


  public void windowIconified( WindowEvent e )
    {
    keyboardTimer.stop();
    // showStatus( "windowIconified" );
    }



  public void windowDeiconified( WindowEvent e )
    {
    keyboardTimer.start();
    // showStatus( "windowDeiconified" );
    }



  public void windowActivated( WindowEvent e )
    {
    // showStatus( "windowActivated" );
    }


  public void windowDeactivated( WindowEvent e )
    {
    // showStatus( "windowDeactivated" );
    }



  private void addComponents( Container pane )
    {
    // pane.setBackground( Color.red );
    // pane.setForeground( Color.red );
    pane.setLayout( new LayoutSimpleVertical());

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout( new LayoutSimpleVertical());
    mainPanel.setBackground( Color.black );
    // Setting it to FixedHeightMax means this component is
    // stretchable.
    // new Dimension( Width, Height );
    mainPanel.setPreferredSize( new Dimension(
                   1, LayoutSimpleVertical.FixedHeightMax ));

    pane.add( mainPanel );

    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout( new LayoutSimpleVertical());
    bottomPanel.setBackground( Color.black );
    bottomPanel.setPreferredSize( new Dimension( 1, 74 ));
    pane.add( bottomPanel );

    statusLabel = new JLabel( " Label with numbers to see." );
    statusLabel.setForeground( Color.white );
    statusLabel.setFont( mainFont );
    bottomPanel.add( statusLabel );

    // Add a second label below the first.
    // JLabel testLabel = new JLabel( " Test label." );

    // black, blue, cyan, darkGray, gray, green, lightGray,
    // magenta, orange, pink, red, white, yellow.

    mainTabbedPane = new JTabbedPane();
    mainTabbedPane.setBackground( Color.black );
    // mainTabbedPane.setForeground( Color.white );

    // Not these:
    // mainTabbedPane.setSelectedTextColor( Color.black );
    // mainTabbedPane.setSelectionColor( Color.white );

    mainTabbedPane.setFont( menuFont );
    mainTabbedPane.setPreferredSize( new Dimension(
                   1, LayoutSimpleVertical.FixedHeightMax ));

    mainPanel.add( mainTabbedPane );

    addStatusTextPane();
    }




  public void showStatus( String toShow )
    {
    if( statusTextArea == null )
      return;

    statusTextArea.append( toShow + "\n" );
    }



  public void clearStatus()
    {
    if( statusTextArea == null )
      return;

    statusTextArea.setText( "" );
    }



  private void addStatusTextPane()
    {
    statusTextArea = new JTextArea();
    addTextPane( statusTextArea,
                 "Status",
                 statusFileName );

    }



  private EditorTabPage[] resizeTabPagesArray( EditorTabPage[] in, int sizeToAdd )
    {
    EditorTabPage[] newArray;
    try
    {
    newArray = new EditorTabPage[in.length + sizeToAdd];
    int max = in.length;
    for( int count = 0; count < max; count++ )
      newArray[count] = in[count];

    }
    catch( Exception e )
      {
      showStatus( "Couldn't allocate a new array for the EditorTabPage array." );
      showStatus( e.getMessage() );
      return in;
      }

    return newArray;
    }



  private void addTextPane( JTextArea textAreaToUse,
                            String tabTitle,
                            String fileName )
    {
    JTextArea textArea1;
    if( textAreaToUse == null )
      textArea1 = new JTextArea();
    else
      textArea1 = textAreaToUse;

    textArea1.setFont( mainFont );
    textArea1.setLineWrap( false );
    // textArea1.setWrapStyleWord( true );
    textArea1.setBackground( Color.black );
    textArea1.setForeground( Color.white );
    textArea1.setSelectedTextColor( Color.white );
    textArea1.setSelectionColor( Color.darkGray );
    // black, blue, cyan, darkGray, gray, green, lightGray,
    // magenta, orange, pink, red, white, yellow.

    CaretWide cWide = new CaretWide();
    textArea1.setCaret( cWide );
    textArea1.setCaretColor( Color.white );

    JScrollPane scrollPane1 = new JScrollPane( textArea1 );

    scrollPane1.setVerticalScrollBarPolicy(
             JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );

    mainTabbedPane.addTab( tabTitle, null, scrollPane1,
                      "Tool tip." );

    JLabel tabLabel = new JLabel( tabTitle );
    tabLabel.setOpaque( true );
    tabLabel.setForeground( Color.white );
    tabLabel.setBackground( Color.black );
    tabLabel.setFont( menuFont );
    mainTabbedPane.setTabComponentAt( tabPagesArrayLast,
                                tabLabel );

    EditorTabPage newPage = new EditorTabPage( mApp,
                                               tabTitle,
                                               fileName,
                                               textArea1 );

    newPage.readFromTextFile();

    tabPagesArray[tabPagesArrayLast] = newPage;
    // ProjectConfigFile.SetString( "RecentFile" + TabPagesArrayLast.ToString(), FileName, true );
    tabPagesArrayLast++;

    if( tabPagesArrayLast >= tabPagesArray.length )
      {
      tabPagesArray = resizeTabPagesArray( tabPagesArray, 16 );
      }
    }




  private void setupMenus()
    {
    JMenuBar menuBar = new JMenuBar();
    menuBar.setBackground( Color.black );

    ///////////////////////
    // File Menu:
    JMenu fileMenu = new JMenu( "File" );
    fileMenu.setMnemonic( KeyEvent.VK_F );
    fileMenu.setFont( menuFont );
    fileMenu.setForeground( Color.white );
    menuBar.add( fileMenu );

    JMenuItem menuItem = new JMenuItem( "Open" );
    menuItem.setMnemonic( KeyEvent.VK_O );
    menuItem.setActionCommand( "FileOpen" );
    menuItem.addActionListener( this );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( menuFont );
    fileMenu.add( menuItem );

    menuItem = new JMenuItem( "Save All", KeyEvent.VK_L );
    // menuItem.setAccelerator( KeyStroke.getKeyStroke(
       // KeyEvent.VK_1, ActionEvent.ALT_MASK ));
    menuItem.setActionCommand( "FileSaveAll" );
    menuItem.addActionListener( this );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( menuFont );
    fileMenu.add( menuItem );


    menuItem = new JMenuItem( "Close Current" );
    menuItem.setMnemonic( KeyEvent.VK_U );
    menuItem.setActionCommand( "FileCloseCurrent" );
    menuItem.addActionListener( this );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( menuFont );
    fileMenu.add( menuItem );


    menuItem = new JMenuItem( "Exit" );
    menuItem.setMnemonic( KeyEvent.VK_X );
    menuItem.setActionCommand( "FileExit" );
    menuItem.addActionListener( this );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( menuFont );
    fileMenu.add( menuItem );


    ///////////////////////
    // Edit Menu:
    JMenu editMenu = new JMenu( "Edit" );
    editMenu.setMnemonic( KeyEvent.VK_E );
    editMenu.setForeground( Color.white );
    editMenu.setFont( menuFont );
    menuBar.add( editMenu );

    menuItem = new JMenuItem( "Copy" );
    menuItem.setMnemonic( KeyEvent.VK_C );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( menuFont );
    menuItem.setActionCommand( "EditCopy" );
    menuItem.addActionListener( this );
    editMenu.add( menuItem );

    menuItem = new JMenuItem( "Cut" );
    menuItem.setMnemonic( KeyEvent.VK_T );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( menuFont );
    menuItem.setActionCommand( "EditCut" );
    menuItem.addActionListener( this );
    editMenu.add( menuItem );

    menuItem = new JMenuItem( "Paste" );
    menuItem.setMnemonic( KeyEvent.VK_P );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( menuFont );
    menuItem.setActionCommand( "EditPaste" );
    menuItem.addActionListener( this );
    editMenu.add( menuItem );

/*
    menuItem = new JMenuItem( "Select All" );
    menuItem.setMnemonic( KeyEvent.VK_A );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( menuFont );
    menuItem.setActionCommand( "EditSelectAll" );
    menuItem.addActionListener( this );
    editMenu.add( menuItem );
*/


    ///////////////////////
    // Project Menu:
    JMenu projectMenu = new JMenu( "Project" );
    projectMenu.setMnemonic( KeyEvent.VK_P );
    projectMenu.setForeground( Color.white );
    projectMenu.setFont( menuFont );
    menuBar.add( projectMenu );

    menuItem = new JMenuItem( "Set Current" );
    menuItem.setMnemonic( KeyEvent.VK_C );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( menuFont );
    menuItem.setActionCommand( "ProjectSetCurrent" );
    menuItem.addActionListener( this );
    projectMenu.add( menuItem );


    ///////////////////////
    // Help Menu:
    JMenu helpMenu = new JMenu( "Help" );
    helpMenu.setMnemonic( KeyEvent.VK_H );
    helpMenu.setForeground( Color.white );
    helpMenu.setFont( menuFont );
    menuBar.add( helpMenu );

    menuItem = new JMenuItem( "About" );
    menuItem.setMnemonic( KeyEvent.VK_A );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( menuFont );
    menuItem.setActionCommand( "HelpAbout" );
    menuItem.addActionListener( this );
    helpMenu.add( menuItem );

    setJMenuBar( menuBar );
    }



  private void setupTimer()
    {
    int delay = 250;
    keyboardTimer = new Timer( delay, this );
    keyboardTimer.start();
    }




  private void openFile()
    {
    final JFileChooser fc = new JFileChooser();
    // fc.setCurrentDirectory()

    // FileFilter filter = new FileNameExtensionFilter( "Text file", "txt" );
    // fc.setFileFilter( filter );
    // fc.addChoosableFileFilter( filter );

    int returnVal = fc.showOpenDialog( this );
    if( returnVal != JFileChooser.APPROVE_OPTION )
      return;

    // Save Dialog:
    // int returnVal = fc.showSaveDialog(FileChooserDemo.this);


    // https://docs.oracle.com/javase/7/docs/api/java/nio/file/package-summary.html
    // https://docs.oracle.com/javase/7/docs/api/java/io/File.html
    File file = fc.getSelectedFile();
    String fileName = file.getName();
    showStatus( "File name picked is: " + fileName );

    String pathName = file.getPath();
    showStatus( "Path name picked is: " + pathName );

    if( fileIsInTabPages( pathName ))
      {
      JOptionPane.showMessageDialog( this,
                 "The file is already in the tabs." );

      return;
      }

    addTextPane( null, fileName, pathName );
    }



  private void openRecentFiles()
    {
    mainTabbedPane.removeAll();
    tabPagesArrayLast = 0;
    addStatusTextPane();

    // showStatus( "Opening recent files." );

    // int howMany = 0;
    for( int count = 1; count <= maximumTabsOpen; count++ )
      {
      String fileName = mApp.projectConfigFile.getString(
                                "RecentFile" + count );

      if( fileName.length() < 1 )
        break;

      // howMany++;
      Path path = Paths.get( fileName );
      File file = path.toFile();
      String tabTitle = file.getName();
      // showStatus( "tabTitle is: " + tabTitle );

      // String pathName = file.getPath();
      // showStatus( "Path name picked is: " + pathName );

      addTextPane( null, tabTitle, fileName );
      }

    // showStatus( "Files opened: " + howMany );
    }



  public void saveAllFiles()
    {
    if( tabPagesArrayLast < 1 )
      return;

    for( int count = 1; count < tabPagesArrayLast; count++ )
      {
      tabPagesArray[count].writeToTextFile();

      String fileName = tabPagesArray[count].getFileName();
      mApp.projectConfigFile.setString( "RecentFile" + count, fileName, false );
      }

    showStatus( "Saving project config file." );
    mApp.projectConfigFile.writeToTextFile();
    showStatus( "Saving main config file." );
    mApp.mainConfigFile.writeToTextFile();
    }



  public void closeCurrentFile()
    {
    try
    {
    // Don't save anything automatically.
    // saveAllFiles();

    if( tabPagesArrayLast < 2 )
      return;

    // getTabCount()
    int selectedIndex = mainTabbedPane.getSelectedIndex();
    if( selectedIndex < 1 )
      return;

    if( selectedIndex >= tabPagesArrayLast )
      return;

    // Clear all RecentFile entries.
    for( int count = 1; count <= maximumTabsOpen; count++ )
      mApp.projectConfigFile.setString( "RecentFile" + count, "", false );

    int where = 1;
    for( int count = 1; count < tabPagesArrayLast; count++ )
      {
      if( count == selectedIndex )
        continue;

      String fileName = tabPagesArray[count].getFileName();
      mApp.projectConfigFile.setString( "RecentFile" + where, fileName, false );
      where++;
      }

    mApp.projectConfigFile.writeToTextFile();

    openRecentFiles();
    }
    catch( Exception e )
      {
      showStatus( "Exception in closeCurrentFile()." );
      showStatus( e.getMessage() );
      }
    }



  public void actionPerformed( ActionEvent event )
    {
    try
    {
    // String paramS = event.paramString();
    // showStatus( "paramS: " + paramS );

    String command = event.getActionCommand();

    if( command == null )
      {
      keyboardTimerEvent();
      return;
      }

    showStatus( "ActionEvent Command is: " + command );

    //////////////
    // File Menu:
    if( command == "FileOpen" )
      {
      openFile();
      return;
      }

    if( command == "FileSaveAll" )
      {
      saveAllFiles();
      return;
      }

    if( command == "FileCloseCurrent" )
      {
      closeCurrentFile();
      return;
      }

    if( command == "FileExit" )
      {
      System.exit( 0 );
      // return;
      }


    /////////////
    // Edit Menu:

    if( command == "EditCopy" )
      {
      editCopy();
      return;
      }

    if( command == "EditCut" )
      {
      editCut();
      return;
      }

    if( command == "EditPaste" )
      {
      editPaste();
      return;
      }

/*
    if( command == "EditSelectAll" )
      {
      editSelectAll();
      return;
      }
*/

    ///////////////
    // Project Menu:
    if( command == "ProjectSetCurrent" )
      {
      setCurrentProject();
      return;
      }

// =======
// Build and run...
// java.lang.Runtime
// Runtime.exec( "Command" );



    //////////////
    // Help Menu:
    if( command == "HelpAbout" )
      {
      showAboutBox();
      return;
      }

    }
    catch( Exception e )
      {
      showStatus( "Exception in actionPerformed()." );
      showStatus( e.getMessage() );
      }
    }



  private void keyboardTimerEvent()
    {
    try
    {
    // showStatus( "keyboardTimerEvent called." );
    // keyboardTimer.stop();

    if( windowIsClosing )
      {
      keyboardTimer.stop();
      return;
      }

    int selectedIndex = mainTabbedPane.getSelectedIndex();

    // The status page is at zero.
    if( selectedIndex == 0 )
      {
      statusLabel.setText( "Status page." );
      return;
      }

    if( selectedIndex < 1 )
      {
      statusLabel.setText( "No text area selected." );
      return;
      }

    if( selectedIndex >= tabPagesArrayLast )
      {
      statusLabel.setText( "No text area selected." );
      return;
      }

    String tabTitle = tabPagesArray[selectedIndex].getTabTitle();

    JTextArea selectedTextArea = getSelectedTextArea();
    if( selectedTextArea == null )
      return;

    int position = selectedTextArea.getCaretPosition();

    int line = selectedTextArea.getLineOfOffset( position );

    // The +1 is for display and matching with
    // the compiler error line number.
    line++;

    String showText = "Line: " + line +
                    "     " + tabTitle +
                    "      Proj: " + showProjectText;

    statusLabel.setText( showText );

    // keyboardTimer.start();
    }
    catch( Exception e )
      {
      showStatus( "Exception in keyboardTimerEvent()." );
      showStatus( e.getMessage() );
      }
    }



  private boolean fileIsInTabPages( String fileName )
    {
    for( int count = 0; count < tabPagesArrayLast; count++ )
      {
      String fileAtTab = tabPagesArray[count].getFileName();
      if( fileAtTab.compareToIgnoreCase( fileName ) == 0 )
        return true;

      }

    return false;
    }



  private void editCopy()
    {
    try
    {
    JTextArea selectedTextArea = getSelectedTextArea();
    if( selectedTextArea == null )
      return;

    selectedTextArea.copy();
    }
    catch( Exception e )
      {
      showStatus( "Exception in editCopy()." );
      showStatus( e.getMessage() );
      }
    }



  private void editCut()
    {
    try
    {
    JTextArea selectedTextArea = getSelectedTextArea();
    if( selectedTextArea == null )
      return;

    selectedTextArea.cut();
    }
    catch( Exception e )
      {
      showStatus( "Exception in editCut()." );
      showStatus( e.getMessage() );
      }
    }



  private void editPaste()
    {
    try
    {
    JTextArea selectedTextArea = getSelectedTextArea();
    if( selectedTextArea == null )
      return;

    selectedTextArea.paste();
    }
    catch( Exception e )
      {
      showStatus( "Exception in editPaste()." );
      showStatus( e.getMessage() );
      }
    }



  /*
  private void editSelectAll()
    {
    try
    {
    JTextArea selectedTextArea = getSelectedTextArea();
    if( selectedTextArea == null )
      return;

    selectedTextArea.selectAll();
    }
    catch( Exception e )
      {
      showStatus( "Exception in editSelectAll()." );
      showStatus( e.getMessage() );
      }
    }
    */



  private void showAboutBox()
    {
    JOptionPane.showMessageDialog( this,
                 "Programming by Eric Chauvin.  Version date: " + MainApp.versionDate );

    }



  private JTextArea getSelectedTextArea()
    {
    if( tabPagesArrayLast < 2 )
      return null;

    int selectedIndex = mainTabbedPane.getSelectedIndex();
    if( selectedIndex < 1 )
      return null;

    if( selectedIndex >= tabPagesArrayLast )
      return null;

    JTextArea selectedTextArea = tabPagesArray[
                       selectedIndex].getTextArea();

    return selectedTextArea;
    }




  private void setShowProjectText()
    {
    String showS = mApp.projectConfigFile.getString( "ProjectDirectory" );

    showS = showS.replace( "C:\\Eric\\", "" );
    showProjectText = showS;
    }




  private void setCurrentProject()
    {
    String fileToOpen = "";

    try
    {
    final JFileChooser fc = new JFileChooser();

    try
    {
    File dir = new File( "c:\\Eric\\" );
    fc.setCurrentDirectory( dir );
    }
    catch( Exception e )
      {
      showStatus( "Couldn't set the directory for the file chooser." );
      showStatus( e.getMessage() );
      }

    FileFilter filter = new FileNameExtensionFilter( "Text file", "txt" );
    // fc.addChoosableFileFilter( filter );
    fc.setFileFilter( filter );

    int returnVal = fc.showOpenDialog( this );
    if( returnVal != JFileChooser.APPROVE_OPTION )
      return;

    // https://docs.oracle.com/javase/7/docs/api/java/nio/file/package-summary.html
    // https://docs.oracle.com/javase/7/docs/api/java/io/File.html
    File file = fc.getSelectedFile();
    String fileName = file.getName();
    showStatus( "Project file name picked is: " + fileName );

    String pathName = file.getPath();
    showStatus( "Project path name picked is: " + pathName );

    if( !pathName.toLowerCase().contains( ".txt" ))
      {
      JOptionPane.showMessageDialog( this,
                 "The file should be a .txt file." );

      return;
      }

    if( !file.exists())
      {
      JOptionPane.showMessageDialog( this,
                 "The file doesn't exist." );

      return;
      }

    String projectFileName = pathName;

    if( projectFileName.length() < 4 )
      {
      JOptionPane.showMessageDialog( this,
                 "Pick a file name." );

      return;
      }

    Path path = Paths.get( projectFileName );

    Path parentDir = path.getParent();
    if( parentDir == null )
      {
      JOptionPane.showMessageDialog( this,
                 "The file name has no parent directory." );

      return;
      }

    String workingDir = parentDir.toString();
    showStatus( "workingDir is: " + workingDir );

    mApp.mainConfigFile.setString( "CurrentProjectFile",
                               projectFileName, true );

    mApp.projectConfigFile = new ConfigureFile(
                                  mApp, projectFileName );

    mApp.projectConfigFile.setString( "ProjectDirectory",
                                         workingDir, false );

    // openRecentFiles() clears any existing files first.
    openRecentFiles();

    setShowProjectText();
    }
    catch( Exception e )
      {
      String showS = "Exception with opening project file.\n" +
                     "Entered: " + fileToOpen;

      showStatus( showS );
      showStatus( e.getMessage() );
      }
    }




  }
