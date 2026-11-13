package ui;

import static ui.EscapeSequences.*;

public class DrawBoard {
    public static void draw(String playerColor){
        System.out.print(ERASE_SCREEN);

        if(playerColor.equalsIgnoreCase("BLACK")){
            drawBlackPerspective();
        }else{
            drawWhitePerspective();
        }

        System.out.print(RESET_BG_COLOR + RESET_TEXT_COLOR);
        System.out.println();
    }

    private static void drawBlackPerspective() {
        System.out.println("Black Perspective Board: ");
    }

    private static void drawWhitePerspective() {
        System.out.println("White Perspective Board: ");
    }
}
