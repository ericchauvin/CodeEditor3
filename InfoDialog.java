// Copyright Eric Chauvin 2020.




import java.awt.Frame;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.ComponentAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JDialog;
import javax.swing.WindowConstants;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JButton;



class InfoDialog extends JDialog
                   implements ActionListener
  {
  // It needs to have a version UID since it's
  // serializable.
  public static final long serialVersionUID = 1;
  private MainApp mApp;
  private Font mainFont;
  private JTextArea textArea;
  private JButton okButton;
  private String showText;



  public InfoDialog( Frame useFrame,
                     MainApp useApp,
                     Font useFont,
                     String useShowText,
                     int width,
                     int height )
    {
    super( useFrame, true );

    mApp = useApp;
    mainFont = useFont;
    showText = useShowText;
    addComponents( getContentPane() );
    // setContentPane( );

    // Make it so that pressing Enter clicks the OK button.
    getRootPane().setDefaultButton( okButton );

    setSize( width, height );

    setTitle( "Information" );

    setDefaultCloseOperation( WindowConstants.HIDE_ON_CLOSE );
    // DISPOSE_ON_CLOSE, DO_NOTHING_ON_CLOSE, EXIT_ON_CLOSE

    addWindowListener( new WindowAdapter()
      {
      public void windowClosing( WindowEvent we )
        {

        }
      });

    addComponentListener( new ComponentAdapter()
      {
      public void componentShown( ComponentEvent ce )
        {
        okButton.requestFocusInWindow();
        }
      });

    // Center it.
    setLocationRelativeTo( null );
    }




  private void addComponents( Container pane )
    {
    pane.setBackground( Color.green );
    // pane.setForeground( Color.red );

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout( new LayoutSimpleVertical());
    mainPanel.setBackground( Color.red );

    textArea = new JTextArea( showText );
    // Setting it to FixedHeightMax means this component is
    // stretchable.
    // new Dimension( Width, Height );
    textArea.setPreferredSize( new Dimension(
                   1, LayoutSimpleVertical.FixedHeightMax ));

    textArea.setFont( mainFont );
    textArea.setBackground( Color.black );
    textArea.setForeground( Color.white );
    textArea.setLineWrap( true );
    textArea.setWrapStyleWord( true );

    mainPanel.add( textArea );

    okButton = new JButton( "OK" );
    okButton.setMnemonic( KeyEvent.VK_O ); // VK_ENTER
    okButton.setActionCommand( "OkButton" );
    okButton.addActionListener( this );
    okButton.setFont( mainFont );
    okButton.setBackground( Color.black );
    okButton.setForeground( Color.white );
    mainPanel.add( okButton );

    pane.add( mainPanel );
    }



  public static void showInfo( Frame useFrame,
                               MainApp useApp,
                               Font useFont,
                               String showText,
                               int width,
                               int height )
    {
    InfoDialog infoD = new InfoDialog( useFrame,
                                       useApp,
                                       useFont,
                                       showText,
                                       width,
                                       height );

    infoD.setVisible( true );
    // useApp.showStatus( "Before dispose." );
    infoD.dispose();
    // useApp.showStatus( "After dispose() was called." );
    }




  public void actionPerformed( ActionEvent event )
    {
    try
    {
    // String paramS = event.paramString();

    String command = event.getActionCommand();

    if( command == null )
      {
      // keyboardTimerEvent();
      return;
      }

    // mApp.showStatus( "ActionEvent Command is: " + command );

    if( command == "OkButton" )
      {
      // mApp.showStatus( "okButton was clicked." );
      setVisible( false );
      return;
      }

    }
    catch( Exception e )
      {
      mApp.showStatus( "Exception in actionPerformed()." );
      mApp.showStatus( e.getMessage() );
      }
    }




  }
