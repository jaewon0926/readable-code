package cleancode.studycafe.refactoring;

import cleancode.studycafe.refactoring.exception.AppException;
import cleancode.studycafe.refactoring.io.*;
import cleancode.studycafe.refactoring.model.*;

public class StudyCafePassMachine {

    private static final StudyCafeFileHandler STUDY_CAFE_FILE_HANDLER = new StudyCafeFileHandler();
    private final InputHandler inputHandler;
    private final OutputHandler outputHandler;

    public StudyCafePassMachine(InputHandler inputHandler, OutputHandler outputHandler) {
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
    }

    public void run() {
        try {
            outputHandler.showWelcomeMessage();
            outputHandler.showAnnouncement();
            outputHandler.askPassTypeSelection();

            StudyCafePassType studyCafePassType = inputHandler.getPassTypeSelectingUserAction();

            StudyCafePasses studyCafePasses = STUDY_CAFE_FILE_HANDLER.readStudyCafePasses(studyCafePassType);

            outputHandler.showPassListForSelection(studyCafePasses);
            StudyCafePass selectedPass = inputHandler.getSelectPass(studyCafePasses);
            outputHandler.showPassOrderSummary(selectedPass, null);

            if (isFixedType(studyCafePassType)) {
                StudyCafeLockerPasses studyCafeLockerPasses = STUDY_CAFE_FILE_HANDLER.readLockerPasses();
                StudyCafeLockerPass lockerPass = studyCafeLockerPasses.getLockerPassBy(selectedPass);

                boolean lockerSelection = false;
                if (canChooseLockerPass(lockerPass)) {
                    outputHandler.askLockerPass(lockerPass);
                    lockerSelection = inputHandler.getLockerSelection();
                }
                if (lockerSelection) {
                    lockerPass = null;
                }

                outputHandler.showPassOrderSummary(selectedPass, lockerPass);
            }
        } catch (AppException e) {
            outputHandler.showSimpleMessage(e.getMessage());
        } catch (Exception e) {
            outputHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
        }
    }

    private static boolean isFixedType(StudyCafePassType studyCafePassType) {
        return studyCafePassType == StudyCafePassType.FIXED;
    }

    private static boolean canChooseLockerPass(StudyCafeLockerPass lockerPass) {
        return lockerPass != null;
    }

}
