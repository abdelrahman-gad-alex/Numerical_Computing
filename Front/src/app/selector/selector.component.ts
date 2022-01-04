import { Component, OnInit , Output , EventEmitter } from '@angular/core';

@Component({
  selector: 'app-selector',
  templateUrl: './selector.component.html',
  styleUrls: ['./selector.component.css']
})
export class SelectorComponent implements OnInit {
  @Output() emitter:EventEmitter<number>=new EventEmitter();


  constructor() {}
  
  ngOnInit(): void {}
  
  res:any // a global variaple to store the response from the back end


  handleSwitch() {// a function to change the options displayed based on the mode selected
  
    var selectedindex=((document.getElementById("method"))as HTMLSelectElement)!.selectedIndex;
    var opt =((document.getElementById("method"))as HTMLSelectElement)!.options[selectedindex];
    var valueString = (opt).value;
    var val:number =+valueString; 
    var currentMode=val;
    switch(currentMode){
      case 3:
        document.getElementById("Error")!.style.display = "none";
        document.getElementById("iterations")!.style.display = "none";
        document.getElementById("intial")!.style.display = "none";  
        document.getElementById("LU-type")!.style.display = "flex";
        document.getElementById("textBox")!.style.marginLeft= "0%";        
        break;  
      case 2:
      case 4:
        document.getElementById("Error")!.style.display = "flex";
        document.getElementById("iterations")!.style.display = "flex";
        document.getElementById("intial")!.style.display = "flex";  
        document.getElementById("LU-type")!.style.display = "none";      
        document.getElementById("textBox")!.style.marginLeft= "0%";        
        break;
        case 0:
        case 1:
          document.getElementById("Error")!.style.display = "none";
          document.getElementById("iterations")!.style.display = "none";
          document.getElementById("intial")!.style.display = "none";  
          document.getElementById("LU-type")!.style.display = "none";
          document.getElementById("textBox")!.style.marginLeft= "100%";      
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
    this.emitter.emit()
  }
}