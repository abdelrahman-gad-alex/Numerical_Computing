import { Component } from '@angular/core';
import { HttpClient, HttpHeaders,HttpParams } from "@angular/common/http";
import { SelectorComponent } from './selector/selector.component';
import { requestData } from './requestData';
import { JsonPipe } from '@angular/common';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})


export class AppComponent {
  solveGaussURL="http://localhost:8080/controller/gauss"
  solveSeidelURL="http://localhost:8080/controller/gaussSeidel"
  solveJacobiURL="http://localhost:8080/controller/jacobi"
  solveLUURL="http://localhost:8080/controller/lu"
  solveBisectionURL="http://localhost:8080/controller/bisection"


  title = 'Front';
  numberOfeq:number=2;
  strEq:string="";
  constructor(private httpclient :HttpClient){} 
  Selector = new SelectorComponent

  /**
   * This function is called when the submit button is pressed.
   * it separates the text by the newline character into multiple separate equations
   * then it calls the methods responsible for sending the request 
   */
  receiveInput(){
    var type = this.getMethodType();
    var txt :string = this.getEquations()
    var dividedTxt=txt.split(/\n/)
    var numberOfequations:number = dividedTxt.length
    this.numberOfeq=numberOfequations

    switch(type){
      case "Gauss-Elimination":
      case "Gauss-Jordan":
      case "Gauss-Seidel":
      case "LU-decomposition":
      case "Jacobi-Iterations":
      //regex to check the invalid format charcters in the string
      var groupLettersRegex =/[a-zA-Z]\d*\.?[a-zA-Z]|\(|\)|>|<|,/g
      if(groupLettersRegex.test(txt)===true)
      { 
        this.errorAlert(0)
        return;
      }
      for(let i=0;i<numberOfequations;i++){
        if(this.validateTwoSides(dividedTxt[i])===false) {
          this.errorAlert(0)
          return
        }
        var sides=dividedTxt[i].split(/=/)
        var LHS=sides[0]
        //checks if there is a free term on the left hand 
        var freeTermDetectionRegex=/(?<![a-zA-z\d])\d+\.?\d*?(?![\.\*a-zA-z\d])/gm
        if(freeTermDetectionRegex.test(LHS) === true) {
            this.errorAlert(0)
            return
        }

        //regex to prepare the input before sending to the back
          var evenNegRegex = /(?<!-)((?:--)+)(?!-)/g
          var oddNegRegex = /(?<!^|-|\+)(-(?:--)*)(?!-)/g
          var insertOneToLonelyRegex=/(?<![\d\*])(?=[a-zA-Z])/g
          var insertTimesRegex=/(?<=(\d*\.)?\d+)(?=[a-zA-z])/g 
          
          dividedTxt[i]= dividedTxt[i].replace(evenNegRegex,"+")
          dividedTxt[i]=dividedTxt[i].replace(oddNegRegex,"-")
          dividedTxt[i]=dividedTxt[i].replace(insertOneToLonelyRegex,"1")
          dividedTxt[i]=dividedTxt[i].replace(insertTimesRegex,"*");
          while(dividedTxt[i].includes("+"))
          {
            dividedTxt[i]=dividedTxt[i].replace("+","<");
          }
          dividedTxt[i]=dividedTxt[i].replace("=", ">")
          if(i<numberOfequations) this.strEq=this.strEq+dividedTxt[i]+"\n"
          else this.strEq=this.strEq+dividedTxt[i]

      }
      this.strEq=this.putComma(this.strEq)



        break;
      case "Bisection":
      case "False-Position":
      case "Fixed Point":
      case "Newton-Raphson":
      case "Secant Method":
        //must be one equation
        var fxRegex=/^f\(x\)=[a-z\d^+()]+(?!=)$/gm
        if(this.numberOfeq!==1 || !fxRegex.test(txt)){
          this.errorAlert(0);
          return
        }
            
        
        var evenNegRegex = /(?<!-)((?:--)+)(?!-)/g
        var oddNegRegex = /(?<!^|-|\+)(-(?:--)*)(?!-)/g
        var insertOneToLonelyRegex=/(?<![\d\*])(?=[a-zA-Z])/g
        var insertTimesRegex=/(?<=(\d*\.)?\d+)(?=[a-zA-z])/g 
        txt=txt.replace(evenNegRegex,"+")
        txt=txt.replace(oddNegRegex,"-")
        txt=txt.replace(insertOneToLonelyRegex,"1")
        txt=txt.replace(insertTimesRegex,"*");

        this.strEq=txt
        var validating=validTs(this.strEq)
        if(validating==="Invalid"){
          this.errorAlert(0)
          return;
        }
        break


    function validTs(s: string): any
      {
        if(!s.startsWith("f(x)="))
        {
            return "Invalid"
        }
        s = s.substring(s.indexOf("f(x)=")+5)
        while(s.includes("sin("))
        {
            s= s.replace("sin(", "Math.sIn(x)")
        }
        while(s.includes("Math.sIn(x)"))
        {
            s = s.replace("Math.sIn(x)", "Math.sin(")
        }
        while(s.includes("cos("))
        {
            s= s.replace("cos(", "Math.sIn(x)")
        }
        while(s.includes("Math.sIn(x)"))
        {
            s = s.replace("Math.sIn(x)", "Math.cos(")
        }
        while(s.includes("e"))
        {
            s= s.replace("e", "Math.sIn(x)")
        }
        while(s.includes("Math.sIn(x)"))
        {
            s = s.replace("Math.sIn(x)", "Math.E")
        }
      // s= s.replace("sin(x)", "Math.sin(x)")
      console.log(s)
      }
    }


    //SENDING REQUESTS SWITCH
    switch(type){
      case "Gauss-Elimination":
        this.solveAndReceiveAnswerGauss()
        break
      case "Gauss-Jordan":
        this.solveAndReceiveAnswerGauss()
        break;
      case "Gauss-Seidel":
        this.solveAndReceiveAnswerSeidel()
        break
      case "LU-decomposition":
        this.solveAndReceiveAnswerLU()
        break
      case "Jacobi-Iterations":
        this.solveAndReceiveAnswerJacobi()
        break
      case "Bisection":
        this.solveAndReceiveAnswerBisection()
        break;
      case "False-Position":
      case "Fixed Point":
      case "Newton-Raphson":
      case "Secant Method":

      default:
        break
    }
    this.strEq=""
  }


