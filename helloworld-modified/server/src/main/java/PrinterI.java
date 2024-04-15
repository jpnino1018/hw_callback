import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

import Demo.*;

public class PrinterI implements Printer{

    private Map<String, CallbackPrx> clients = new ConcurrentHashMap<>();

    public void printString(String s,CallbackPrx client, com.zeroc.Ice.Current current)
    {

        new Thread(()->{
            try{
                String[] info = s.split(":");
                String newClient = info[0] + " : " + info[1];
                clients.putIfAbsent(newClient, client);

                System.out.println(newClient + " : " + s);
                String response = process(s);

                client.callbackClient(new Response(0, "Server response: " + response));
    
            }catch(Exception e){
                e.printStackTrace();
                client.callbackClient(null);
            }

        }).start();
    }

    private String process(String s){
        String[] parts = s.split("=");
        try{
            String check = parts[1];
            try {

                if(Integer.parseInt(parts[1])>0){
                    int num = Integer.parseInt(parts[1]);

                    System.out.println(num);

                    System.out.println("Fibonacci Series: ");
                    for (int i = 0; i < num; i++) {
                        int fibonacciNumber = fibonacci(i);
                        System.out.println(fibonacciNumber + " ");
                    }

                    String primeFactors = getPrimeFactors(num);
                    return("Prime Factors: " + primeFactors);
                }else{
                    System.out.println(s);
                }

            } catch (NumberFormatException e) {
                System.out.println(s);
            }
            try {
                if (parts[1].startsWith("listifs")) {
                    StringBuilder resultado = new StringBuilder();
                    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                    while (interfaces.hasMoreElements()) {
                        NetworkInterface iface = interfaces.nextElement();
                        resultado.append(iface+" / ");
                    }
                    System.out.println(resultado);
                    return resultado + "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (parts[1].startsWith("!")) {
                String command = parts[1].substring(1);
                String result = executeCommand(command);

                System.out.println(" - Command Result: ");
                System.out.println(parts[0]+result);

                System.out.println(parts[0]+"Command Result: " + result);

                return "Command result: "+ result;
            }
            if (parts[1].startsWith("listports")) {

                String command = parts[1].split(" ")[1];
                String result = executeCommand("nmap " + command);

                System.out.println(parts[0]+"\n List of ports of "+command + ":");
                System.out.println(result);

                return result;
            }
        } catch(Exception e){
            System.out.println("Nothing here.");
        }
        return "";
    }

    private static int fibonacci(int n) {
        if (n <= 1) {
            return n;
        } else {
            return fibonacci(n - 1) + fibonacci(n - 2);
        }
    }

    private static String getPrimeFactors(int number) {
        StringBuilder primeFactors = new StringBuilder();

        for (int i = 2; i <= number; i++) {
            while (number % i == 0) {
                primeFactors.append(i).append(" ");
                number /= i;
            }
        }

        return primeFactors.toString().trim();
    }

    private String executeCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }

            return result.toString();
        } catch (Exception e) {
            return "Error executing command: " + e.getMessage();
        }
    }
}