import java.awt.Color;
import java.awt.Font;

/** <p>Style stands for Indent, Color, Font and Leading.</p>
 * <p>The link between a style number and a item level is hard-linked:
 * in Slide the style is grabbed for an item
 * with a style number the same as the item level.</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */

public class Style {
    // private static final String FONTNAME = "Helvetica";
    private int level;
	private int indent;
    private Color color;
    private Font font;
    private String fontName;
    private int fontSize;
    private int leading;

	public Style(int level, int indent, String colorStr, String fontName, int points, int leading) {
        this.level = level;
        this.indent = indent;
        this.color = hex2Rgb(colorStr);
        this.fontName = fontName;
        this.fontSize = points;
        this.font = new Font(this.fontName, Font.BOLD, this.fontSize);
        this.leading = leading;
    }

    public int getLevel() {
        return level;
    }

    public Font getFont(float scale) {
        if (font == null) {
            font = new Font("Helvetica", Font.PLAIN, 12); 
        }
        return font.deriveFont(Font.BOLD, fontSize * scale);
    }
    
	public void setFont(Font font) {
        this.font = font;
    }

	public int getIndent() {
        return indent;
    }

    public void setIndent(int indent) {
        this.indent = indent;
    }

	public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

	public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getLeading() {
        return leading;
    }

    public void setLeading(int leading) {
        this.leading = leading;
    }

	public String toString() {
		return "["+ indent + "," + color + "; " + fontSize + " on " + leading +"]";
	}

    private Color hex2Rgb(String colorStr) {
        return new Color(
            Integer.valueOf(colorStr.substring(1, 3), 16),
            Integer.valueOf(colorStr.substring(3, 5), 16),
            Integer.valueOf(colorStr.substring(5, 7), 16)
        );
    }
}
