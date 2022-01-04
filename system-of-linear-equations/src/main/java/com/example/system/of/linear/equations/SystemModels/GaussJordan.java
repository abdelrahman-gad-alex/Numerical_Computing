package com.example.system.of.linear.equations.SystemModels;

import com.example.system.of.linear.equations.SystemModels.ISystem;

import java.util.LinkedList;

public class GaussJordan implements ISystem {
    public double[][] system ;
    // system = [ A  | b ]
    double[] solution ;
    int n ;
    double es = 1e-10 ;
    int figures ;
    boolean hasSolution ;
    LinkedList<double[][]> steps ;
    boolean infinite = false ;
    double time ;


    public LinkedList<double[][]> getSteps() {

        return steps;
    }
    public boolean getHasSolution(){
        return hasSolution;
    }
    public boolean isInfinite(){
        return infinite;
    }

    public void setSystem(double[][] A, int f){
        figures = f ;
        system = new double[A.length][A.length+1] ;
        n = system.length ;
        for(int i=0 ; i<n ; i++){
            for(int j=0 ; j<n+1 ; j++){
                system[i][j] = round(A[i][j]) ;
            }
        }
    }
    public double[] solve() {
        long start = System.nanoTime();


        steps = new LinkedList<>();
        eliminate();
        solution = new double[n];

        for (int i = 0; i < n; i++) {
            solution[i] = round(system[i][n]);
            System.out.println(solution[i]);

        }

        long endd = System.nanoTime();
        time = (endd - start) / 1e6;


        return solution;

    }

    public double getTime(){
        return time ;

    }

    private void eliminate(){
        System.out.println("in elimination");

        for(int i=0 ; i< n ; i++){
            // pivoting and scaling each of the rest rows
            if(i< n-1){
                pivoting(i);
            }
            print();




            if(Math.abs(system[i][i]) < es){
                continue;
            }else {
                pivotScaling(i);
            }

            for(int k=0 ; k<n ; k++){

                if(k==i || Math.abs(system[k][i]) < es){
                    continue;
                }

                double f = system[k][i]/ system[i][i] ;
                system[k][i] = 0 ;
                for(int j=i+1; j< n+1 ; j++){
                    system[k][j] =round(system[k][j]-f*system[i][j]) ;

                }
            }

            steps.add(copy(system)) ;
            print();

        }

        check();

    }

    void check(){
        for (int i=0 ; i<n ; i++){
            if(Math.abs(system[i][i]) < es){
                int j = i ;
                while (Math.abs(system[i][j]) < es){
                    if(j==n){
                        hasSolution = true ;
                        infinite = true ;
                        return;
                    }
                    j ++ ;
                }
                // i, j is the last pivot
                for(int i2= i+1 ; i2<n ; i2++){
                    if(Math.abs(system[i2][n]) > es){
                        hasSolution = false ;
                        infinite = false ;
                        return;
                    }
                }
                hasSolution = true ;
                infinite = true ;
                return;

            }
        }
        hasSolution = true ;
        infinite = false ;
    }


    private void pivoting(int j){
        int maxI = 0 ;
        double max = 0 ;
        for(int i=j ; i<n ; i++){
            if(Math.abs(system[i][j]) > Math.abs(max)) {
                max = system[i][j];
                maxI = i;
            }
        }
        if(Math.abs(max) > es)
            swap(maxI, j);
    }
    private void swap(int first, int second){
        for(int i=0 ; i<n+1 ; i++){
            double s = system[first][i] ;
            system[first][i] = system[second][i] ;
            system[second][i] = s;
        }
    }

    private void pivotScaling(int i){

        for(int j=i+1 ; j<n+1 ; j++){
            System.out.println(j);
            system[i][j] = round(system[i][j]/system[i][i]);
        }
        system[i][i] = 1 ;
    }


    public void print(){
        for(int i=0 ; i< system.length ; i++){
            for(int j=0 ; j< system[0].length ; j++){
                System.out.print(system[i][j]);
                System.out.print(" ");
            }
            System.out.println("");
        }
        System.out.println("");

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

    double[][] copy(double[][] matrix){
        double[][] n = new double[matrix.length][matrix[0].length] ;
        for(int i=0; i<matrix.length; i++){
            for(int j=0; j<matrix[i].length; j++)
                n[i][j]=matrix[i][j];
        }
        return n ;
    }

}
