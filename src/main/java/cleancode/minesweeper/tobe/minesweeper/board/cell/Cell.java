package cleancode.minesweeper.tobe.minesweeper.board.cell;

public interface Cell {
    boolean isLandMine();

    boolean hasLandMineCount();

    void flag();

    void open();

    boolean isChecked();

    boolean isOpened();

    CellSnapshot getSnapshot();
}

