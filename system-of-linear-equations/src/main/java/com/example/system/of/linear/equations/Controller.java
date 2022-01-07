package com.example.system.of.linear.equations;

import com.example.system.of.linear.equations.Nonlinear.*;
//import com.example.system.of.linear.equations.Nonlinear.Bracketing;
import com.example.system.of.linear.equations.SystemModels.*;
import org.springframework.web.bind.annotation.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.Gson;
//import com.google.gson.Gson;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.LinkedList;


@RestController
@CrossOrigin
@RequestMapping("/controller")
public class Controller {

    int n = 0;
    String operationName;
    ArrayList<String> equ = new ArrayList<String>();
    ArrayList<String> variables = new ArrayList<String>();
    HashMap<String,Integer> varId = new HashMap<String,Integer>();
    double[][] x ;
    boolean error = false;
    int sigFig = 0;
    
    //It is used to set operation name and significant figures
    public void getOpertaions( String first,  int sigFig)
    {
        error = false;
        equ.clear();
        variables.clear();
        varId.clear();
        this.sigFig = sigFig;
        this.operationName = first;
    }
    //Checking if decimal number has more than one decimal point
    private boolean checkDot(String s)
    {
        int nDot = 0;
        for(int i = 0; i < s.length(); i++)
        {
            if(s.charAt(i) == '.')
            {
                nDot++;
            }
            if(nDot > 1)
            {
                return false;
            }
        }
        return true;
    }
    
    

    //Receiving equations sent by front and modify it to operate on it
    public void getEquation( String eq)
    {
        
        eq = eq.replaceAll("<", "+");
        eq = eq.replaceAll(">", "=");
        String[] tempArr = eq.split(",");
        for(int i = 0; i < tempArr.length; i++)
        {
            
            //System.out.println(eq);
            equ.add(tempArr[i]);
        }
    }
    
    
    
    //Validate the equations in addition to detecting coefficients and variable names
    boolean validateEquations()
    {
        n = equ.size();
        x= new double[n][n+1];
        //Setting array of coefficients values to zero
        for(int i = 0; i < n; i++)
        {
            for(int j = 0;j < n + 1; j++)
            {
                x[i][j] = 0;
            }
        }
        //Setting variable names and the coefficients
        //Then we see if the number of variables bigger than array we notify user
        for(int i = 0; i < n && !error; i++)
        {
            error = false;
            String currentEqu = equ.get(i);
            String newPart = "";
            double ln = 0;
            for(int j = 0; j < currentEqu.length(); j++)
            {
                char c = currentEqu.charAt(j);
                if((j - 1) >= 0 && currentEqu.charAt(j-1) == '=')
                {
                    String temp = currentEqu.substring(j);
                    if(!checkDot(temp))
                    {
                        error = true;
                        return error;
                    }
                    else
                    {
                        x[i][n] += Double.parseDouble(temp);
                    }
                }
                else if(c == '+' || c == '=')
                {
                    if(varId.containsKey(newPart))
                    {
                        x[i][varId.get(newPart)] += ln;
                    }
                    else
                    {
                        variables.add(newPart);
                        varId.put(newPart, variables.size() - 1);
                        if(variables.size() > n)
                        {
                            error = true;
                            return error;
                        }
                        x[i][varId.get(newPart)] += ln;
                    }
                    newPart = "";
                }
                else if(c == '*')
                {
                    if(checkDot(newPart))
                    {
                        ln = Double.parseDouble(newPart);
                    }
                    else
                    {
                        error = true;
                        return error;
                    }
                    newPart = "";
                }
                else
                {
                    newPart = newPart + c;
                }
            }
        }
        //We add variables names and related coefficients zero if the user entered less variable name
        //to set nxn matrix
        while(variables.size() < n)
        {
            int idx = variables.size();
            String temp = variables.get(idx - 1) + "$";
            varId.put(temp, idx);
            variables.add(temp);
        }
        return error;
    }
    
    
    
    Gson gson = new Gson();
    
