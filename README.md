# InterAppMessageBus
PoC using Elemental 2 postMessage to communicate between two GWT applications.

## Usage

1. build 'InterAppMessage'
   
    * open a terminal window
    * run `cd InterAppMessageBus`
    * run `mvn clean install`

2. run the first application
    
    * open a terminal window
    * run `cd InterAppMessageBusExampleForElementalModuleB`
    * run `mvn gwt:devmode`

3. run the first application
   
    * open a terminal window
    * run `cd InterAppMessageBusExampleForElementalModuleA`
    * run `mvn gwt:devmode`

Note: You need to start **Module B** before **Module A**!

Call `http://127.0.0.1:8888/InterAppMessageBusExampleForElemental.html` in the browser.