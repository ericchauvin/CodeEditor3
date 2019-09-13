// Copyright Eric Chauvin 2019.


import javax.swing.text.DefaultCaret;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.plaf.TextUI;


 

public class CaretWide extends DefaultCaret
  {
  // It needs to have a version UID since it's
  // serializable.
  public static final long serialVersionUID = 1;
  private final int drawWidth = 6;



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

    if( (r.width == 0) && (r.height == 0) )
      return;

/*
    if( width > 0 && height > 0 &&

boolean contains(int x, int y)

               !this._contains( r.x, r.y, r.width, r.height ))
      {
      // We seem to have gotten out of sync and no
      // longer contain the right location, adjust
      // accordingly.
      Rectangle clip = g.getClipBounds();
      if( clip != null && !clip.contains( this ))
        {
        // Clip doesn't contain the old location,
        // force it to be repainted lest we leave
        // a caret around.
        repaint();
        }

      // This will potentially cause a repaint of
      // something we're already repainting, but
      // without changing the semantics of damage we
      // can't really get around this.
      damage( r );
      }
*/

 
    g.setColor( getComponent().getCaretColor() );
    int paintWidth = drawWidth; // getCaretWidth(r.height);
    r.x -= paintWidth  >> 1;
    g.fillRect( r.x, r.y, paintWidth, r.height );

    // see if we should paint a flag to indicate the
    // bias of the caret.
    // PENDING(prinz) this should be done through
    // protected methods so that alternative LAF
    // will show bidi information.

    /*
    Document doc = component.getDocument();
    if( doc instanceof AbstractDocument )
      {
      AbstractDocument getBidiRootElement()

      Element bidi = ((AbstractDocument)doc ).
                                       getBidiRootElement();

      if( (bidi != null) && (bidi.getElementCount() > 1))
        {
        // there are multiple directions present.
        flagXPoints[0] = r.x + ((dotLTR) ?
                                        paintWidth : 0);

        flagYPoints[0] = r.y;
        flagXPoints[1] = flagXPoints[0];
        flagYPoints[1] = flagYPoints[0] + 4;
        flagXPoints[2] = flagXPoints[0] + ((dotLTR) ?
                                                    4 : -4);
        flagYPoints[2] = flagYPoints[0];
        g.fillPolygon(flagXPoints, flagYPoints, 3);
        }
      }
      */

    }
    catch( Exception e )
      {
      // can't render I guess
      //System.err.println("Can't render cursor");
      }
    }



  }


