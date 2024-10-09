package cleancode.studycafe.refactoring.io;

import cleancode.studycafe.refactoring.model.StudyCafeLockerPass;
import cleancode.studycafe.refactoring.model.StudyCafePass;
import cleancode.studycafe.refactoring.model.StudyCafePasses;

public interface OutputHandler {
    void showWelcomeMessage();
    void showAnnouncement();
    void askPassTypeSelection();
    void showPassListForSelection(StudyCafePasses passes);
    void askLockerPass(StudyCafeLockerPass lockerPass);
    void showPassOrderSummary(StudyCafePass selectedPass, StudyCafeLockerPass lockerPass);
    void showSimpleMessage(String message);
}
