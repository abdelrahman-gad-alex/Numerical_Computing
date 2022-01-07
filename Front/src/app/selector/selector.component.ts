import { Component, OnInit , Output , EventEmitter } from '@angular/core';
import { Globals } from '../Globals';

@Component({
  selector: 'app-selector',
  templateUrl: './selector.component.html',
  styleUrls: ['./selector.component.css']
})
export class SelectorComponent implements OnInit {
  @Output() emitter:EventEmitter<number>=new EventEmitter();


  constructor() {}
  
  ngOnInit(): void {
  }
  fun1(x: number) {return eval(Globals.drawEquation);  }
  fun2(x: number) {return Math.cos(3*x);}
 draw(max:number) {
 var canvas = <HTMLCanvasElement>document.getElementById("canvas");
 if (null==canvas || !canvas.getContext) return;

 var axes:any={}, ctx=canvas.getContext("2d");
 axes.x0 = .5 + .5*canvas.width;  // x0 pixels from left to x=0
 axes.y0 = .5 + .5*canvas.height; // y0 pixels from top to y=0
 axes.scale = (canvas.width-20)/(2*max);  
 axes.max = max               // 40 pixels from x=0 to x=1
 axes.doNegativeX = true;

 this.showAxes(ctx!,axes);
 this.funGraph(ctx!,axes,this.fun1,"rgb(11,153,11)",1); 

}

 funGraph (ctx: { canvas: { width: number; }; beginPath: () => void; lineWidth: any; strokeStyle: any; moveTo: (arg0: any, arg1: number) => void; lineTo: (arg0: any, arg1: number) => void; stroke: () => void; },axes: { x0?: any; y0?: any; scale?: any; doNegativeX?: any; },func: { (x: any): number; (x: any): number; (arg0: number): number; },color: string,thick: number) {
 var xx, yy, dx=1, x0=axes.x0, y0=axes.y0, scale=axes.scale;
 var iMax = Math.round((ctx.canvas.width-x0)/dx);
 var iMin = axes.doNegativeX ? Math.round(-x0/dx) : 0;
 ctx.beginPath();
 ctx.lineWidth = 2;
 ctx.strokeStyle = color;
 for (var i=iMin;i<=iMax;i+=0.1) {
  xx = dx*i; yy = scale*func(xx/scale);
  if (yy==Infinity||(Math.abs(yy-(scale*func((i-1)/scale)))>1000&&yy*(scale*func((i-1)/scale))<0)){
    ctx.stroke()
    ctx.beginPath()
  }
  else if (i==iMin) {
    ctx.moveTo(x0+xx,y0-yy);
  }
  else {
    ctx.lineTo(x0+xx,y0-yy);}
 }
 ctx.stroke();
}

 showAxes(ctx: CanvasRenderingContext2D,axes: { x0?: any; y0?: any; doNegativeX?: any; scale?: any; max?: any; }) {
 var x0=axes.x0, w=ctx.canvas.width;
 var y0=axes.y0, h=ctx.canvas.height;
 var scale=axes.scale;
 var xmin = axes.doNegativeX ? 0 : x0;
 ctx.beginPath();
 ctx.strokeStyle = "rgb(0,0,0)"; 
 ctx.moveTo(xmin,y0); ctx.lineTo(w,y0);  // X axis
 ctx.moveTo(x0,0);    ctx.lineTo(x0,h);  // Y axis
 ctx.stroke();

 for (let i = -(axes.max) ;i<=(axes.max);i++){
    ctx!.fillText(i.toString(), (ctx.canvas.width / 2) + (scale*i), (ctx.canvas.height / 2) + 10);
    if(i!=0){
      ctx!.beginPath();
      ctx.strokeStyle = "rgb(225,225,225)"; 
      ctx.moveTo(xmin,(ctx.canvas.height / 2) - (scale*i)); ctx.lineTo(w,(ctx.canvas.height / 2) - (scale*i));  // X axis
      ctx.moveTo((ctx.canvas.width / 2) + (scale*i),0);    ctx.lineTo((ctx.canvas.width / 2) + (scale*i),h);  // Y axis
      ctx.stroke();
    
      ctx!.fillText(i.toString(), (ctx.canvas.width / 2) + 8, (ctx.canvas.height / 2) +3- (scale*i));
      ctx!.fillText("__", (ctx.canvas.width / 2) -5, (ctx.canvas.height / 2) -1- (scale*i));
      ctx!.fillText("|", (ctx.canvas.width / 2) -1+ (scale*i), (ctx.canvas.height / 2) +3);
    }
 }
}
// JavaScript source code goes here

  
  
