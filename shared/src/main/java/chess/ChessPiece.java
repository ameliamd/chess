package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    private void Move(ChessBoard board, ChessPosition startPosition, int rowOffset, int colOffset, boolean isSliding, Collection<ChessMove> validMoves) {
        int currentRow = startPosition.getRow() + rowOffset;
        int currentCol = startPosition.getColumn() + colOffset;

        while (currentRow >= 1 && currentRow <= 8 && currentCol >= 1 && currentCol <= 8) {
            ChessPosition newPosition = new ChessPosition(currentRow, currentCol);
            ChessPiece pieceAtDestination = board.getPiece(newPosition);

            if (pieceAtDestination == null) {
                validMoves.add(new ChessMove(startPosition, newPosition, null));

            } else {
                if (pieceAtDestination.getTeamColor() != this.getTeamColor()) {
                    validMoves.add(new ChessMove(startPosition, newPosition, null));
                }
                break;

            }

            if (!isSliding) {
                break;
            }

            currentRow += rowOffset;
            currentCol += colOffset;


        }

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        switch (this.getPieceType()) {
            case KING:
                return kingMoves(board, myPosition);
            case QUEEN:
                return queenMoves(board, myPosition);
            case BISHOP:
                return bishopMoves(board, myPosition);
            case ROOK:
                return rookMoves(board,myPosition);
            case KNIGHT:
                return knightMoves(board, myPosition);
            case PAWN:
                break;
            default:
                return new ArrayList<>();
        }
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        boolean isSliding = false;
        Move(board, myPosition, 1, 0, isSliding, moves); //Up
        Move(board, myPosition, 1, 1, isSliding, moves); //Up-Right
        Move(board, myPosition, 1, -1, isSliding, moves); //Up-Left
        Move(board, myPosition, 0, 1, isSliding, moves); //Right
        Move(board, myPosition, 0, -1, isSliding, moves); //Left
        Move(board, myPosition, -1, 0, isSliding, moves); //Down
        Move(board, myPosition, -1, 1, isSliding, moves); //Down-Right
        Move(board, myPosition, -1, -1, isSliding, moves); //Down-Left

        return moves;

    }

    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        boolean isSliding = true;
        Move(board, myPosition, 1, 0, isSliding, moves); //Up
        Move(board, myPosition, 1, 1, isSliding, moves); //Up-Right
        Move(board, myPosition, 1, -1, isSliding, moves); //Up-Left
        Move(board, myPosition, 0, 1, isSliding, moves); //Right
        Move(board, myPosition, 0, -1, isSliding, moves); //Left
        Move(board, myPosition, -1, 0, isSliding, moves); //Down
        Move(board, myPosition, -1, 1, isSliding, moves); //Down-Right
        Move(board, myPosition, -1, -1, isSliding, moves); //Down-Left

        return moves;

    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        boolean isSliding = true;
        Move(board, myPosition, 1, 1, isSliding, moves); //Up-Right
        Move(board, myPosition, 1, -1, isSliding, moves); //Up-Left
        Move(board, myPosition, -1, 1, isSliding, moves); //Down-Right
        Move(board, myPosition, -1, -1, isSliding, moves); //Down-Left

        return moves;

    }

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        boolean isSliding = true;
        Move(board, myPosition, 1, 0, isSliding, moves); //Up
        Move(board, myPosition, 0, 1, isSliding, moves); //Right
        Move(board, myPosition, 0, -1, isSliding, moves); //Left
        Move(board, myPosition, -1, 0, isSliding, moves); //Down
        return moves;

    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        boolean isSliding = false;
        Move(board, myPosition, 2, 1, isSliding, moves); //Up 2, Right 1
        Move(board, myPosition, 2, -1, isSliding, moves); //Up 2, Left 1
        Move(board, myPosition, 1, 2, isSliding, moves); //Down 2, Right 1
        Move(board, myPosition, 1, -2, isSliding, moves); //Down 2, Left 1

        Move(board, myPosition, -2, 1, isSliding, moves); //Up 1, Right 2
        Move(board, myPosition, -2, -1, isSliding, moves); //Up 1, Left 2
        Move(board, myPosition, -1, 2, isSliding, moves); //Down 1, Right 2
        Move(board, myPosition, -1, -2, isSliding, moves); //Down 1, Left 2

        return moves;

    }

}
