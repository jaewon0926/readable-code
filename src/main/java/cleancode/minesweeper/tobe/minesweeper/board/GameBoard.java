package cleancode.minesweeper.tobe.minesweeper.board;

import cleancode.minesweeper.tobe.minesweeper.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPositions;
import cleancode.minesweeper.tobe.minesweeper.board.position.RelativePosition;
import cleancode.minesweeper.tobe.minesweeper.board.cell.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class GameBoard {
    private final Cell[][] board;
    private final int landMineCount;
    private GameStatus gameStatus;

    public GameBoard(GameLevel gameLevel) {
        int rowSize = gameLevel.getRowSize();
        int colSize = gameLevel.getColSize();
        board = new Cell[rowSize][colSize];

        landMineCount = gameLevel.getLandMineCount();
        initializeGameStatus();
    }

    // 상태 변경
    public void initializeGame() {
        initializeGameStatus();
        CellPositions cellPositions = CellPositions.from(board);

        initializeEmptyCells(cellPositions);

        List<CellPosition> landMinePositions = cellPositions.extractRandomPositions(landMineCount);
        initializeLandMindCells(landMinePositions);

        List<CellPosition> numberPositionCandidates = cellPositions.subtract(landMinePositions);
        initializeNumberCells(numberPositionCandidates);
    }
    public void flagAt(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        cell.flag();
        checkIfGameIsOver();
    }

    public void openAt(CellPosition cellPosition) {
        if (isLandMineCellAt(cellPosition)) {
            openOneCellAt(cellPosition);
            changeGameStatusToLose();
            return;
        }

        openSurroundedCells(cellPosition);
        checkIfGameIsOver();
    }



    // 판별
    public boolean isInvalidCellPosition(CellPosition cellPosition) {
        int rowSize = getRowSize();
        int colSize = getColSize();
        return cellPosition.isRowIndexMoreThanOrEqual(rowSize)
                || cellPosition.isColIndexMoreThanOrEqual(colSize);
    }

    public boolean isInProgress() {
        return gameStatus==GameStatus.IN_PROGRESS;
    }

    public boolean isWinStatus() {
        return gameStatus==GameStatus.WIN;
    }
    public boolean isLoseStatus() {
        return gameStatus==GameStatus.LOSE;
    }

    // 조회

    public int getRowSize() {
        return board.length;
    }
    public int getColSize() {
        return board[0].length;
    }

    public CellSnapshot getSnapshot(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        return cell.getSnapshot();
    }



    private void initializeGameStatus() {
        gameStatus = GameStatus.IN_PROGRESS;
    }

    private void initializeEmptyCells(CellPositions cellPositions) {
        List<CellPosition> allPositions = cellPositions.getPositions();
        for (CellPosition position : allPositions) {
            updateCellAt(position, new EmptyCell());
        }
    }

    private void initializeLandMindCells(List<CellPosition> landMinePositions) {
        for (CellPosition position : landMinePositions) {
            updateCellAt(position, new LandMineCell());
        }
    }

    private void initializeNumberCells(List<CellPosition> numberPositionCandidates) {
        for (CellPosition candidatePosition : numberPositionCandidates) {
            int count = countNearbyLandMines(candidatePosition);
            if (count != 0) {
                updateCellAt(candidatePosition,new NumberCell(count));;
            }
        }
    }

    private int countNearbyLandMines(CellPosition cellPosition) {
        int rowSize = getRowSize();
        int colSize = getColSize();

        long count = calculateSurroundedPositions(cellPosition, rowSize, colSize).stream()
                .filter(this::isLandMineCellAt)
                .count();

        return (int) count;
    }

    private void updateCellAt(CellPosition landMinePosition, Cell cell) {
        board[landMinePosition.getRowIndex()][landMinePosition.getColIndex()] = cell;
    }

    private static List<CellPosition> calculateSurroundedPositions(CellPosition cellPosition, int rowSize, int colSize) {
        return RelativePosition.SURROUNDED_POSITIONS.stream()
                .filter(cellPosition::canCalculatePositionBy)
                .map(cellPosition::calculatePositionBy)
                .filter(position -> position.isRowIndexLessThan(rowSize))
                .filter(position -> position.isColIndexLessThan(colSize))
                .toList();
    }

/*  재귀로 인한 StackOverFlow
    private void openSurroundedCells(CellPosition cellPosition) {
        if (isOpenedCell(cellPosition)) {
            return;
        }
        if (isLandMineCellAt(cellPosition)) {
            return;
        }

        openOneCellAt(cellPosition);

        if (doseCellHaveLandMineCount(cellPosition)) {
            return;
        }

        List<CellPosition> surroundedPositions = calculateSurroundedPositions(cellPosition, getRowSize(), getColSize());
        surroundedPositions.forEach(this::openSurroundedCells);

        *//*for (RelativePosition relativePosition : RelativePosition.SURROUNDED_POSITIONS) {
            if(cellPosition.canCalculatePositionBy(relativePosition)){
                CellPosition nextCellPosition = cellPosition.calculatePositionBy(relativePosition);
                openSurroundedCells(nextCellPosition);
            }
        }*//*
    }*/
    private void openSurroundedCells(CellPosition cellPosition){
        Deque<CellPosition> stack = new ArrayDeque<>();
        stack.push(cellPosition);

        while(!stack.isEmpty()){
            openAndPushCellAt(stack);
        }
    }

    private void openAndPushCellAt(Deque<CellPosition> stack) {
        CellPosition currentCellPosition = stack.pop();
        if (isOpenedCell(currentCellPosition)) {
            return;
        }
        if (isLandMineCellAt(currentCellPosition)) {
            return;
        }

        openOneCellAt(currentCellPosition);

        if (doseCellHaveLandMineCount(currentCellPosition)) {
            return;
        }

        List<CellPosition> surroundedPositions = calculateSurroundedPositions(currentCellPosition, getRowSize(), getColSize());
        for (CellPosition surroundedPosition : surroundedPositions) {
            stack.push(surroundedPosition);
        }
    }

    private void openOneCellAt(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        cell.open();
    }

    private boolean isOpenedCell(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        return cell.isOpened();
    }

    private boolean isLandMineCellAt(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        return cell.isLandMine();
    }

    private boolean doseCellHaveLandMineCount(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        return cell.hasLandMineCount();
    }

    private void checkIfGameIsOver() {
        boolean isAllChecked = isAllCellChecked();
        if (isAllChecked) {
            changeGameStatusToWin();
        }
    }

    private boolean isAllCellChecked() {
        Cells cells = Cells.from(board);
        return cells.isAllChecked();

//        return Arrays.stream(board)
//                .flatMap(Arrays::stream)
//                .allMatch(Cell::isChecked);
        /*Stream<String[]> stringArrayStream = Arrays.stream(BOARD);
        Stream<String> stringStream = stringArrayStream
                .flatMap(stringArray -> {
                    Stream<String> stringStream2 = Arrays.stream(stringArray);
                    return stringStream2;
                });
        return stringStream // Stream<String>
                .noneMatch(cell -> cell.equals(CLOSED_CELL_SIGN));*/
    }
    private void changeGameStatusToWin() {
        gameStatus = GameStatus.WIN;
    }
    private void changeGameStatusToLose() {
        gameStatus = GameStatus.LOSE;
    }
    private Cell findCell(CellPosition cellPosition) {
        return board[cellPosition.getRowIndex()][cellPosition.getColIndex()];
    }
}
