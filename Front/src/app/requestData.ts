export class requestData{
    itr:number=50
    fig:number=10
    EPS:number=0.00001
    func:string="";
}

export class bisection extends requestData{
    userGuess:boolean=false;
    xu:number=0
    xl:number=1
}

export class falsePosition extends requestData{
    userGuess:boolean=false;
    xu:number=0
    xl:number=1
}

export class newtonRaphson extends requestData{
    type:string="original"
    userGuess:boolean=false
    x0:number=0;
    m:number=1;
}

export class fixedPoint extends requestData{
    userGuess:boolean=false;
    x0:number=0
}
export class secant extends requestData{
    userGuess:boolean=false;
    x0:number=0
    x1:number=1
}