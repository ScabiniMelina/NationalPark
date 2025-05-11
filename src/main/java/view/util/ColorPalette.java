package view.util;

import java.awt.*;

public enum ColorPalette {
    BACKGROUND_DARK_BLUE  (new Color(30, 42, 68)),
    TEXT_WHITE_SOFT  (new Color(230, 240, 250)),
    BUTTON_KRUSKAL_BLUE (new Color(77, 168, 218)),
    BUTTON_STATIONS_PURPLE (new Color(142, 68, 173)),
    TEXT_WHITE (Color.WHITE),
    TRAIL_MEDIUM_IMPACT (new Color(212, 184, 60)),
    TRAIL_LOW_IMPACT (new Color(106, 191, 75)),
    TRAIL_HIGH_IMPACT (new Color(230, 57, 70));

    private final Color color;

    ColorPalette(Color color){
        this.color = color;

    }

    public Color getColor(){
        return color;
    }
}

