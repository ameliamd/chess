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
        String[] headers = {" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "};
        drawBoard(headers, 1, 8, 1);
    }
    private static void drawWhitePerspective() {
        String[] headers = {" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
        drawBoard(headers, 8, 1, -1);
    }

    private static void drawBoard(String[] headers, int startRow, int endRow, int rowIncrement) {
        drawHeaders(headers);

        for(int row = startRow; row != endRow + rowIncrement; row += rowIncrement){
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + row + " ");
            for(int col = 0; col < 8; col ++){
                int actualCol = (headers[0].equals(" a ")) ? col + 1 : 8 - col;

                if((row + actualCol) % 2 != 0){
                    System.out.print(SET_BG_COLOR_WHITE);
                } else {
                    System.out.print(SET_BG_COLOR_BLACK);
                }

                System.out.print(getStartPiece(row, actualCol));
            }

            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + row + " " + RESET_BG_COLOR + "\n");
        }

        drawHeaders(headers);
    }

    private static void drawHeaders(String[] headers) {
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "  ");
        for (String h : headers){
            System.out.print(h);
        }
        System.out.print("  " + RESET_BG_COLOR + "\n");
    }

    private static String getStartPiece(int row, int col){
        if(row == 2) return SET_TEXT_COLOR_RED + WHITE_PAWN;
        if(row == 7) return SET_TEXT_COLOR_BLUE + BLACK_PAWN;

        if(row == 1){
            if(col == 1 || col == 8) return SET_TEXT_COLOR_RED + WHITE_ROOK;
            if(col == 2 || col == 7) return SET_TEXT_COLOR_RED + WHITE_KNIGHT;
            if(col == 3 || col == 6) return SET_TEXT_COLOR_RED + WHITE_BISHOP;
            if(col == 4) return SET_TEXT_COLOR_RED + WHITE_QUEEN;
            if(col == 5) return SET_TEXT_COLOR_RED + WHITE_KING;

        }

        if(row == 8){
            if(col == 1 || col == 8) return SET_TEXT_COLOR_BLUE + BLACK_ROOK;
            if(col == 2 || col == 7) return SET_TEXT_COLOR_BLUE + BLACK_KNIGHT;
            if(col == 3 || col == 6) return SET_TEXT_COLOR_BLUE + BLACK_BISHOP;
            if(col == 4) return SET_TEXT_COLOR_BLUE + BLACK_QUEEN;
            if(col == 5) return SET_TEXT_COLOR_BLUE + BLACK_KING;
    }
        return EMPTY;
}
}
