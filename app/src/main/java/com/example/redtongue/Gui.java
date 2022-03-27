package com.example.redtongue;

/**
 * Dummy class to allow for compatibility
 */
public class Gui implements UI {
    public Gui(RedTongue red, String s) {
    }
    public Gui(RedTongue red) {
    }
    public void display(char type, String s) {
    }
    public void changeMode(Mode mode) {
    }
    public String getInput(String message) {
        return null;
    }
    public Progress getProg() {
        return null;
    }
}
