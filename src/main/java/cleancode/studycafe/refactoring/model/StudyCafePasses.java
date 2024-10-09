package cleancode.studycafe.refactoring.model;

import java.util.ArrayList;
import java.util.List;

public class StudyCafePasses {
    List<StudyCafePass> passes;

    private StudyCafePasses(List<StudyCafePass> passes) {
        this.passes = passes;
    }

    public static StudyCafePasses of(List<StudyCafePass> studyCafePasses){
        return new StudyCafePasses(studyCafePasses);
    }

    public StudyCafePasses selectBy(StudyCafePassType studyCafePassType) {
        ArrayList<StudyCafePass> studyCafePasses = new ArrayList<>(passes);
        List<StudyCafePass> selectedPasses = extractPassesBy(studyCafePassType, studyCafePasses);

        return of(selectedPasses);
    }

    public List<StudyCafePass> getPasses() {
        return new ArrayList<>(passes);
    }

    public StudyCafePass getBy(int index){
        return passes.get(index);
    }

    private static List<StudyCafePass> extractPassesBy(StudyCafePassType studyCafePassType, ArrayList<StudyCafePass> studyCafePasses) {
        return studyCafePasses.stream()
                .filter(studyCafePass -> studyCafePass.isSamePassType(studyCafePassType))
                .toList();
    }
}
