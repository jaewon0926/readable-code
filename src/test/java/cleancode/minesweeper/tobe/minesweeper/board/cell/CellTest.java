package cleancode.minesweeper.tobe.minesweeper.board.cell;

import cleancode.minesweeper.tobe.minesweeper.board.GameBoard;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPositions;
import cleancode.minesweeper.tobe.minesweeper.exception.GameException;
import cleancode.minesweeper.tobe.minesweeper.gamelevel.Beginner;
import cleancode.minesweeper.tobe.minesweeper.io.BoardIndexConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CellTest {
    @DisplayName("CellState 초기화 시 닫혀있어야한다.")
    @Test
    void initializeCellState(){
        // given
        // when
        CellState initializedCell = CellState.initialize();

        //then
        assertThat(initializedCell.isFlagged()).isFalse();
        assertThat(initializedCell.isOpened()).isFalse();

    }

    @DisplayName("빈 셀에 깃발을 꽂으면 체크된 것으로 확인되선 안된다.")
    @Test
    void flaggedEmptyCanCheck(){
        // given
        Cell emptyCell = new EmptyCell();

        // when
        emptyCell.flag();

        //then
        assertThat(emptyCell.isChecked()).isFalse();
    }

    @DisplayName("Cell[][]으로 부터 CellPositions 생성시 배열의 크기와 동일 List가 생성되어야 한다.")
    @Test
    void createCellPositionsFromBoard(){
        // given
        Cell[][] board = new Cell[2][2];

        // when
        CellPositions cellPositions = CellPositions.from(board);
        List<CellPosition> positions = cellPositions.getPositions();

        //then
        assertThat(positions).hasSize(4);

    }

    @DisplayName("0보다 작은 인덱스가 주어질 때 셀포지션은 생성되지 않는다.")
    @Test
    void createCellPositionFromWrongIndex(){
        // given
        int rowIndex = 10;
        int colIndex = -1;

        // when
        //then
        assertThatThrownBy(()->CellPosition.of(rowIndex,colIndex))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바르지 않은 좌표입니다.");

    }

    @DisplayName("CellPositions 에서 CellPosition을 10개 추출하면 동일한 사이즈의 리스트가 반환되어야 한다.")
    @Test
    void test1(){
        // given
        Cell[][] board = new Cell[10][10];

        // when
        CellPositions cellPositions = CellPositions.from(board);

        //then
        assertThat(cellPositions.extractRandomPositions(10)).hasSize(10);

    }

    @DisplayName("비기너 레벨로 게임보드를 초기화 시 8행 10열 크기여야 한다.")
    @Test
    void initializeGameBoardByBeginnerLevel(){
        // given
        GameBoard gameBoard = new GameBoard(new Beginner());

        // when
        gameBoard.initializeGame();

        //then
        assertThat(gameBoard.getRowSize()).isEqualTo(8);
        assertThat(gameBoard.getColSize()).isEqualTo(10);

    }

    @DisplayName("모든 Cell에 깃발을 꽂았을 때 승리하면 안된다")
    @Test
    void allFlaggedBoardInProgress(){
        // given
        GameBoard gameBoard = new GameBoard(new Beginner());
        gameBoard.initializeGame();

        // when
        for(int row=0; row< gameBoard.getRowSize();row++){
            for(int col=0;col< gameBoard.getColSize();col++){
                CellPosition cellPosition = CellPosition.of(row, col);
                gameBoard.flagAt(cellPosition);
            }
        }

        //then
        assertThat(gameBoard.isWinStatus()).isFalse();

    }

    @DisplayName("대문자 알파벳이 입력되면 GameException이 발생한다.")
    @Test
    void wrongUserActionInput(){
        // given
        BoardIndexConverter boardIndexConverter = new BoardIndexConverter();

        // when
        String cellInput = "A";

        //then
        assertThatThrownBy(()->boardIndexConverter.getSelectedColIndex(cellInput))
                .isInstanceOf(GameException.class)
                .hasMessage("잘못된 입력입니다.");

    }

}