    //we perform gauss elimination and gauss jordan elimination
    //Thus send solution
    //Through receiving its needed data from the user
    //Then mapping them to the class in order to calculate solution
    @GetMapping("/gauss")
    String sendSol(@RequestParam String first,@RequestParam  int sigFig, @RequestParam String strEq)
    {
    	
        getOpertaions(first, sigFig);
        getEquation(strEq);
        if(validateEquations())
        {
            return "Error";
        }
        if(first.equals("Gauss-Elimination" ))
        {
        	
            Gauss gauss = new Gauss();
            gauss.setSystem(x, sigFig);
            HashMap<String, Object> tempHM = new HashMap<String, Object>();

            double[] solution = gauss.solve();
            //If there is no solution or infinite number of solutions we notify user
            if(!gauss.getHasSolution())
            {
                return "System has no solution";
            }
            else if(gauss.isInfinite())
            {
                return "System has infinite number of solutions";
            }
            else
            {
                //we put the final solution, steps, time of calculation in addition to variable name
                //in hashMap then we convert it to json string to send it to be viewed to user
                tempHM.put("varNames", variables);
                tempHM.put("finalSol", solution);
                tempHM.put("steps", gauss.getSteps());
                tempHM.put("time", gauss.getTime());
                String ans = gson.toJson(tempHM);
                return ans;
            }
        }
        else if(first.equals("Gauss-Jordan") )
        {
            GaussJordan gaussJordan = new GaussJordan();
            gaussJordan.setSystem(x, n);
            HashMap<String, Object> tempHM = new HashMap<String, Object>();

            double[] solution = gaussJordan.solve();
            //If there is no solution or infinite number of solutions we notify user
            if(!gaussJordan.getHasSolution())
            {
                return "System has no solution";
            }
            else if(gaussJordan.isInfinite())
            {
                return "System has infinite number of solutions";
            }
            else
            {
                //we put the final solution, steps, time of calculation in addition to variable name
                //in hashMap then we convert it to json string to send it to be viewed to user
                tempHM.put("varNames", variables);
                tempHM.put("finalSol", solution);
                tempHM.put("steps", gaussJordan.getSteps());
                tempHM.put("time", gaussJordan.getTime());
                String ans = gson.toJson(tempHM);
                return ans;
            }

        }
        return "There is something wrong!!!";
    }
    //We perform gauss Seidel
    //Thus send solution
    //Through receiving its needed data from the user
    //Then mapping them to the class in order to calculate solution
    @GetMapping("/gaussSeidel")
    String gaussSeidel(@RequestParam String first,@RequestParam  int sigFig, @RequestParam String strEq, @RequestParam int maxItr,
                   @RequestParam String intVal, @RequestParam double maxError) {
        String[] temp = intVal.split(",");
        double[] intArr = new double[temp.length];
        for (int i = 0; i < temp.length; i++)
        {
            intArr[i] = Double.parseDouble(temp[i]);
        }
        GaussSeidel gs = new GaussSeidel(maxItr, intArr, maxError);
        getOpertaions(first, sigFig);
        getEquation(strEq);
        if(validateEquations())
        {
            return "Error";
        }
        gs.setSystem(x, sigFig);
        double[] tempAns = gs.solve();
        Gson gson = new Gson();
        HashMap<String, Object> tempHM = new HashMap<String, Object>();
        //we put the final solution, steps, time of calculation in addition to variable name
        //in hashMap then we convert it to json string to send it to be viewed to user
        tempHM.put("varNames", variables);
        tempHM.put("finalSol", tempAns);
        tempHM.put("steps", gs.getSteps());
        tempHM.put("time", gs.getTime());
        return gson.toJson(tempHM);
    }
    //We perform jacobi iteration
    //Thus send solution
    //Through receiving its needed data from the user
    //Then mapping them to the class in order to calculate solution
    @GetMapping("/jacobi")
    String solveJacobi(@RequestParam String first,@RequestParam  int sigFig, @RequestParam String strEq, @RequestParam int maxItr,
                   @RequestParam String intVal, @RequestParam double maxError) {
        System.out.println("jacob   fsghert");
        String[] temp = intVal.split(",");
        double[] intArr = new double[temp.length];
        for (int i = 0; i < temp.length; i++)
        {
            intArr[i] = Double.parseDouble(temp[i]);
        }
        Jacobi jac = new Jacobi(maxItr, intArr, maxError);
        getOpertaions(first, sigFig);
        getEquation(strEq);
        System.out.println(first+" " + sigFig + " " + strEq);
        if(validateEquations())
        {
            return "Error";
        }
        jac.setSystem(x, sigFig);
        double[] tempAns = jac.solve();
        Gson gson = new Gson();
        HashMap<String, Object> tempHM = new HashMap<String, Object>();
        //we put the final solution, steps, time of calculation in addition to variable name
        //in hashMap then we convert it to json string to send it to be viewed to user
        tempHM.put("varNames", variables);
        tempHM.put("finalSol", tempAns);
        tempHM.put("steps", jac.getSteps());
        tempHM.put("time", jac.getTime());
        return gson.toJson(tempHM);
    }
    //We perform LU decomposition
    //Thus send solution
    //Through receiving its needed data from the user
    //Then mapping them to the class in order to calculate solution
    @GetMapping("/lu")
    String solveLU(@RequestParam String first,@RequestParam  int sigFig, @RequestParam String strEq,
                       @RequestParam String type) {
        LU lu = new LU(type);
        getOpertaions(first, sigFig);
        getEquation(strEq);
        if(validateEquations())
        {
            return "Error";
        }
        lu.setSystem(x, sigFig);
        double[] tempAns = lu.solve();
        Gson gson = new Gson();
        HashMap<String, Object> tempHM = new HashMap<String, Object>();
        //we put the final solution, steps, time of calculation in addition to variable name
        //in hashMap then we convert it to json string to send it to be viewed to user
        tempHM.put("varNames", variables);
        tempHM.put("finalSol", tempAns);
        tempHM.put("steps", lu.getSteps());
        tempHM.put("time", lu.getTime());
        return gson.toJson(tempHM);
    }
    @PostMapping("/bisection")
    String solveBisection(@RequestBody String reqParam) throws JSONException
    {
        try
        {
            JSONObject jas = new JSONObject(reqParam);
            int fig = jas.getInt("fig");
            double EPS = jas.getDouble("EPS");
            int itr = jas.getInt("itr");
            String func = jas.getString("func");
            Bisection bisect = new Bisection();
            bisect.setEs(EPS);
            bisect.setMaxIterations(itr);
            bisect.setNoFigures(fig);
            boolean isGuess = jas.getBoolean("userGuess");
            if(isGuess)
            {
                double xu = jas.getDouble("xu");
                double xl = jas.getDouble("xl");
                bisect = bisect.setxlxu(0, 5);
            }
            double res = bisect.bisections(func);
            if(!bisect.HasSolution())
            {
            	System.out.println("NOT HAS SOLUTION");
                return "Invalid";
            }
            LinkedList<HashMap<String, Double>> steps = bisect.getSteps();
            
            Gson gson = new Gson();
            HashMap<String, Object> tempHM = new HashMap<String, Object>();
            tempHM.put("res", res);
            tempHM.put("steps", steps);
            String ans = gson.toJson(tempHM);
            
            return ans;
        }
        catch (Exception e)
        {
        	System.out.println("Error");
            return "Invalid";
       }

    }
    @PostMapping("/falsePosition")
    String solveFalsePos(@RequestBody String reqParam) throws JSONException
    {
        try
        {
            JSONObject jas = new JSONObject(reqParam);
            int fig = jas.getInt("fig");
            double EPS = jas.getDouble("EPS");
            int itr = jas.getInt("itr");
            String func = jas.getString("func");
            FalsePosition FP = new FalsePosition();
            FP.setEs(EPS);
            FP.setMaxIterations(itr);
            FP.setNoFigures(fig);
            boolean isGuess = jas.getBoolean("userGuess");
            if(isGuess)
            {
                double xu = jas.getDouble("xu");
                double xl = jas.getDouble("xl");
                FP.setxlxu(xl, xu);
            }
            double res = FP.falsePosition(func);
            if(!FP.HasSolution())
            {
                return "Invalid";
            }
            LinkedList<HashMap<String, Double>> steps = FP.getSteps();
            Gson gson = new Gson();
            HashMap<String, Object> tempHM = new HashMap<String, Object>();
            tempHM.put("res", res);
            tempHM.put("steps", steps);
            String ans = gson.toJson(tempHM);
            return ans;
        }
        catch (Exception e)
        {
            return "Invalid";
        }

    }
    @PostMapping("/newton")
    String solveNewtonRaphson(@RequestBody String reqParam) throws JSONException
    {
        try
        {
            JSONObject jas = new JSONObject(reqParam);
            int fig = jas.getInt("fig");
            double EPS = jas.getDouble("EPS");
            int itr = jas.getInt("itr");
            String func = jas.getString("func");
            String type = jas.getString("type");
//            double x0 = jas.getDouble("x0");
            boolean isGuess = jas.getBoolean("userGuess");
            NewtonRaphson NR = new NewtonRaphson();
            NR.setEs(EPS);
            NR.setMaxIterations(itr);
            NR.setNoFigures(fig);
            int m = jas.getInt("m");
            double res =0;
            if(isGuess)
            {
                double x0 = jas.getDouble("x0");
                NR.setx0(x0);
            }
            if(type.equals("original"))
            {
                res = NR.newton(func);
            }
            else if(type.equals("mod1"))
            {
                res = NR.newtonModify1(func, m);
            }
            else if(type.equals("mod2"))
            {
                res = NR.newtonModify2(func);
            }
            if(!NR.HasSolution())
            {
                return "Invalid";
            }
            LinkedList<HashMap<String, Double>> steps = NR.getSteps();
            Gson gson = new Gson();
            HashMap<String, Object> tempHM = new HashMap<String, Object>();
            tempHM.put("res", res);
            tempHM.put("steps", steps);
            String ans = gson.toJson(tempHM);
            return ans;
        }
        catch (Exception e)
        {
            return "Invalid";
        }
    }
    @PostMapping("/fixedPoint")
    String solveFixedPoint(@RequestBody String reqParam) throws JSONException
    {
        try
        {
            JSONObject jas = new JSONObject(reqParam);
            int fig = jas.getInt("fig");
            double EPS = jas.getDouble("EPS");
            int itr = jas.getInt("itr");
            String func = jas.getString("func");
            String type = jas.getString("type");
//            double x0 = jas.getDouble("x0");
            boolean isGuess = jas.getBoolean("userGuess");
            FixedPoint FP = new FixedPoint();
            FP.setEs(EPS);
            FP.setMaxIterations(itr);
            FP.setNoFigures(fig);
            double res =0;
            if(isGuess)
            {
                double x0 = jas.getDouble("x0");
//                FP.setx0(x0);
            }
            res = FP.fixedpt(func);
            LinkedList<HashMap<String, Double>> steps = FP.getSteps();
            Gson gson = new Gson();
            HashMap<String, Object> tempHM = new HashMap<String, Object>();
            tempHM.put("res", res);
            tempHM.put("steps", steps);
            String ans = gson.toJson(tempHM);
            return ans;
        }
        catch (Exception e)
        {
            return "Invalid";
        }
    }
    @PostMapping("/secant")
    String solveSecant(@RequestBody String reqParam) throws JSONException
    {
        try
        {
            JSONObject jas = new JSONObject(reqParam);
            int fig = jas.getInt("fig");
            double EPS = jas.getDouble("EPS");
            int itr = jas.getInt("itr");
            String func = jas.getString("func");
            boolean isGuess = jas.getBoolean("userGuess");
            Secant secant = new Secant();
            secant.setEs(EPS);
            secant.setMaxIterations(itr);
            secant.setNoFigures(fig);
            double res =0;
            if(isGuess)
            {
                double x0 = jas.getDouble("x0");
                double x1 = jas.getDouble("x1");
                secant.setx0(x0);
                secant.setx1(x1);
            }
            res = secant.secant(func);
            LinkedList<HashMap<String, Double>> steps = secant.getSteps();
            Gson gson = new Gson();
            HashMap<String, Object> tempHM = new HashMap<String, Object>();
            tempHM.put("res", res);
            tempHM.put("steps", steps);
            String ans = gson.toJson(tempHM);
            return ans;
        }
        catch (Exception e)
        {
            return "Invalid";
        }
    }


}