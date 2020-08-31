//global variable making this a singleton
import Subscriber from './subscriber';

//generic static export functions
export default class ApplicationState {
    
    private static instance: ApplicationState = new ApplicationState();

    //key value pairs like: person.firstName: 'GIL'
    private static states: Map<String, any> = new Map<String, any>();

    //an entry here has state as a key and callback function [] as value
    private static stateSubscribers: Map<String, Set<Subscriber>> = new Map<String, Set<Subscriber>>();

    //private static stateCallbackSubscribers: Map<String, Set<any>> = new Map<String, Set<any>>();

    private constructor() {
        
    }

    //implemented as a singleton
    public static getInstance() : ApplicationState{
        return ApplicationState.instance;
    }
    
    public publish(state: String, newValue: any){
        let oldValue: any = ApplicationState.states.get(state);
        ApplicationState.states.set(state, newValue);
        //TODO: looks for any subscribers we should notify
        
        let subscribers: any;
        if( ! ApplicationState.stateSubscribers.has(state) ){
            subscribers = new Set<Subscriber>();
            ApplicationState.stateSubscribers.set(state, subscribers);
        } else {
            subscribers = ApplicationState.stateSubscribers.get(state);
        }
        
        subscribers.forEach( (subscriber: Subscriber) => {
            try{
                subscriber.stateChanged(state, oldValue, newValue);
            }catch(e){
                console.error(e);
            }
        })
        

        

        // console.log('publish new state ' + state);
        // let callbackSubscribers: any;
        // callbackSubscribers = ApplicationState.stateCallbackSubscribers.get(state);
        // if(callbackSubscribers){
        //     callbackSubscribers.forEach( (callback: any) => {
        //         console.log('sending state to callback');
        //         callback (state, oldValue, newValue);
        //     })
        // }
    }

    public subscribe(subscriber: Subscriber, state: String){
        let subscribers: any;
        if( ! ApplicationState.stateSubscribers.has(state) ){
            subscribers = new Set<Subscriber>();
            ApplicationState.stateSubscribers.set(state, subscribers);
        } else {
            subscribers = ApplicationState.stateSubscribers.get(state);
        }
        subscribers.add(subscriber);
    }

    // public subscribeWithCallback(callback: any, state: String){
    //     let callbackSubscribers: any;
    //     if( ! ApplicationState.stateCallbackSubscribers.has(state) ){
    //         callbackSubscribers = new Set<any>();
    //         ApplicationState.stateSubscribers.set(state, callbackSubscribers);
    //     } else {
    //         callbackSubscribers = ApplicationState.stateCallbackSubscribers.get(state);
    //     }
    //     callbackSubscribers.add(callback);
    // }
    
    public unsubscribe(subscriber: Subscriber, state: String){
        if( ApplicationState.stateSubscribers.has(state) ){
            let subscribers = ApplicationState.stateSubscribers.get(state);
            if(subscribers){
                subscribers.delete(subscriber);
            }
        } 
    }

    // public unsubscribeCallback(callback: any, state: String){
    //     if( ApplicationState.stateCallbackSubscribers.has(state) ){
    //         let subscribers = ApplicationState.stateCallbackSubscribers.get(state);
    //         if(subscribers){
    //             subscribers.delete(callback);
    //         }
    //     } 
    // }
}

/*

    To subscrive to a state:<style scoped>
    1. import ApplicationState
    2. Subscribe on mount():<style scoped>
        ApplicationState.getInstance().subscribeWithCallback(
            this.myCallback, 
            'myStateName');
    3. add to methods:<style scoped>
        myCallback(state, oldValue, newValue){ 
            //do somthing here
        }

    To Publish:>
    ApplicationState.getInstance().publish('myStateName', 'stateValue');

*/