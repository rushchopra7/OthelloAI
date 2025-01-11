package de.lmu.bio.ifi;

import szte.mi.Move;

import javax.swing.text.Utilities;

import static de.lmu.bio.ifi.Utilities.getIntInput;

public class HumanPlayer {

    public Move getMove() {
        int x = getIntInput("Enter x-coordinate (0-7): ");
        int y = getIntInput("Enter y-coordinate (0-7): ");
        return new Move(x, y);
    }
}