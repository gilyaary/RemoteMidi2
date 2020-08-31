//global variable making this a singleton


//generic static export functions
export default interface Subscriber {
    stateChanged(state: any, oldValue: any, newValue: any): any;
}