

import java.awt.LayoutManager;
import java.awt.Container;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.AWTError;
import java.awt.Insets;
import java.awt.Rectangle;



public class LayoutSimpleVertical implements LayoutManager
  {
  public static final int FixedHeightMax = 1000000;



  public LayoutSimpleVertical()
    {
    //
    }



  // This is part of the LayoutManager interface.
  public void addLayoutComponent( String name, Component comp )
    {
    // Not used.
    }




  // This is part of the LayoutManager interface.
  public void removeLayoutComponent( Component comp )
    {
    // Not used.
    }




  // This is part of the LayoutManager interface.
  public Dimension preferredLayoutSize( Container target )
    {
    // The preferred size that this returns is the size that
    // it is now.  It's not the total preferred size of the
    // components it contains.
    Dimension targetSize = target.getSize();

    Insets insets = target.getInsets();

    targetSize.width = targetSize.width + insets.left + insets.right;
    targetSize.height = targetSize.height + insets.top + insets.bottom;

    /*
    Dimension minTargertSize = minimumLayoutSize( target );

    if( targetSize.width < minTargertSize.width )
      targetSize.width = minTargertSize.width;

    if( targetSize.height < minTargertSize.height )
      targetSize.height = minTargertSize.height;

    if( targetSize.height < 20 )
      targetSize.height = 20;

    if( targetSize.width < 20 )
      targetSize.width = 20;
    */

    return targetSize;
    }




  // This is part of the LayoutManager interface.
  public Dimension minimumLayoutSize( Container target )
    {
    return preferredLayoutSize( target );

    /*
    int fixedHeight = getFixedHeight( target );
    int fixedWidth = 900; // getFixedWidth( target );

    Dimension targetSize = new Dimension( fixedWidth,
                                          fixedHeight );

    Insets insets = target.getInsets();

    targetSize.width = targetSize.width + insets.left + insets.right;
    targetSize.height = targetSize.height + insets.top + insets.bottom;

    return targetSize;
    */
    }




  // This is part of the LayoutManager interface.
  public void layoutContainer( Container target )
    {
    // synchronized( this )
      // {

    Dimension targetSize = target.getSize();
    Insets in = target.getInsets();
    targetSize.width -= in.left + in.right;
    targetSize.height -= in.top + in.bottom;

    int fHeight = getFixedHeight( target );
    int leftOverHeight = targetSize.height - fHeight;

    int y = in.top;
    int n = target.getComponentCount();
    for( int i = 0; i < n; i++ )
      {
      Component c = target.getComponent( i );
      // if( !c.isVisible() )

      // Dimension min = c.getMinimumSize();
      Dimension pref = c.getPreferredSize();
      // Dimension max = c.getMaximumSize();

      int x = in.left;
      int height = pref.height;
      if( height >= FixedHeightMax )
        height = leftOverHeight; // Make it stretch to fit the container.

      int width = targetSize.width;
      c.setBounds( x, y, width, height );
      y += height;
      }
    }




  private int getFixedHeight( Container target )
    {
    int howMany = 0;
    int total = 0;
    int n = target.getComponentCount();
    for( int i = 0; i < n; i++ )
      {
      Component c = target.getComponent( i );
      // if( !c.isVisible() )

      Dimension pref = c.getPreferredSize();

      int height = pref.height;
      if( height < FixedHeightMax )
        {
        total += height;
        }
      else
        {
        // Then it's a stretch object.
        howMany++;
        if( howMany > 1 )
          throw new AWTError( "LayoutSimpleVertical can't have more than one stretch component." );

        }
      }

    return total;
    }



/*
  // This is a vertical layout, so get the widest one.
  private int getFixedWidth( Container target )
    {
    int howMany = 0;
    int widest = 0;
    int n = target.getComponentCount();
    for( int i = 0; i < n; i++ )
      {
      Component c = target.getComponent( i );
      // if( !c.isVisible() )

      // Dimension min = c.getMinimumSize();
      Dimension pref = c.getPreferredSize();

      int width = pref.width;
      if( width >= FixedHeightMax )
        continue;

      if( width > widest )
        {
        widest = width;
        }
      }

    return widest;
    }
*/


  }
