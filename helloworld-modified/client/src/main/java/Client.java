import com.zeroc.Ice.ObjectPrx;

import Demo.CallbackPrx;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Client
{
    public static void main(String[] args)
    {
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args,"config.client",extraArgs))
        {
            //com.zeroc.Ice.ObjectPrx base = communicator.stringToProxy("SimplePrinter:default -p 10000");
            Demo.PrinterPrx service = Demo.PrinterPrx
                    .checkedCast(communicator.propertyToProxy("Printer.Proxy"));
            
            if(service == null)
            {
                throw new Error("Invalid proxy");
            }
            com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapter("Callback");
            com.zeroc.Ice.Object object = new CallbackImpl();
            ObjectPrx prx = adapter.add(object, com.zeroc.Ice.Util.stringToIdentity("CallbackService"));
            adapter.activate();

            CallbackPrx clprx=CallbackPrx.uncheckedCast(prx);
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
            try{
                String client = System.getProperty("user.name") + ":" + java.net.InetAddress.getLocalHost().getHostName() + "=";

                while (true) {
                    System.out.print("Enter message (or 'exit' to quit): ");
                    String userInput = consoleInput.readLine();

                    if ("exit".equalsIgnoreCase(userInput)) {
                        break;
                    }

                    String message = client + userInput;

                    service.printString(message,clprx);
                }
            } catch(Exception e){
                e.printStackTrace();
            }
            System.out.println("callback invoked");
            communicator.waitForShutdown();
        }
    }
}