  /**
   * @returns the equations as a string from text area
   */
  getEquations():string{
    var txt=(<HTMLInputElement>(document.getElementById("textBox"))).value;
    return txt;
  }

  /**
   * 
   * @returns the method choosed
   */
  getMethodType():string {
    var type=(<HTMLSelectElement>(document.getElementById("method"))).selectedIndex;
    var choice:string ="Gauss-Elimination"
    switch(type){
      case 0:
        choice="Gauss-Elimination"
        break
      case 1:
        choice="Gauss-Jordan"
        break;
      case 2:
        choice="Gauss-Seidel"
        break
      case 3:
        choice="LU-decomposition"
        break
      case 4:
        choice="Jacobi-Iterations"
        break
      case 5:
        choice="Bisection"
        break;
      case 6:
        choice="False-Position"
        break;
      case 7:
        choice="Fixed Point"
        break;
      case 8:
        choice="Newton-Raphson"
        break;
      case 9:
        choice="Secant Method"
        break;
      default:
        break
    }
    return choice;
  }


  /**
   * 
   * @returns the chosen precision
   */
  getPrecision():number {
    var significantFigures=(<HTMLSelectElement>(document.getElementById("precision"))).selectedIndex +1;
    if(significantFigures==1) significantFigures=10;
    return significantFigures;
  }

  getLUtype():string{
    var type:number=(<HTMLSelectElement>(document.getElementById("LU-type-choice"))).selectedIndex;
    var choice:string= "Doolittle";
    switch(type){
      case 0:
        choice="doolittle"
        break
      case 1:
        choice="crout"
        break;

      default:
        break
    }
    return choice
  }


 

  


  /* 
   * @returns max number of iteration
   */
  getMaxNumberOfIterations():number{
    var n=(<HTMLInputElement>(document.getElementById("maxIterations"))).valueAsNumber;
    return n;
  }


  /*
   * @returns relative error to check in the operations
   */
  getRelativeError():number{
    var e=(<HTMLInputElement>(document.getElementById("relError"))).valueAsNumber; 
    return e;
  }
  

  /*
   * @returns initial conditions from its text area
   */
  getInitialConditions():string{
    var txt=(<HTMLInputElement>(document.getElementById("intial"))).value;
    return txt;
  }


  /**
   * alerts the user of errors
   */
  errorAlert(type:number=0){
    var message:string="Error in the input format";
    switch(type){
      case 0:
        message="Error in the input format"
        break;
      case 1:
        message="Enter positive values <=100 for the error and positive values for the max number of iterations"
        break;
      case 2:
        message="Enter the initial conditions"
        break;
      case 3:
        message="System has no solution"
        break;
      case 4:
        message="System has infinite number of solutions"
        break;


      case 5:
        message="Error"
        break
      default:
        message="Error"
        break
    }
    alert(message)
  }




  /**
   * prepares the string before sending it to backend
   */
  putComma(txt:string): string{
    var arr = txt.split("\n")
    var numberOfequations=arr.length
    var res:string=""
    for(let i=0;i<numberOfequations;i++){
      if(i<numberOfequations-1){
        res=res + arr[i]+","
      } 
    
      else{
        res=res+arr[i]
      } 

    }

    return res
  }

  validateTwoSides(txt:string):boolean{
    let regexp: RegExp = /^.+=\d+\.?\d*$/gm;
    return regexp.test(txt)
  }
  

  /**
   * the next equations sends all the needed requests to the backend
   */