  res:any // a global variaple to store the response from the back end


  handleSwitch() {// a function to change the options displayed based on the mode selected
  
    var selectedindex=((document.getElementById("method"))as HTMLSelectElement)!.selectedIndex;
    var opt =((document.getElementById("method"))as HTMLSelectElement)!.options[selectedindex];
    var valueString = (opt).value;
    var val:number =+valueString; 
    var currentMode=val;
    switch(currentMode){
      case 0:
      case 1:
        document.getElementById("Lower")!.style.display = "none";
        document.getElementById("Upper")!.style.display = "none";
        document.getElementById("Error")!.style.display = "none";
        document.getElementById("Guess")!.style.display = "none";   
        document.getElementById("Multi")!.style.display = "none";
        document.getElementById("iterations")!.style.display = "none";
        document.getElementById("intial")!.style.display = "none";  
        document.getElementById("LU-type")!.style.display = "none";
        document.getElementById("Guessx0")!.style.display = "none"; 
        document.getElementById("Guessx-1")!.style.display = "none"; 
        document.getElementById("textBox")!.style.marginLeft= "100%";      
        break;  
      
      case 2:
      case 4:
        document.getElementById("Lower")!.style.display = "none";
        document.getElementById("Upper")!.style.display = "none";
        document.getElementById("Multi")!.style.display = "none";
        document.getElementById("Guess")!.style.display = "none";   
        document.getElementById("LU-type")!.style.display = "none";  
        document.getElementById("Guessx0")!.style.display = "none"; 
        document.getElementById("Guessx-1")!.style.display = "none"; 
        document.getElementById("Error")!.style.display = "flex";
        document.getElementById("iterations")!.style.display = "flex";
        document.getElementById("intial")!.style.display = "flex";     
        document.getElementById("textBox")!.style.marginLeft= "0%";        
        break;
  
      case 3:
        document.getElementById("Lower")!.style.display = "none";
        document.getElementById("Upper")!.style.display = "none";
        document.getElementById("Error")!.style.display = "none";
        document.getElementById("Multi")!.style.display = "none";
        document.getElementById("Guess")!.style.display = "none";   
        document.getElementById("intial")!.style.display = "none";  
        document.getElementById("iterations")!.style.display = "none";
        document.getElementById("Guessx0")!.style.display = "none"; 
        document.getElementById("Guessx-1")!.style.display = "none"; 
        document.getElementById("LU-type")!.style.display = "flex";
        document.getElementById("textBox")!.style.marginLeft= "0%";        
        break;  
        
      case 5:
      case 6:

        document.getElementById("Multi")!.style.display = "none";
        document.getElementById("intial")!.style.display = "none";  
        document.getElementById("LU-type")!.style.display = "none";      
        document.getElementById("Guess")!.style.display = "none";
        document.getElementById("Guessx0")!.style.display = "none"; 
        document.getElementById("Guessx-1")!.style.display = "none"; 
        document.getElementById("Lower")!.style.display = "flex";
        document.getElementById("Upper")!.style.display = "flex";
        document.getElementById("Error")!.style.display = "flex";
        document.getElementById("iterations")!.style.display = "flex"; 
        document.getElementById("textBox")!.style.marginLeft= "0%";     

        break;      
      case 7:
      case 8:
      case 10:
        document.getElementById("Lower")!.style.display = "none";
        document.getElementById("Upper")!.style.display = "none";
        document.getElementById("Multi")!.style.display = "none";
        document.getElementById("intial")!.style.display = "none";  
        document.getElementById("LU-type")!.style.display = "none"; 
        document.getElementById("Guessx0")!.style.display = "none"; 
        document.getElementById("Guessx-1")!.style.display = "none"; 
        document.getElementById("Error")!.style.display = "flex";
        document.getElementById("iterations")!.style.display = "flex";
        document.getElementById("Guess")!.style.display = "flex";
        document.getElementById("textBox")!.style.marginLeft= "0%";        
        break;
        case 9:
          document.getElementById("Lower")!.style.display = "none";
          document.getElementById("Upper")!.style.display = "none";
          document.getElementById("Error")!.style.display = "flex";
          document.getElementById("Multi")!.style.display = "flex";
          document.getElementById("intial")!.style.display = "none";  
          document.getElementById("LU-type")!.style.display = "none";  
          document.getElementById("Guessx0")!.style.display = "none"; 
          document.getElementById("Guessx-1")!.style.display = "none";   
          document.getElementById("iterations")!.style.display = "flex";
          document.getElementById("Guess")!.style.display = "flex";
          document.getElementById("textBox")!.style.marginLeft= "0%";        
          break;
          case 11:
            document.getElementById("Lower")!.style.display = "none";
            document.getElementById("Upper")!.style.display = "none";
            document.getElementById("Multi")!.style.display = "none";
            document.getElementById("intial")!.style.display = "none";  
            document.getElementById("LU-type")!.style.display = "none";
            document.getElementById("Guess")!.style.display = "none"; 
            document.getElementById("Error")!.style.display = "flex";
            document.getElementById("iterations")!.style.display = "flex";
            document.getElementById("Guessx0")!.style.display = "flex"; 
            document.getElementById("Guessx-1")!.style.display = "flex"; 
            document.getElementById("textBox")!.style.marginLeft= "0%";            
          break;
            }
    
  }

