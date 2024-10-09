package cleancode.studycafe.refactoring.io;

import cleancode.studycafe.refactoring.model.StudyCafePass;
import cleancode.studycafe.refactoring.model.StudyCafePassType;
import cleancode.studycafe.refactoring.model.StudyCafePasses;

public interface InputHandler {
    StudyCafePassType getPassTypeSelectingUserAction();
    StudyCafePass getSelectPass(StudyCafePasses passes);
    boolean getLockerSelection();
}
