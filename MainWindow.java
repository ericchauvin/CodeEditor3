// Copyright Eric Chauvin 2019 - 2020.




import javax.swing.JFrame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowStateListener;
import java.awt.Container;
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
import java.lang.ProcessBuilder.Redirect;



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
  // private Font menuFont;
  private JTabbedPane mainTabbedPane;
  private JLabel statusLabel;
  private JTextArea statusTextArea;
  private EditorTabPage[] tabPagesArray;
  private int tabPagesArrayLast = 0;
  private String showProjectText = "";
  private String searchText = "";
  private String statusFileName = "";
  private final int maximumTabsOpen = 30;
  private Timer keyboardTimer;
  private boolean windowIsClosing = false;
  private Process buildProcess = null;
  private int buildTimerCount = 0;
  private int exeTimerCount = 0;
  private Process executableProcess = null;



  private MainWindow()
    {
    }



  public MainWindow( MainApp useApp, String showText )
    {
    super( showText );

    mApp = useApp;

    tabPagesArray = new EditorTabPage[2];

    mainFont = new Font( Font.MONOSPACED, Font.PLAIN, 40 );
    // menuFont = new Font( Font.MONOSPACED, Font.PLAIN, 40 );

    setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );

    setSize( 1200, 600 );
    addComponents( getContentPane() );
    // pack();
    // setExtendedState( JFrame.MAXIMIZED_BOTH );

    setupMenus();

    addWindowListener( this );
    addWindowFocusListener( this );
    addWindowStateListener( this );

    // Center it.
    setLocationRelativeTo( null );
    setVisible( true );
    }


  // Do this after the constructor has returned.
  public void initialize()
    {
    openRecentFiles();

    showStatusTab();
    setShowProjectText();
    setupTimer();
    showStatus( "Programming by Eric Chauvin." );
    showStatus( "Version date: " + MainApp.versionDate );
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
                   100, LayoutSimpleVertical.FixedHeightMax ));

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

    mainTabbedPane.setFont( mainFont );
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
                 "Stat",
                 statusFileName,
                 true );

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
      showStatusTab();
      showStatus( "Couldn't allocate a new array for the EditorTabPage array." );
      showStatus( e.getMessage() );
      return in;
      }

    return newArray;
    }



  private void setTabLabel( String title, int index )
    {
    JLabel tabLabel = new JLabel( title );
    tabLabel.setOpaque( true );
    tabLabel.setForeground( Color.white );
    tabLabel.setBackground( Color.black );
    tabLabel.setFont( mainFont );
    mainTabbedPane.setTabComponentAt( index, tabLabel );
    }



  private void addTextPane( JTextArea textAreaToUse,
                            String tabTitle,
                            String fileName,
                            boolean wordWrap )
    {
    JTextArea textArea1;
    if( textAreaToUse == null )
      textArea1 = new JTextArea();
    else
      textArea1 = textAreaToUse;

    textArea1.setDragEnabled( false );

    textArea1.setFont( mainFont );

    if( wordWrap )
      {
      textArea1.setLineWrap( true );
      textArea1.setWrapStyleWord( true );
      }
    else
      {
      textArea1.setLineWrap( false );
      }

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

    setTabLabel( tabTitle, tabPagesArrayLast );

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
    fileMenu.setFont( mainFont );
    fileMenu.setForeground( Color.white );
    menuBar.add( fileMenu );

    JMenuItem menuItem = new JMenuItem( "Open" );
    menuItem.setMnemonic( KeyEvent.VK_O );
    menuItem.setActionCommand( "FileOpen" );
    menuItem.addActionListener( this );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    fileMenu.add( menuItem );

    menuItem = new JMenuItem( "Save File As", KeyEvent.VK_A );
    menuItem.setActionCommand( "FileSaveAs" );
    menuItem.addActionListener( this );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    fileMenu.add( menuItem );


    menuItem = new JMenuItem( "Save All", KeyEvent.VK_L );
    menuItem.setActionCommand( "FileSaveAll" );
    menuItem.addActionListener( this );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    fileMenu.add( menuItem );


    menuItem = new JMenuItem( "New File" );
    menuItem.setMnemonic( KeyEvent.VK_N );
    menuItem.setActionCommand( "FileNew" );
    menuItem.addActionListener( this );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    fileMenu.add( menuItem );


    menuItem = new JMenuItem( "Close Current" );
    menuItem.setMnemonic( KeyEvent.VK_U );
    menuItem.setActionCommand( "FileCloseCurrent" );
    menuItem.addActionListener( this );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    fileMenu.add( menuItem );


    menuItem = new JMenuItem( "Exit" );
    menuItem.setMnemonic( KeyEvent.VK_X );
    menuItem.setActionCommand( "FileExit" );
    menuItem.addActionListener( this );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    fileMenu.add( menuItem );


    ///////////////////////
    // Edit Menu:
    JMenu editMenu = new JMenu( "Edit" );
    editMenu.setMnemonic( KeyEvent.VK_E );
    editMenu.setForeground( Color.white );
    editMenu.setFont( mainFont );
    menuBar.add( editMenu );

    menuItem = new JMenuItem( "Copy" );
    menuItem.setMnemonic( KeyEvent.VK_C );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menuItem.setActionCommand( "EditCopy" );
    menuItem.addActionListener( this );
    editMenu.add( menuItem );

    menuItem = new JMenuItem( "Cut" );
    menuItem.setMnemonic( KeyEvent.VK_T );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menuItem.setActionCommand( "EditCut" );
    menuItem.addActionListener( this );
    editMenu.add( menuItem );

    menuItem = new JMenuItem( "Paste" );
    menuItem.setMnemonic( KeyEvent.VK_P );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menuItem.setActionCommand( "EditPaste" );
    menuItem.addActionListener( this );
    editMenu.add( menuItem );

    menuItem = new JMenuItem( "Find" );
    menuItem.setMnemonic( KeyEvent.VK_F );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menuItem.setActionCommand( "EditFind" );
    menuItem.addActionListener( this );
    editMenu.add( menuItem );

    menuItem = new JMenuItem( "Find Next" );
    menuItem.setMnemonic( KeyEvent.VK_N );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menuItem.setActionCommand( "EditFindNext" );
    menuItem.addActionListener( this );
    editMenu.add( menuItem );

    menuItem = new JMenuItem( "Remove Comments" );
    menuItem.setMnemonic( KeyEvent.VK_R );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menuItem.setActionCommand( "EditRemoveComments" );
    menuItem.addActionListener( this );
    editMenu.add( menuItem );


    ///////////////////////
    // Project Menu:
    JMenu projectMenu = new JMenu( "Project" );
    projectMenu.setMnemonic( KeyEvent.VK_P );
    projectMenu.setForeground( Color.white );
    projectMenu.setFont( mainFont );
    menuBar.add( projectMenu );

    menuItem = new JMenuItem( "Set Current Project" );
    menuItem.setMnemonic( KeyEvent.VK_C );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menuItem.setActionCommand( "ProjectSetCurrent" );
    menuItem.addActionListener( this );
    projectMenu.add( menuItem );

    menuItem = new JMenuItem( "Set Executable" );
    menuItem.setMnemonic( KeyEvent.VK_X );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menuItem.setActionCommand( "ProjectSetExecutable" );
    menuItem.addActionListener( this );
    projectMenu.add( menuItem );

    menuItem = new JMenuItem( "Run Executable" );
    menuItem.setMnemonic( KeyEvent.VK_R );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menuItem.setActionCommand( "ProjectRunExecutable" );
    menuItem.addActionListener( this );
    projectMenu.add( menuItem );

    menuItem = new JMenuItem( "Set Build File" );
    menuItem.setMnemonic( KeyEvent.VK_S );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menuItem.setActionCommand( "ProjectSetBuildFile" );
    menuItem.addActionListener( this );
    projectMenu.add( menuItem );

    menuItem = new JMenuItem( "Build" );
    menuItem.setMnemonic( KeyEvent.VK_B );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menuItem.setActionCommand( "ProjectRunBuildFile" );
    menuItem.addActionListener( this );
    projectMenu.add( menuItem );

    menuItem = new JMenuItem( "Show Build Log" );
    menuItem.setMnemonic( KeyEvent.VK_L );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menuItem.setActionCommand( "ProjectShowBuildLog" );
    menuItem.addActionListener( this );
    projectMenu.add( menuItem );



    ///////////////////////
    // Keyboard Menu:
    JMenu keyBoardMenu = new JMenu( "Keyboard" );
    keyBoardMenu.setMnemonic( KeyEvent.VK_K );
    keyBoardMenu.setForeground( Color.white );
    keyBoardMenu.setFont( mainFont );
    menuBar.add( keyBoardMenu );

    menuItem = new JMenuItem( "Next Tab" );
    menuItem.setMnemonic( KeyEvent.VK_T );
    // ALT_MASK  CTRL_MASK  SHIFT_MASK
    menuItem.setAccelerator( KeyStroke.getKeyStroke(
                     KeyEvent.VK_T, ActionEvent.CTRL_MASK ));

    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menuItem.setActionCommand( "KeyboardNextTab" );
    menuItem.addActionListener( this );
    keyBoardMenu.add( menuItem );


    ///////////////////////
    // Help Menu:
    JMenu helpMenu = new JMenu( "Help" );
    helpMenu.setMnemonic( KeyEvent.VK_H );
    helpMenu.setForeground( Color.white );
    helpMenu.setFont( mainFont );
    menuBar.add( helpMenu );

    menuItem = new JMenuItem( "About" );
    menuItem.setMnemonic( KeyEvent.VK_A );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menuItem.setActionCommand( "HelpAbout" );
    menuItem.addActionListener( this );
    helpMenu.add( menuItem );

    menuItem = new JMenuItem( "Show Non-ASCII" );
    menuItem.setMnemonic( KeyEvent.VK_N );
    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menuItem.setActionCommand( "HelpShowNonASCII" );
    menuItem.addActionListener( this );
    helpMenu.add( menuItem );

    setJMenuBar( menuBar );
    }




  public void actionPerformed( ActionEvent event )
    {
    try
    {
    // String paramS = event.paramString();

    String command = event.getActionCommand();

    if( command == null )
      {
      keyboardTimerEvent();
      return;
      }

    // showStatus( "ActionEvent Command is: " + command );

    //////////////
    // File Menu:
    if( command == "FileOpen" )
      {
      openFile();
      return;
      }

    if( command == "FileSaveAs" )
      {
      saveFileAs();
      return;
      }

    if( command == "FileSaveAll" )
      {
      saveAllFiles();
      return;
      }

    if( command == "FileNew" )
      {
      addNewFile();
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

    if( command == "EditFind" )
      {
      findText();
      return;
      }

    if( command == "EditFindNext" )
      {
      findTextNext();
      return;
      }

    if( command == "EditRemoveComments" )
      {
      removeStarComments();
      return;
      }


    ///////////////
    // Project Menu:
    if( command == "ProjectSetCurrent" )
      {
      setCurrentProject();
      return;
      }

    if( command == "ProjectSetExecutable" )
      {
      setProjectExecutable();
      return;
      }

    if( command == "ProjectRunExecutable" )
      {
      runExecutableFile();
      return;
      }

    if( command == "ProjectSetBuildFile" )
      {
      setProjectBuildFile();
      return;
      }

    if( command == "ProjectRunBuildFile" )
      {
      runBuildFile();
      return;
      }

    if( command == "ProjectShowBuildLog" )
      {
      showBuildLog();
      return;
      }


    ///////////////
    // Keyboard Menu:
    if( command == "KeyboardNextTab" )
      {
      moveToNextTab();
      return;
      }

    //////////////
    // Help Menu:
    if( command == "HelpAbout" )
      {
      showAboutBox();
      return;
      }

    if( command == "HelpShowNonASCII" )
      {
      showNonAsciiCharacters();
      return;
      }

    }
    catch( Exception e )
      {
      showStatusTab();
      showStatus( "Exception in actionPerformed()." );
      showStatus( e.getMessage() );
      }
    }



  private void runBuildFile()
    {
    try
    {
    showStatusTab();

    if( buildProcess != null )
      {
      showStatus( "The build file is already running." );
      return;
      }

    String runFile = mApp.projectConfigFile.getString(
                                          "BuildFile" );

    showStatus( "Running Build File: " + runFile );

    // java.lang.Runtime
    // java.lang.ProcessBuilder
    // java.lang.Process

    buildTimerCount = 0;
    buildProcess = Runtime.getRuntime().exec( runFile );
    if( buildProcess == null )
      {
      showStatus( "exec() returned null for the Build Process." );
      return;
      }

    showStatus( "Started Build File." );

    }
    catch( Exception e )
      {
      showStatusTab();
      showStatus( "Exception in runBuildFile()." );
      showStatus( e.getMessage() );
      }
    }




  private void runExecutableFile()
    {
    try
    {
    showStatusTab();

    if( executableProcess != null )
      {
      showStatus( "The executable process is already running." );
      return;
      }

    String runFile = mApp.projectConfigFile.getString(
                                         "ExecutableFile" );
    showStatus( "Running Exec File: " + runFile );

    // java.lang.Runtime
    // java.lang.ProcessBuilder
    // java.lang.Process

    ProcessBuilder pb = new ProcessBuilder( runFile,
                                            "" );
    // pb.directory(new File("myDir"));

    // Make sure to redirect the executable's output within
    // the RunApp.bat batch file.
    // See the showExeOutput() function.

    // File log = new File( "\\Eric\\CodeAnalysisCpp\\ATestLog.txt" );
    // pb.redirectErrorStream( true );
    // pb.redirectOutput( Redirect.appendTo( log ));
    executableProcess = pb.start();
    showStatus( "Started executable File." );
    }
    catch( Exception e )
      {
      showStatusTab();
      showStatus( "Exception in runExecutableFile()." );
      showStatus( e.getMessage() );
      }
    }




  private void setupTimer()
    {
    int delay = 100;
    keyboardTimer = new Timer( delay, this );
    keyboardTimer.start();
    }




  private void openFile()
    {
    final JFileChooser fc = new JFileChooser();
    try
    {
    File dir = new File( "c:\\Eric\\" );
    fc.setCurrentDirectory( dir );
    }
    catch( Exception e )
      {
      showStatusTab();
      showStatus( "Couldn't set the directory for the file chooser." );
      showStatus( e.getMessage() );
      }

    // FileFilter filter = new FileNameExtensionFilter( "Text file", "txt" );
    // fc.setFileFilter( filter );
    // fc.addChoosableFileFilter( filter );

    int returnVal = fc.showOpenDialog( this );
    if( returnVal != JFileChooser.APPROVE_OPTION )
      return;


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

    addTextPane( null, fileName, pathName, false );
    if( tabPagesArrayLast > 1 )
      {
      int index = tabPagesArrayLast - 1;
      mainTabbedPane.setSelectedIndex( index );
      setTabbedTextArea( index );
      }
    }



  private void openRecentFiles()
    {
    try
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

      addTextPane( null, tabTitle, fileName, false );
      }
    }
    catch( Exception e )
      {
      showStatus( "Exception in openRecentFiles()." );
      showStatus( e.getMessage() );
      }
    }



  private void showStatusTab()
    {
    if( tabPagesArrayLast < 1 )
      return;

    mainTabbedPane.setSelectedIndex( 0 );
    setTabbedTextArea( 0 );
    }




  private void saveAllFiles()
    {
    if( tabPagesArrayLast < 1 )
      return;

    // showStatusTab();

    for( int count = 1; count < tabPagesArrayLast; count++ )
      {
      tabPagesArray[count].writeToTextFile();

      String fileName = tabPagesArray[count].getFileName();
      mApp.projectConfigFile.setString( "RecentFile" + count, fileName, false );
      }

    // showStatus( "Saving project config file." );
    mApp.projectConfigFile.writeToTextFile();
    // showStatus( "Saving main config file." );
    mApp.mainConfigFile.writeToTextFile();
    showStatus( "Saved all files." );
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


/*
  private void readFromExecutableOutput()
    {
    try
    {
    if( executableInputStream == null )
      return;

    StringBuilder sBuilder = new StringBuilder();
    int oneByte = 0;
    for( int count = 0; count < 1000; count++ )
      {
      try
      {
      if( executableInputStream.available() < 1 )
        break;

      // This will block until it has something to read.
      oneByte = executableInputStream.read();
      }
      catch( Exception e )
        {
        executableInputStream = null;
        showStatus( "Exception while doing read() in readFromExecutableOutput()." );
        showStatus( e.getMessage() );
        }

      if( oneByte < 0 )
        break;

      if( oneByte >= 127 )
        {
        showStatus( "The byte was >= 127." );
        }

      char oneChar = (char)oneByte;
      if( oneChar != '\n' )
        {
        if( oneChar < ' ' )
          {
          oneChar = ' ';
          showStatus( "oneChar was less than space." );
          }
        }

      sBuilder.append( oneChar );
      }

    String toShow = sBuilder.toString();
    if( toShow.length() > 0 )
      showStatus( toShow );

    }
    catch( Exception e )
      {
      showStatus( "Exception in readFromExecutableOutput()." );
      showStatus( e.getMessage() );
      }
    }
*/




  private void checkBuildProcess()
    {
    if( buildProcess == null )
      return;

    boolean buildHasReturned = false;
    try
    {
    int returnVal = buildProcess.exitValue();
    buildHasReturned = true;
    }
    catch( IllegalThreadStateException e )
      {
      // Make sure there is no "pause" at the end
      // of the batch file.
      // It has not yet terminated.
      buildTimerCount++;
      if( (buildTimerCount % 4) == 1 )
        showStatus( "Build process is running. " + buildTimerCount );

      // Kill the process.
      // buildProcess.destroy();
      }

    if( buildHasReturned )
      {
      buildProcess = null;
      showBuildLog();
      }
    }




  private void checkExecutableProcess()
    {
    if( executableProcess == null )
      return;

    boolean hasReturned = false;
    try
    {
    int returnVal = executableProcess.exitValue();
    hasReturned = true;
    }
    catch( IllegalThreadStateException e )
      {
      // Make sure there is no "pause" at the end
      // of the batch file.
      // It has not yet terminated.
      exeTimerCount++;
      // if( (exeTimerCount % 4) == 1 )
        // showStatus( "Executable process is running. " + exeTimerCount );

      }

    if( hasReturned )
      {
      executableProcess = null;
      showStatus( "Executable has finished." );
      showExeOutput();
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

    checkBuildProcess();
    checkExecutableProcess();

    // readFromExecutableOutput();

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
                    "   " + tabTitle +
                    "    Proj: " + showProjectText;

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

    selectedTextArea.setSelectionEnd( selectedTextArea.
                                    getSelectionStart());

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
    String showS = "Programming by Eric Chauvin." +
                   "  Version date: " +
                   MainApp.versionDate;

    InfoDialog.showInfo( this,
                         mApp,
                         mainFont,
                         showS,
                         400, // width
                         350 ); // height

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



  private void setTabbedTextArea( int selectedIndex )
    {
    try
    {
    if( selectedIndex < 0 )
      return;

    if( selectedIndex >= tabPagesArrayLast )
      return;

    JTextArea selectedTextArea = tabPagesArray[
                           selectedIndex].getTextArea();

    selectedTextArea.requestFocusInWindow();
    }
    catch( Exception e )
      {
      showStatus( "Exception in setTabbedTextArea()." );
      showStatus( e.getMessage() );
      }
    }



  private void moveToNextTab()
    {
    try
    {
    if( tabPagesArrayLast < 1 )
      return;

    if( tabPagesArrayLast < 2 )
      {
      mainTabbedPane.setSelectedIndex( 0 );
      setTabbedTextArea( 0 );
      return;
      }

    int selectedIndex = mainTabbedPane.getSelectedIndex();
    if( selectedIndex < 0 )
      {
      mainTabbedPane.setSelectedIndex( 0 );
      setTabbedTextArea( 0 );
      return;
      }

    if( (selectedIndex + 1) >= tabPagesArrayLast )
      {
      mainTabbedPane.setSelectedIndex( 0 );
      setTabbedTextArea( 0 );
      return;
      }

    selectedIndex++;
    mainTabbedPane.setSelectedIndex( selectedIndex );
    setTabbedTextArea( selectedIndex );
    }
    catch( Exception e )
      {
      showStatus( "Exception in moveToNextTab()." );
      showStatus( e.getMessage() );
      }
    }




  private void showNonAsciiCharacters()
    {
    /*

    Symbols:
        General Punctuation (2000206F)
        Superscripts and Subscripts (2070209F)
        Currency Symbols (20A020CF)
        Combining Diacritical Marks for Symbols (20D020FF)
        Letterlike Symbols (2100214F)
        Number Forms (2150218F)
        Arrows (219021FF)
        Mathematical Operators (220022FF)
        Miscellaneous Technical (230023FF)
        Control Pictures (2400243F)
        Optical Character Recognition (2440245F)
        Enclosed Alphanumerics (246024FF)
        Box Drawing (2500257F)
        Block Elements (2580259F)
        Geometric Shapes (25A025FF)
        Miscellaneous Symbols (260026FF)
        Dingbats (270027BF)
        Miscellaneous Mathematical Symbols-A (27C027EF)
        Supplemental Arrows-A (27F027FF)
        Braille Patterns (280028FF)
        Supplemental Arrows-B (2900297F)
        Miscellaneous Mathematical Symbols-B (298029FF)
        Supplemental Mathematical Operators (2A002AFF)
        Miscellaneous Symbols and Arrows (2B002BFF)
    */

    //    Dingbats (2700 27BF)

    for( int count = 0x2700; count < 0x27BF; count++ )
      {
      int where = count - 0x2700;
      showStatus( "" + where + ") " + Character.
                                toString( (char)count ));

      }

    // for( int Count = 128; Count < 256; Count++ )
      // ShowStatus( "      case (int)'" + Char.ToString( (char)Count ) + "': return " + Count.ToString( "X4" ) + ";" );


    // for( int Count = 32; Count < 256; Count++ )
      // ShowStatus( "    CharacterArray[" + Count.ToString() + "] = '" + Char.ToString( (char)Count ) + "';  //  0x" + Count.ToString( "X2" ) );

     // &#147;

    // ShowStatus( " " );

    // int getVal = 0x252F;
    // showStatus( "Character: " + (char)getVal );
    }




  private void saveFileAs()
    {
    try
    {
    int selectedIndex = mainTabbedPane.getSelectedIndex();
    if( selectedIndex >= tabPagesArray.length )
      {
      showStatusTab();
      showStatus( "The selected index is past the TabPagesArray length." );
      return;
      }

    // The status page is at zero.
    if( selectedIndex <= 0 )
      {
      showStatusTab();
      showStatus( "There is no tab page selected, or the status page is selected. (Top.)" );
      return;
      }

    final JFileChooser fc = new JFileChooser();

    try
    {
    File dir = new File( "c:\\Eric\\" );
    fc.setCurrentDirectory( dir );
    }
    catch( Exception e )
      {
      showStatusTab();
      showStatus( "Couldn't set the directory for the file chooser." );
      showStatus( e.getMessage() );
      }

    // FileFilter filter = new FileNameExtensionFilter( "Text file", "txt" );
    // fc.addChoosableFileFilter( filter );
    // fc.setFileFilter( filter );

    int returnVal = fc.showSaveDialog( this );
    if( returnVal != JFileChooser.APPROVE_OPTION )
      return;

    // https://docs.oracle.com/javase/7/docs/api/java/nio/file/package-summary.html
    // https://docs.oracle.com/javase/7/docs/api/java/io/File.html
    File file = fc.getSelectedFile();
    String fileName = file.getName();
    showStatus( "File name picked is: " + fileName );
    String pathName = file.getPath();
    showStatus( "Path name picked is: " + pathName );

    // if( file.exists())

    if( pathName.length() < 4 )
      {
      // JOptionPane.showMessageDialog( this,
      //            "Pick a file name." );

      return;
      }

    // Path path = Paths.get( pathName );

    tabPagesArray[selectedIndex].setTabTitle( fileName );
    tabPagesArray[selectedIndex].setFileName( pathName );
    tabPagesArray[selectedIndex].writeToTextFile();
    setTabLabel( fileName, selectedIndex );

    mApp.projectConfigFile.setString( "RecentFile" + selectedIndex, tabPagesArray[selectedIndex].getFileName(), true );

    }
    catch( Exception e )
      {
      showStatus( "Exception in saveFileAs()." );
      showStatus( e.getMessage() );
      }
    }





  private String showEditTextDialog()
    {
    EditTextDialog editD = new EditTextDialog( this,
                                       mApp,
                                       mainFont );

    editD.setVisible( true );
    String gotText = editD.getText();
    // showStatus( "gotText: " + gotText );
    // showStatus( "Before dispose." );
    editD.dispose();
    // showStatus( "After dispose() was called." );
    return gotText;
    }




  private void removeStarComments()
    {
    try
    {
    int selectedIndex = mainTabbedPane.getSelectedIndex();
    if( (selectedIndex < 1) ||
        (selectedIndex >= tabPagesArrayLast))
      {
      showStatusTab();
      showStatus( "No tab page selected." );
      return;
      }

    JTextArea selectedTextArea = getSelectedTextArea();
    if( selectedTextArea == null )
      {
      showStatusTab();
      showStatus( "No text area selected." );
      return;
      }

    String textS = selectedTextArea.getText();
    textS = RemoveStarComments.removeComments( mApp,
                                               textS );

    selectedTextArea.setText( textS );
    }
    catch( Exception e )
      {
      showStatusTab();
      showStatus( "Exception in removeStarComments()." );
      showStatus( e.getMessage() );
      }
    }




  private void findText()
    {
    try
    {
    int selectedIndex = mainTabbedPane.getSelectedIndex();
    if( (selectedIndex < 1) ||
        (selectedIndex >= tabPagesArrayLast))
      {
      showStatusTab();
      showStatus( "No tab page selected." );
      return;
      }

    String editedText = showEditTextDialog();
    // showStatus( "EditedText: " + editedText );

    if( editedText == null )
      return;

    searchText = editedText.trim().toLowerCase();
    if( searchText.length() < 1 )
      return;

    // showStatus( "Search text: " + searchText );

    findTextNext();
    }
    catch( Exception e )
      {
      showStatusTab();
      showStatus( "Exception in findText()." );
      showStatus( e.getMessage() );
      }
    }




  private int searchTextMatches( int position,
                                 String textToSearch,
                                 String searchText )
    {
    int sLength = searchText.length();
    if( sLength < 1 )
      return -1;

    if( (position + sLength - 1) >= textToSearch.length() )
      return -1;

    for( int count = 0; count < sLength; count++ )
      {
      if( searchText.charAt( count ) != textToSearch.
                            charAt( position + count ) )
        return -1;

      }

    return position;
    }




  private void findTextNext()
    {
    try
    {
    int selectedIndex = mainTabbedPane.getSelectedIndex();
    if( (selectedIndex < 1) ||
        (selectedIndex >= tabPagesArrayLast ))
      {
      showStatusTab();
      showStatus( "No tab page selected." );
      return;
      }

    if( searchText.length() < 1 )
      {
      showStatusTab();
      showStatus( "There is no searchText." );
      return;
      }

    // showStatus( "Search text: " + searchText );

    JTextArea selectedTextArea = getSelectedTextArea();
    if( selectedTextArea == null )
      {
      showStatusTab();
      showStatus( "No text area selected." );
      return;
      }

    int start = selectedTextArea. getCaretPosition();
    if( start < 0 )
      start = 0;

    String textS = selectedTextArea.getText().toLowerCase();
    int textLength = textS.length();
    for( int count = start; count < textLength; count++ )
      {
      if( textS.charAt( count ) == searchText.charAt( 0 ) )
        {
        int where = searchTextMatches( count,
                                       textS,
                                       searchText );
        if( where >= 0 )
          {
          // showStatus( "Found at: " + where );
          selectedTextArea. setCaretPosition( where );
          return;
          }
        }
      }

    showStatusTab();
    showStatus( "Nothing found." );
    }
    catch( Exception e )
      {
      showStatusTab();
      showStatus( "Exception in findTextNext()." );
      showStatus( e.getMessage() );
      }
    }




  private void showBuildLog()
    {
    clearStatus();

    String fileName = mApp.projectConfigFile.getString( "ProjectDirectory" );
    fileName += "\\Build.log";

    showStatus( "Log file: " + fileName );
    // BuildLog Log = new BuildLog( FileName, this );
    // Log.ReadFromTextFile();

    String fileS = FileUtility.readFileToString( mApp,
                                                      fileName,
                                                      false );

    if( fileS == "" )
      {
      showStatus( "Nothing in the log file." );
      return;
      }

    fileS = fileS.trim();
    fileS = fileS + "\n";

    StringBuilder sBuilder = new StringBuilder();
    String[] lines = fileS.split( "\n" );
    for( int count = 0; count < lines.length; count++ )
      {
      showStatus( lines[count] );
      }
    }



  private void showExeOutput()
    {
    clearStatus();

    String fileName = mApp.projectConfigFile.getString( "ProjectDirectory" );
    fileName += "\\ExeOut.txt";

    String fileS = FileUtility.readFileToString( mApp,
                                                 fileName,
                                                 false );

    if( fileS == "" )
      {
      showStatus( "Nothing in the exe out file." );
      return;
      }

    fileS = fileS.trim();
    fileS = fileS + "\n";

    StringBuilder sBuilder = new StringBuilder();
    String[] lines = fileS.split( "\n" );
    for( int count = 0; count < lines.length; count++ )
      {
      showStatus( lines[count] );
      }
    }



  private void setProjectExecutable()
    {
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
      showStatusTab();
      showStatus( "Couldn't set the directory for the file chooser." );
      showStatus( e.getMessage() );
      }

    FileFilter filter = new FileNameExtensionFilter(
                                      "Batch file", "bat" );

    fc.setFileFilter( filter );

    filter = new FileNameExtensionFilter(
                                      "Executable", "exe" );

    fc.addChoosableFileFilter( filter );

    int returnVal = fc.showOpenDialog( this );
    if( returnVal != JFileChooser.APPROVE_OPTION )
      return;

    // https://docs.oracle.com/javase/7/docs/api/java/nio/file/package-summary.html
    // https://docs.oracle.com/javase/7/docs/api/java/io/File.html
    File file = fc.getSelectedFile();
    String fileName = file.getName();
    showStatus( "File name picked is: " + fileName );

    String pathName = file.getPath();
    showStatus( "Path name picked is: " + pathName );

    mApp.projectConfigFile.setString( "ExecutableFile", pathName, true );
    showStatus( "Exec File: " + mApp.projectConfigFile.getString( "ExecutableFile" ));
    }
    catch( Exception e )
      {
      showStatusTab();
      showStatus( "Exception with naming exec file." );
      showStatus( e.getMessage() );
      }
    }



  private void setProjectBuildFile()
    {
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
      showStatusTab();
      showStatus( "Couldn't set the directory for the file chooser." );
      showStatus( e.getMessage() );
      }

    FileFilter filter = new FileNameExtensionFilter(
                                      "Batch file", "bat" );

    fc.setFileFilter( filter );

    filter = new FileNameExtensionFilter(
                                      "Executable", "exe" );

    fc.addChoosableFileFilter( filter );

    int returnVal = fc.showOpenDialog( this );
    if( returnVal != JFileChooser.APPROVE_OPTION )
      return;

    // https://docs.oracle.com/javase/7/docs/api/java/nio/file/package-summary.html
    // https://docs.oracle.com/javase/7/docs/api/java/io/File.html
    File file = fc.getSelectedFile();
    String fileName = file.getName();
    showStatus( "File name picked is: " + fileName );

    String pathName = file.getPath();
    showStatus( "Path name picked is: " + pathName );

    mApp.projectConfigFile.setString( "BuildFile", pathName, true );
    showStatus( "Build File: " + mApp.projectConfigFile.getString( "BuildFile" ));
    }
    catch( Exception e )
      {
      showStatusTab();
      showStatus( "Exception with naming Build file." );
      showStatus( e.getMessage() );
      }
    }




  private void addNewFile()
    {
    String tabTitle = "No Name";
    String fileName = "";
    addTextPane( null, tabTitle, fileName, false );

    if( tabPagesArrayLast > 1 )
      {
      int index = tabPagesArrayLast - 1;
      mainTabbedPane.setSelectedIndex( index );
      setTabbedTextArea( index );
      }
    }




  }
