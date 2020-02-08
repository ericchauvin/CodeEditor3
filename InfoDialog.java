// Copyright Eric Chauvin 2019 - 2020.




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
import javax.swing.JDialog;
import javax.swing.WindowConstants;
import javax.swing.JTextField;
import javax.swing.JPanel;



class InfoDialog extends JDialog
                   implements ActionListener
  {
  // It needs to have a version UID since it's
  // serializable.
  public static final long serialVersionUID = 1;
  private MainApp mApp;
  private JTextField textField;




  public InfoDialog( Frame useFrame,
                     MainApp useApp,
                     Font useFont,
                     String useshowText )
    {
    super( useFrame, true );

    mApp = useApp;

    setTitle( "Test info" );

    textField = new JTextField( 10 );

    addComponents( getContentPane() );

    // setContentPane( optionPane );

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
        textField.requestFocusInWindow();
        }
      });

    textField.addActionListener( this );

    // Center it.
    setLocationRelativeTo( null );

    }




  private void addComponents( Container pane )
    {
    // pane.setBackground( Color.red );
    // pane.setForeground( Color.red );
    pane.setLayout( new LayoutSimpleVertical());

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout( new LayoutSimpleVertical());
    mainPanel.setBackground( Color.red );
    // Setting it to FixedHeightMax means this component is
    // stretchable.
    // new Dimension( Width, Height );
    mainPanel.setPreferredSize( new Dimension(
                   1, LayoutSimpleVertical.FixedHeightMax ));

    pane.add( mainPanel );


    }



  public static void showInfo( Frame useFrame,
                               MainApp useApp,
                               Font useFont,
                               String showText )
    {
    InfoDialog infoD = new InfoDialog( useFrame,
                                       useApp,
                                       useFont,
                                       showText );

    infoD.setVisible( true );
    useApp.showStatus( "Before dispose." );
    infoD.dispose();
    }



/*
  public String getValidatedText()
    {
    return typedText;
    }
*/



  public void actionPerformed( ActionEvent e )
    {
    mApp.showStatus( "Action performed." );
    // optionPane.setValue( btnString1 );
    }




/*
  public void clearAndHide()
    {
    textField.setText( null );
    setVisible( false );
    }
*/


  }
