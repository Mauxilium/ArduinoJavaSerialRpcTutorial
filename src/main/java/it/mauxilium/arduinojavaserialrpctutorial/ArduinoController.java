package it.mauxilium.arduinojavaserialrpctutorial;


import it.mauxilium.arduinojavaserialrpc.ArduinoJavaSerialRpc;
import it.mauxilium.arduinojavaserialrpc.exception.ArduinoRpcJavaFailsException;

import java.io.IOException;

/**
 * ArduinoController extends ArduinoJavaRpc this promote the Sketch inside Arduino Card
 * as a potential caller of public methods implemented here (i.e. button)
 */
public class ArduinoController extends ArduinoJavaSerialRpc {

    private AsyncStatusAgent agent;

    public ArduinoController(String port, int rate) {
        super(port, rate);
        agent = new AsyncStatusAgent(this);
        agent.start();
    }

    /**
     * Disconnect from card and terminate the agent thread
     */
    public void stop() {
        agent.disconnect();
        try {
            this.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is the one specified into the Sketch as: arpc.executeRemoteMethod("button", "PRESSED");
     * @param status the value sent from the Sketch (i.e. "PRESSED")
     * @return A string returned to the Sketch. In this simple example it is ignored.
     */
    public String button(final String status) {
        System.out.println("Arduino says: Button is "+status);
        manageStatus(status);
        return ""; // Ignored
    }

    /**
     * Implements the simple activation business logic of embedded Arduino Led.
     * This method call the Sketch registered function during the setup:
     *      arpc.registerArduinoAction("LedUpdate", ledControl);
     * @param status the driving value
     */
    private void manageStatus(final String status) {
        if (status.equals("PRESSED")) {
            agent.sendToArduino("ON");
        } else {
            agent.sendToArduino("OFF");
        }
    }

    private static class AsyncStatusAgent extends Thread {

        private static final String ARDUINO_FUNCTION_TO_CALL = "LedUpdate";

        private ArduinoJavaSerialRpc arduino;
        private String status = "OFF";
        private final Object lock = new Object();
        private boolean isActive = true;

        public AsyncStatusAgent(final ArduinoJavaSerialRpc ar) {
            arduino = ar;
        }

        public void disconnect() {
            isActive = false;
            readySignal(); // Unlock the waitReadySignal inside run()
        }

        public void sendToArduino(String value) {
            status = value;
            readySignal();
        }

        @Override
        public void run() {
            String response = "";
            while (isActive) {
                if (waitReadySignal()) {
                    // The following check is required in order to manage when the disconnect is called during the wait
                    if (isActive) {
                        try {
                            response = arduino.executeRemoteFunction(ARDUINO_FUNCTION_TO_CALL, status);
                        } catch (ArduinoRpcJavaFailsException e) {
                            System.out.println("Sorry, failure with Arduino card: " + e.getLocalizedMessage());
                        }
                        System.out.println("Executed Led Switch number: " + response);
                    }
                }
            }
        }

        private boolean waitReadySignal() {
            try {
                synchronized (lock) {
                    lock.wait();
                }
            } catch (InterruptedException e) {
                return false;
            }
            return true;
        }

        private void readySignal() {
            synchronized (lock) {
                lock.notify();
            }
        }
    }
}
