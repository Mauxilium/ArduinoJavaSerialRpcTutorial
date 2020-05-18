package it.mauxilium.arduinojavaserialrpctutorial;


import it.mauxilium.arduinojavaserialrpc.exception.ArduinoRpcActionFailsException;
import it.mauxilium.arduinojavaserialrpc.exception.ArduinoRpcInitializationError;

import java.io.IOException;

public class JavaRpcTutorial {

    private final static String EXPECTED_SKETCH = "Led Tutorial Sketch (www.mauxilium.it)";

    void doIt(final String port, final String baudRate) throws ArduinoRpcInitializationError, ArduinoRpcActionFailsException {
        System.out.println("Hello from Mauxilium Arduino RPC Java Tutorial");
        ArduinoController ctrl = new ArduinoController(port, Integer.parseInt(baudRate));
        ctrl.connect();

        String cardName  = ctrl.getCardName();
        if (EXPECTED_SKETCH.equals(cardName)) {
            System.out.println("Connected to card: "+cardName);
        } else {
            System.out.println("Invalid card. Found \""+cardName+"\" instead of expected \""+EXPECTED_SKETCH+"\"");
            ctrl.stop();
            System.exit(-1);
        }
    }

    public static void main(String[] args) throws ArduinoRpcInitializationError, ArduinoRpcActionFailsException {
        if (args.length == 2) {
            try {
                new JavaRpcTutorial().doIt(args[0], args[1]);
            } catch (NumberFormatException | ArduinoRpcActionFailsException | ArduinoRpcInitializationError ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        } else {
            System.out.println("\nPlease use: JavaRpcTutorial 'port' 'baudRate'");
            System.out.println("I.e.: JavaRpcTutorial COM5 9600");
        }
    }
}