  solveAndReceiveAnswerGauss(){
    var equations = this.strEq
    var choice=this.getMethodType()
    var prec=this.getPrecision()
    this.httpclient.get(this.solveGaussURL,{
              responseType:'text',
              params:{
                  first: choice,
                  sigFig: prec,
                  strEq: this.strEq
              },
              observe:'response'
            })
            .subscribe(response=>{      
              try{
                this.Selector.res=JSON.parse(<string>response.body)
                this.Selector.handleSteps()
              }catch(e){
                switch(<string>response.body)
                {
                  case "System has no solution":
                    this.errorAlert(3);
                    break;
                  case "System has infinite number of solutions":
                    this.errorAlert(4);
                    break;
                  case "Error":
                    this.errorAlert(0)
                    break
                  default:
                    this.errorAlert(0)
                    break
                    
                }

              }
              

            })
  }




  solveAndReceiveAnswerSeidel(){
    var choice=this.getMethodType()
    var prec=this.getPrecision()
    var guess=this.getInitialConditions()
    var commaGuess=this.putComma(guess)
    var maxN=this.getMaxNumberOfIterations()
    var tolerance=this.getRelativeError()
    if(maxN===NaN || tolerance===NaN || guess === null){
      this.errorAlert(0)
      return
    }
    if ( (tolerance<=0 || tolerance>100)&&((maxN>0)||(maxN<=30) ))
    {
      this.errorAlert(1)
    } 

    var checkInitalCondition=/[a-zA-Z]|[a-zA-Z]\d*\.?[a-zA-Z]?|\(|\)|>|<|,/g
    if(checkInitalCondition.test(guess)===true)
    {
      this.errorAlert(0)
      return
    }

    this.httpclient.get(this.solveSeidelURL,{
              responseType:'text',
              params:{
                  first: choice,
                  sigFig: prec,
                  strEq: this.strEq,
                  maxItr:maxN,
                  intVal:commaGuess,
                  maxError:tolerance
              },
              observe:'response'
            })
            .subscribe(response=>{            
              try{
                this.Selector.res=JSON.parse(<string>response.body)
                this.Selector.handleSteps()
              }catch(e){
                switch(<string>response.body)
                {
                  case "System has no solution":
                    this.errorAlert(3);
                    break;
                  case "System has infinite number of solutions":
                    this.errorAlert(4);
                    break;
                  case "Error":
                    this.errorAlert(0)
                    break

                    
                }

              }
            })
  }


  solveAndReceiveAnswerLU(){
    var choice=this.getMethodType()
    var prec=this.getPrecision()
    var typeLU=this.getLUtype()
    
    this.httpclient.get(this.solveLUURL,{
              responseType:'text',
              params:{
                  first: choice,
                  sigFig: prec,
                  strEq: this.strEq,
                  type:typeLU
              },
              observe:'response'
            })
            .subscribe(response=>{            
              try{
                console.log(response.body)
                this.Selector.res=JSON.parse(<string>response.body)
                this.Selector.handleSteps()
              }catch(e){
                
                switch(<string>response.body)
                {
                  case "System has no solution":
                    this.errorAlert(3);
                    break;
                  case "System has infinite number of solutions":
                    this.errorAlert(4);
                    break;
                  case "Error":
                    this.errorAlert(0)
                    break
         
                    
                }
              }
            })
  }
  
    
  
  
  solveAndReceiveAnswerJacobi(){
    var choice=this.getMethodType()
    var prec=this.getPrecision()
    var guess=this.getInitialConditions()
    var commaGuess=this.putComma(guess)
    var maxN=this.getMaxNumberOfIterations()
    var tolerance=this.getRelativeError()
    if(maxN===NaN || tolerance===NaN || guess === null){
      this.errorAlert(0)
      return
    }
    if ( (tolerance<=0 || tolerance>100)&&((maxN>0)||(maxN<=30) ))
    {
      this.errorAlert(1)
    } 
    this.httpclient.get(this.solveJacobiURL,{
              responseType:'text',
              params:{
                  first: choice,
                  sigFig: prec,
                  strEq: this.strEq,
                  maxItr:maxN,
                  intVal:commaGuess,
                  maxError:tolerance
              },
              observe:'response'
            })
            .subscribe(response=>{            
              try{
                this.Selector.res=JSON.parse(<string>response.body)
                this.Selector.handleSteps()
              }catch(e){
                switch(<string>response.body)
                {
                  case "System has no solution":
                    this.errorAlert(3);
                    break;
                  case "System has infinite number of solutions":
                    this.errorAlert(4);
                    break;
                  case "Error":
                    this.errorAlert(0)
                    break
          
                    
                }

              }
            })
  }
  


  solveAndReceiveAnswerBisection(){
    var reqBody:requestData=new requestData()
    reqBody.fig=this.getPrecision()
    if(this.getMaxNumberOfIterations()){
      reqBody.itr=this.getMaxNumberOfIterations()
    }
    else{
      reqBody.itr=50
    }

    reqBody.func=this.strEq
    reqBody.EPS=this.getRelativeError();
    if(this.getRelativeError())
      reqBody.EPS=this.getRelativeError()
    else
      reqBody.EPS=0.00001
    var strBody=JSON.stringify(reqBody)
    console.log(strBody)
    this.httpclient.post(this.solveBisectionURL,strBody,{responseType:'text'}).subscribe(response=>{
      if(response==='Invalid'){
        this.errorAlert(5)
      } else {
        console.log(response)
      }
    })


  }
}







