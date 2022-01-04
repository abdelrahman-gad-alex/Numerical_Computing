package com.example.system.of.linear.equations.SystemModels;

import java.util.LinkedList;

public interface ISystem {
    void setSystem(double[][]A, int figures) ;
    double[] solve();

    LinkedList getSteps();
    boolean getHasSolution();
    double getTime();

}
