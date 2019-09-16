// This is adapted and modified from the original
// DefaultCaret.



import javax.swing.text.DefaultCaret;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.plaf.TextUI;




public class CaretWide extends DefaultCaret
  {
  // It needs to have a version UID since it's
  // serializable.
  public static final long serialVersionUID = 1;
  private final int drawWidth = 8;



  public CaretWide()
    {
    }



  protected synchronized void damage( Rectangle r )
    {
    if( r == null )
      return;

    int damageWidth = drawWidth;
    x = r.x - 4 - (damageWidth >> 1);
    y = r.y;
    width = 9 + damageWidth;
    height = r.height;

    repaint();
    }




  public void paint( Graphics g )
    {
    if( !isVisible() )
      return;

    try
    {
    TextUI mapper = getComponent().getUI();
    Rectangle r = mapper.modelToView( getComponent(),
                                      getDot(),
                                      getDotBias() );

    if( r == null )
      return;

    if( (r.width <= 0) && (r.height <= 0) )
      return;

    if( !contains( r ) )
      {
      // It got out of sync and needs the right location?
      Rectangle clip = g.getClipBounds();
      if( clip != null )
        {
        if( !clip.contains( this ))
          {
          repaint();
          }
        }

      damage( r );
      }

    g.setColor( getComponent().getCaretColor() );
    r.x -= drawWidth  >> 1;
    g.fillRect( r.x, r.y, drawWidth, r.height );
    }
    catch( Exception e )
      {
      // mApp.snowStatus()
      }
    }



  }

