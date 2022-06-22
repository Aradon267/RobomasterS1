package com.example.robomaster;

import java.util.List;

/**
 * The class will hold the information on each program that was saved to the firebase.
 * programName - the name of the program.
 * programDescription - the description of the program.
 * username - the user that saved the program.
 * date - the date the program was saved on.
 * program - the commands to be executed.
 * programDisplay - the commands to be displayed to the user.
 * progid - the program's ID.
 */

public class Program {

    public List<String> program;
    public List<String> programDisplay;
    public String username;
    public String programName;
    public String programDescription;
    public String date;
    public String progid;

    /**
     * An empty constructor of the class
     */
    public Program(){}


    /**
     * A regular constructor of the class
     * @param program the commands to be executed
     * @param username the user that saved the program
     * @param programName the name of the program
     * @param programDescription the description of the program
     * @param date the date the program was saved on
     * @param programDisplay the commands to be displayed to the user
     * @param progid the program's ID from the push
     */
    public Program(List<String> program, String username, String programName, String programDescription, String date, List<String> programDisplay, String progid) {
        this.program = program;
        this.username = username;
        this.programName = programName;
        this.programDescription = programDescription;
        this.date = date;
        this.programDisplay = programDisplay;
        this.progid = progid;
    }

    /**
     * The function will return the program ID
     * @return the program's ID
     */
    public String getprogid() {
        return progid;
    }

    /**
     * The function will set the program ID
     * @param progid the program's ID
     */
    public void setprogid(String progid) {
        this.progid = progid;
    }

    /**
     * The function will return the program to be executed
     * @return the program
     */
    public List<String> getProgram() {
        return program;
    }

    /**
     * The function will set the program that needs to be executed
     * @param program the program
     */
    public void setProgram(List<String> program) {
        this.program = program;
    }

    /**
     * The function will return the user that saved the program
     * @return the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * The function will set the username that saved the program
     * @param username the name of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * The function will return the name of the program
     * @return the name of the program
     */
    public String getProgramName() {
        return programName;
    }

    /**
     * The function will set the name of the program
     * @param programName the name of the program
     */
    public void setProgramName(String programName) {
        this.programName = programName;
    }

    /**
     * The function will return the description of the program
     * @return the description of the program
     */
    public String getProgramDescription() {
        return programDescription;
    }

    /**
     * The function will set the description of the program
     * @param programDescription the description of the function
     */
    public void setProgramDescription(String programDescription) {
        this.programDescription = programDescription;
    }

    /**
     * The function will return the date the program was saved on
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * The function will set the date the program was saved on
     * @param date the date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * The function will return the commands the needs to be displayed
     * @return the commands the needs to be displayed
     */
    public List<String> getProgramDisplay() {
        return programDisplay;
    }

    /**
     * The function will set the programs that needs to be displayed
     * @param programDisplay the program that needs to be displayed
     */
    public void setProgramDisplay(List<String> programDisplay) {
        this.programDisplay = programDisplay;
    }

    /**
     * The function will turn the commands that needs to be executed to a string
     * @return a string containing the commands to be executed separated with '\n'
     */
    public String toStringProgram(){
        String res = "";
        for(int i =0;i<this.getProgram().size() - 1;i++){
            res = res + this.getProgram().get(i);
            res = res + '\n';
        }
        res = res + this.getProgram().get(this.getProgram().size() - 1);
        return res;
    }

    /**
     * The function will turn the commands that needs to be displayed to a string
     * @return a string containing the commands to be displayed separated with '\n'
     */
    public String toStringProgramDisplay(){
        String res = "";
        for(int i =0;i<this.getProgramDisplay().size() - 1;i++){
            res = res + this.getProgramDisplay().get(i);
            res = res + '\n';
        }
        res = res + this.getProgramDisplay().get(this.getProgramDisplay().size() - 1);
        return res;
    }
}
