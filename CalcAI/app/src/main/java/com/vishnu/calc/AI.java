package com.vishnu.calc;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * @author life
 */

import org.javia.arity.*;

public class AI {

    public String result = "", formula, Processedformula;
    public String[] datas;
    public int _operant = 0, _operator = 0;

    public void parseString(String arg1) {
        int i = 0;
        formula = arg1;
        Processedformula = formula;
        datas = new String[arg1.length()];
        for (String retval : arg1.split("\\+|\\-|\\*|\\/|\\^|\\%|\\=\\?")) {
            _operant++;
            datas[i] = retval + "";
            i++;
        }
        changeConstent();
    }

    public int countOperant() {
        return _operant;
    }

    @Deprecated
    public String[] getOperant() {
        return datas;
    }

    @Deprecated
    public String getOperantAt(int arg1) {
        return datas[arg1];
    }

    public String result() throws Exception {
        Symbols s = new Symbols();
        double ret = s.eval(Processedformula);
        return ret + "";
    }

    public void setValues(String variable, String value) {
        Processedformula = Processedformula.replace(variable, value);
    }

    void changeConstent() {
        Processedformula = Processedformula.replace("π", "pi");
        Processedformula = Processedformula.replace("E", Math.E + "");
        Processedformula = Processedformula.replace("√", "sqrt");
    }

    @Deprecated
    public String printFormula() {/* This is return only the processed formula*/
        return Processedformula;
    }

    public String[] printFormulas() {
        String[] temp = {Processedformula, formula};
        return temp;
    }

    @Deprecated
    void getInputs() {
        for (int i = 0; i < _operant; i++) {
            if (Character.isLetter(Processedformula.charAt(i)) || isIgnored(Processedformula.charAt(i))) {
                System.out.println("Enter the value for " + Processedformula.charAt(i));
                if ((Processedformula.charAt(i) + "").equals("a")) {
                    setValues("a", "1");
                }
                if ((Processedformula.charAt(i) + "").equals("b")) {
                    setValues("b", "1");
                }
            }
        }
    }

    boolean isIgnored(char arg1) {
        switch (arg1) {
            case 'i':
                return true;
            case 'π':
                return true;
            case 'e':
                return true;
            case 'E':
                return true;
            case 'j':
                return true;
            default:
                return false;
        }
    }
}
