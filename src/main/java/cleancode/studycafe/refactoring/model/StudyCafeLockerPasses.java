package cleancode.studycafe.refactoring.model;

import java.util.ArrayList;
import java.util.List;

public class StudyCafeLockerPasses {
    List<StudyCafeLockerPass> passes;

    private StudyCafeLockerPasses(List<StudyCafeLockerPass> passes) {
        this.passes = passes;
    }

    public static StudyCafeLockerPasses of(List<StudyCafeLockerPass> passes){
        return new StudyCafeLockerPasses(passes);
    }

    public StudyCafeLockerPass getLockerPassBy(StudyCafePass studyCafePass){
        List<StudyCafeLockerPass> studyCafeLockerPasses = new ArrayList<>(passes);
        return studyCafeLockerPasses.stream()
                .filter(lockerPass -> lockerPass.isSamePass(studyCafePass))
                .findFirst()
                 .orElse(null);
    }

}
