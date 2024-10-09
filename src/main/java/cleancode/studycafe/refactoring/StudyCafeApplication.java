package cleancode.studycafe.refactoring;

import cleancode.studycafe.refactoring.io.*;

public class StudyCafeApplication {

    public static void main(String[] args) {
        InputHandler inputHandler = new ConsoleInputHandler();
        OutputHandler outputHandler = new ConsoleOutputHandler();
        StudyCafePassMachine studyCafePassMachine = new StudyCafePassMachine(inputHandler, outputHandler);
        studyCafePassMachine.run();
    }

}
