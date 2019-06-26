// Copyright Eric Chauvin 2019.


// black, blue, cyan, darkGray, gray, green, lightGray,
// magenta, orange, pink, red, white, yellow.

    // Rectangle Rec = pane.getBounds();
    // Rec.height
    // Rec.width
    // Rec.x
    // Rec.y

    // JButton b1 = new JButton( "A Button" );


///////////////////////////////
// javax.media.j3d.Canvas3D
// https://en.wikipedia.org/wiki/Java_3D
//////////////////////////////


// import java.awt.Rectangle;
import java.awt.Container;
import java.awt.Insets;
// import javax.swing.JButton;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;



public class MainApp implements Runnable
  {
  public static final String VersionDate = "6/26/2019";
  private Font mainFont;
  private JTabbedPane mainTabbedPane;
  private JFrame mainFrame;
  private JLabel statusLabel;



  public static void main( String[] args )
    {
    // A static method in this class is creating
    // an instance of this class.
    MainApp mApp = new MainApp();

    // In the event queue.
    SwingUtilities.invokeLater( mApp );
    }



  // For the Runnable interface:
  @Override
  public void run()
    {
    setupMainFrame();
    }




  public void setupMainFrame()
    {
    mainFrame = new JFrame( "Code Editor" );
    mainFont = new Font( Font.MONOSPACED, Font.PLAIN, 45 );

    mainFrame.setDefaultCloseOperation(
                     WindowConstants.EXIT_ON_CLOSE );

    mainFrame.getContentPane().setBackground( Color.red );
    // mainFrame.getContentPane().setForeground( Color.red );

    mainFrame.setSize( 1200, 600 );
    addComponentsToMainFrame( mainFrame.getContentPane() );
    // mainFrame.pack();
    // mainFrame.setExtendedState( JFrame.MAXIMIZED_BOTH );

    setupMenus();

    // Center it.
    mainFrame.setLocationRelativeTo( null );
    mainFrame.setVisible( true );
    }




  private void addComponentsToMainFrame( Container pane )
    {
    pane.setLayout( new LayoutSimpleVertical( pane ));

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout( new LayoutSimpleVertical( mainPanel ));
    mainPanel.setBackground( Color.black );
    // Setting it to FixedHeightMax means this component is
    // stretchable.
    // new Dimension( Width, Height );
    mainPanel.setPreferredSize( new Dimension(
                         1, LayoutSimpleVertical.FixedHeightMax ));

    pane.add( mainPanel );

    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout( new LayoutSimpleVertical( bottomPanel ));
    bottomPanel.setBackground( Color.black );
    bottomPanel.setPreferredSize( new Dimension( 1, 74 ));
    pane.add( bottomPanel );

    // String "[" + minimum + "," + preferred + "," + maximum + "]@" + alignment;
    statusLabel = new JLabel( " Label with numbers to see." );
    statusLabel.setForeground( Color.white );
    statusLabel.setFont( mainFont );
    bottomPanel.add( statusLabel );

    // Add a second label below the first.
    // JLabel testLabel = new JLabel( " Test label." );

// black, blue, cyan, darkGray, gray, green, lightGray,
// magenta, orange, pink, red, white, yellow.

    mainTabbedPane = new JTabbedPane();
    mainTabbedPane.setBackground( Color.cyan );
    mainTabbedPane.setForeground( Color.black );
    mainTabbedPane.setFont( mainFont );
    mainTabbedPane.setPreferredSize( new Dimension(
                         1, LayoutSimpleVertical.FixedHeightMax ));

    mainPanel.add( mainTabbedPane );


    addTextPane( "The tab's title" );
    addTextPane( "Tab 2" );
    }




  private void addTextPane( String tabTitle )
    {
    // tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

    JTextArea textArea1 = new JTextArea();
    textArea1.setFont( mainFont );
    textArea1.setLineWrap( false );
    // textArea1.setWrapStyleWord( true );
    textArea1.setBackground( Color.black );
    textArea1.setForeground( Color.white );
    textArea1.setText( "The title is: " + tabTitle );
    // String getText();
    // selectAll()
    // append( String )

    JScrollPane scrollPane1 = new JScrollPane( textArea1 );

    scrollPane1.setVerticalScrollBarPolicy(
             JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );

    // scrollPane1.setBackground( Color.red );
    // scrollPane1.setForeground( Color.red );

    mainTabbedPane.addTab( tabTitle, null, scrollPane1,
                      "Tool tip." );

    }




  private void setupMenus()
    {
    JMenuBar menuBar = new JMenuBar();
    menuBar.setBackground( Color.black );

    JMenu menu = new JMenu( "A Menu" );
    menu.setMnemonic( KeyEvent.VK_A );
    menu.setFont( mainFont );
    menu.setForeground( Color.white );
    menuBar.add( menu );

    JMenuItem menuItem = new JMenuItem( "A text-only menu item",
                         KeyEvent.VK_T);

    menuItem.setAccelerator( KeyStroke.getKeyStroke(
        KeyEvent.VK_1, ActionEvent.ALT_MASK ));

    menuItem.setForeground( Color.white );
    menuItem.setBackground( Color.black );
    menuItem.setFont( mainFont );
    menu.add( menuItem );

    menuItem = new JMenuItem( "Second one." );
    menuItem.setMnemonic( KeyEvent.VK_B );
    menu.add( menuItem );

    // menu.addSeparator();
    // JMenu submenu = new JMenu( "A submenu" );
    // submenu.setMnemonic(KeyEvent.VK_S);
    // menuItem = new JMenuItem("An item in the submenu");
    // menuItem.setAccelerator(KeyStroke.getKeyStroke(
    //     KeyEvent.VK_2, ActionEvent.ALT_MASK));
    // submenu.add(menuItem);

    // menuItem = new JMenuItem("Another item");
    // submenu.add(menuItem);
    // menu.add(submenu);

    menu = new JMenu("Another Menu");
    menu.setMnemonic(KeyEvent.VK_N);
    menuBar.add(menu);

    // System.exit( 0 );

    mainFrame.setJMenuBar( menuBar );
    }




  }
