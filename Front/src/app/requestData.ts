export class requestData{
    itr:number=50
    fig:number=10
    EPS:number=0.00001
    func:string="";
    constructor(){
        this.itr=50
        this.fig=10
        this.EPS=0.00002
        this.func="";
        console.log("called parent constructor");
        
    }
}

export class bisection extends requestData{
    userGuess:boolean=false;
    xu:number=0
    xl:number=1
    constructor(){
        super();
        this.userGuess=false
        this.xl=0
        this.xu=1
    }
}

export class falsePosition extends requestData{
    userGuess:boolean=false;
    xl:number=0
    xu:number=1
    constructor(){
        super();
        this.userGuess=false
        this.xl=0
        this.xu=1
    }
}

export class newtonRaphson extends requestData{
    type:string="original"
    userGuess:boolean=false
    x0:number=0;
    m:number=1;
    constructor(){
        super();
        this.userGuess=false
        this.x0=0
        this.m=1
    }
}

export class fixedPoint extends requestData{
    userGuess:boolean=false;
    x0:number=0
    constructor(){
        super();
        this.userGuess=false
        this.x0=0
    }
}
export class secant extends requestData{
    userGuess:boolean=false;
    x0:number=0
    x1:number=1
    constructor(){
        super();
        this.userGuess=false
        this.x0=0
        this.x1=1
    }
}