  handleSteps() {// This is the function that displays the data 
    this.clear()
    var selectedindex=((document.getElementById("method"))as HTMLSelectElement)!.selectedIndex;
    var opt =((document.getElementById("method"))as HTMLSelectElement)!.options[selectedindex];
    var valueString = (opt).value;
    var val:number =+valueString; 
    var currentMode=val;
    document.getElementById("steps")!.style.display="block"
    if(currentMode<5){
      this.final()
      switch(currentMode){
        case 0:
        case 1:
          this.gSteps()
          break;

        case 2:
        case 4:
          this.jgsSteps()
          break; 
          
        case 3:
          this.luSteps() 
      }
    }
    else{
      this.finalNL(currentMode)
    }
  }

  finalNL(currentMode:number){// a function to show the final answers and the time
      var span =document.getElementById("final"+"0")
      span!.style.display = "block"
      span!.innerText=("x= "+ this.res.res.toString())
      var ss=document.getElementById("stepss")
      ss!.replaceChildren("")
      for(let i of this.res.steps ){
        var nw=document.createElement("span")
        nw.style.fontSize="30px"
        nw.style.fontStyle="bold"        
        switch(currentMode){
          case 5:
          case 6:
            nw.innerText=("xl= "+i.xl+" ,xr= "+i.xr+" ,f(xr)= "+i.fxr+"\n xu= "+i.xu +" ,ea= "+i.ea+"\n\n")
            break;
          case 8:
          case 9:
            nw.innerText=("xi= "+i.xi+" ,f(xi)= "+i.fxi+"\n xi+1= "+i.xi1+" ,f(xi+1)= "+i.fxi1+"\n f'(xi)= "+i.fdxi +" ,ea= "+i.ea +"\n\n")
            break;
              
        }
        ss!.appendChild(nw)
      }
    // var span =document.getElementById("runTime")
    // span!.style.display = "block"
    // span!.innerText=("Runtime is "+ this.res.time+"m.s")
  }









