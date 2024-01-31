import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.Color;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

public class StyleFactory {
    private Map<Integer, Style> stylesMap = new HashMap<>();
    private static final String CONFIG_FILE = "src/style/stylesheet.json";

    public StyleFactory() {
        loadStyles();
    }

    private void loadStyles() {
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(Color.class, new ColorDeserializer())
                .create();
            StyleContainer styleContainer = gson.fromJson(reader, StyleContainer.class);

            for (Style style : styleContainer.getStyles()) {
                stylesMap.put(style.getLevel(), style);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Style getStyle(int level) {
        return stylesMap.getOrDefault(level, stylesMap.get(stylesMap.size() - 1));
    }
}