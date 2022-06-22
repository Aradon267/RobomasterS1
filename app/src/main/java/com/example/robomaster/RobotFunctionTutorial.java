package com.example.robomaster;

/**
 * The class will hold the information on each function and use it at the custom array adapter.
 * funcName - the name of the functionality.
 * funcDescStart - the beginning of the description of the functionality(will be displayed in the listview).
 * funcDescRest - the ending of the description of the functionality(will not be displayed in the listview).
 * funcPara - the parameters of the functionality and explanation on each parameter.
 * imgSrc - a link to the picture we want to be displayed in the listview.
 */

public class RobotFunctionTutorial {

    public String funcName;
    public String funcDescStart;
    public String funcDescRest;
    public String funcPara;
    public String imgSrc;

    /**
     * An empty constructor of a function
     */
    public RobotFunctionTutorial(){}

    /**
     * A regular constructor of a function
     * @param name the name of the function
     * @param desc the beginning of the description of the function
     * @param descRest the rest of the description of the function
     * @param para the parameters the function gets
     * @param imgSrc the image representing the function
     */
    public RobotFunctionTutorial(String name, String desc, String descRest, String para, String imgSrc){
        this.funcName = name;
        this.funcDescStart = desc;
        this.funcDescRest = descRest;
        this.funcPara = para;
        this.imgSrc = imgSrc;
    }

    /**
     * The function will return the beginning of the description
     * @return the beginning of the description
     */
    public String getFuncDesc() {
        return funcDescStart;
    }

    /**
     * The function will return the rest of the description
     * @return the rest of the description
     */
    public String getFuncDescRest() {
        return funcDescRest;
    }

    /**
     * The function will return the name of the function
     * @return the name of the function
     */
    public String getFuncName() {
        return funcName;
    }

    /**
     * The function will return the parameters of the function
     * @return the parameters of the function
     */
    public String getFuncPara() {
        return funcPara;
    }

    /**
     * The function will return the link to the image of the function
     * @return the link to the image of the function
     */
    public String getImg(){
        return this.imgSrc;
    }

    /**
     * The function will set the name of the function
     * @param funcName the name of the function
     */
    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    /**
     * The function will set the beginning of the description
     * @param funcDescStart the beginning of the description
     */
    public void setFuncDescStart(String funcDescStart) {
        this.funcDescStart = funcDescStart;
    }

    /**
     * The function will set the ending of the description
     * @param funcDescRest the rest of the description
     */
    public void setFuncDescRest(String funcDescRest) {
        this.funcDescRest = funcDescRest;
    }

    /**
     * The function will set the parameters of the function
     * @param funcPara the parameters of the function
     */
    public void setFuncPara(String funcPara) {
        this.funcPara = funcPara;
    }

    /**
     * The function will set the image of the function
     * @param imgSrc the image of the function
     */
    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }
}
