package com.example.system.of.linear.equations.SystemModels;

import java.util.LinkedList;

public class Jacobi implements ISystem{
    public double[][] systemA ;
    public double[] systemB ;
    int n ;
    double es = 1e-10 ;
    int figures ;
    boolean hasSolution = true ;
    LinkedList<double[]> steps ;
    int iterations  ;
    double []initial ;
    double error ;
    double time;


    public Jacobi(int iterations ,double []initial,double error){
        this.initial = initial ;
        this.iterations = iterations ;
        this.error = error ;
    }


    public void setSystem(double[][] A, int f){// set the matrix A and matrix B
        figures = f ;
        systemA = new double[A.length][A[0].length-1] ;
        systemB = new double[A[0].length] ;
        n = systemA.length ;
        for(int i=0 ; i<n ; i++){
            for(int j=0 ; j<n+1 ; j++) {
                if (j < n ) {
                    systemA[i][j] = round(A[i][j]);
                }
            }
            systemB[i] = A[i][n] ;
        }
    }

    public double[] solve() {
        long start = System.nanoTime();
        steps = new LinkedList<>();
        boolean checkError=false; // boolean to check the stop condition
        double[] newInitial = new double[initial.length]; // array to store the new solutions
        while(iterations!=0 && !checkError){
            for (int i =0 ; i< systemA.length;i++){
                double x0 =0;
                for (int j =0 ; j <systemA.length;j++){
                    if(i != j){
                        x0 += systemA[i][j]* initial[j];
                    }
                    newInitial[i] = round((systemB[i]-x0)/systemA[i][i]); //compute new solution
                }
            }

            steps.add(copy(newInitial));
            checkError = check(newInitial,initial,error);
            for(int m = 0 ; m< initial.length; m++){
               initial[m] =newInitial[m]; // store the solutions before the next iteration
             }
            iterations--;
        }
        long end = System.nanoTime();
        time = (end-start) / 1e6 ;
        return newInitial;
    }
    public boolean check(double []New ,double[]old, double error){

        for(int i = 0 ; i< New.length;i++) {
         if(Math.abs(New[i]-old[i])>=error){
                 return false ;
            }
            return true;
        }
        return false;

    }

    public LinkedList<double[]> getSteps() {
        return steps;
    }
    public boolean getHasSolution(){
        return hasSolution;
    }

    double round(double x){
        if(Math.abs(x)< es){
            return 0 ;
        }

        int shift = (int)Math.ceil(Math.log10(x)) - figures;
        x = x/ Math.pow(10, shift) ;
        x = Math.round(x) ;
        x = x*  Math.pow(10, shift) ;
        x = Double.parseDouble( String.format("%.10g%n",x) ) ;
        return x ;
    }
    public double getTime(){
        return time ;
    }
    double[] copy(double[] matrix){
        double[] n = new double[matrix.length] ;
        for(int i=0; i<matrix.length; i++){
            n[i]=matrix[i];
        }
        return n ;
    }
}