  luSteps(){ //a function to show the steps of the LU decompisation
    for (let z=0;z<3;z++){
      document.getElementById("span"+z.toString())!.style.display = "block";
      document.getElementById("mat"+z.toString())!.style.display = "block";
      var table: HTMLTableElement = <HTMLTableElement> document.getElementById("mat"+z.toString());
      for (let i=0;i<this.res.steps[0][0].length;i++){
        var row = table.insertRow(i);
        for (let j=0;j<this.res.steps[0].length;j++){
          var cell = row.insertCell(j);
          cell.innerHTML=this.res.steps[z][i][j].toString()
        }
      }
    }
  }

 
  gSteps(){//a function to show the steps of the gauss elimination and gauss-jordan elimination
    console.log(this.res)
    for (let z=1;z<=this.res.steps.length;z++){
      document.getElementById("mat"+z.toString()+"G")!.style.display = "block";
      var table: HTMLTableElement = <HTMLTableElement> document.getElementById("mat"+z.toString()+"G");
      for (let i=0;i<this.res.steps[0].length;i++){
        var row = table.insertRow(i);
        for (let j=0;j<this.res.steps[0][0].length;j++){
          var cell = row.insertCell(j);
          cell.innerHTML=this.res.steps[z-1][i][j].toString()
          if (j==4){
            cell.style.borderLeft="10px"
          }
        }
      }
    }
  }


  jgsSteps(){//a function to show the steps of the jacobi and gauss seidal decompostions
    
    for (let z=0;z<this.res.steps.length;z++){
      document.getElementById("mat"+z.toString()+"G")!.style.display = "block";
      var table: HTMLTableElement = <HTMLTableElement> document.getElementById("mat"+z.toString()+"G");
      var row = table.insertRow(0);
      for (let j=0;j<this.res.steps[0].length;j++){
        var cell = row.insertCell(j);
        cell.innerHTML=this.res.steps[z][j].toString()
      }
    }
  }



  
  final(){// a function to show the final answers and the time
    for (let i =0 ; i<this.res.varNames.length ; i++){
      var span =document.getElementById("final"+i.toString())
      span!.style.display = "block"
      span!.innerText=(this.res.varNames[i]+" = "+ this.res.finalSol[i].toString())
    }
    var span =document.getElementById("runTime")
    span!.style.display = "block"
    span!.innerText=("Runtime is "+ this.res.time+"m.s")
  }

  clear(){// a function to clear the screen before showing new answers
    for (let i =0 ; i<6 ; i++)
      document.getElementById("final"+i.toString())!.style.display="none"
    for (let i =0 ; i<3 ; i++){
      try{
        document.getElementById("span"+i.toString())!.style.display = "none";
        document.getElementById("mat"+i.toString())!.style.display = "none";  
        (<HTMLTableElement>document.getElementById("mat"+i.toString()))!.deleteRow(0);
        (<HTMLTableElement>document.getElementById("mat"+i.toString()))!.deleteRow(0);
        (<HTMLTableElement>document.getElementById("mat"+i.toString()))!.deleteRow(0);
        (<HTMLTableElement>document.getElementById("mat"+i.toString()))!.deleteRow(0);
        (<HTMLTableElement>document.getElementById("mat"+i.toString()))!.deleteRow(0);
        (<HTMLTableElement>document.getElementById("mat"+i.toString()))!.deleteRow(0);
      }
      catch(e){}
    }
    for (let i =0 ; i<=30 ; i++){
      try{
        document.getElementById("mat"+i.toString()+"G")!.style.display = "none";
        (<HTMLTableElement>document.getElementById("mat"+i.toString()+"G"))!.deleteRow(0);
        (<HTMLTableElement>document.getElementById("mat"+i.toString()+"G"))!.deleteRow(0);
        (<HTMLTableElement>document.getElementById("mat"+i.toString()+"G"))!.deleteRow(0);
        (<HTMLTableElement>document.getElementById("mat"+i.toString()+"G"))!.deleteRow(0);
        (<HTMLTableElement>document.getElementById("mat"+i.toString()+"G"))!.deleteRow(0);
        (<HTMLTableElement>document.getElementById("mat"+i.toString()+"G"))!.deleteRow(0);
      }
      catch(e){} 
    }
    document.getElementById("runTime")!.style.display = "none";
    document.getElementById("steps")!.style.display = "none";
  }


  handleSubmit(){
    var canvas = <HTMLCanvasElement>document.getElementById("canvas");

    canvas.getContext("2d")!.clearRect(0, 0, canvas.width, canvas.height);
    this.emitter.emit()
    console.log(Globals.drawEquation)
    this.draw(15);
  }
}