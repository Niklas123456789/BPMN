import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.util.Scanner;

public class RESTfulClient {


    public static void main(String[] args) {

        Scanner myObj = new Scanner(System.in);
        System.out.println("Welcome to the isiLoan!");
        System.out.println("Please enter your username:");

        // String input
        String username = myObj.nextLine();
        System.out.println("Enter password:");
        String password = myObj.nextLine();
        String temp = getLoginSuccess(username, password);
        System.out.println(temp);
        if (temp.equals("Success")){
            System.out.println("LOGIN SUCCESS!");
            System.out.println("How high should the loan be?");
            int amount = myObj.nextInt();
            getLoanOffers(amount);
        } else {
            System.out.println("Failed to login");
        }
        myObj.close();


    }
    public static void getLoanOffers(int amount) {
        try {
            String uri = "https://0imc9pt59h.execute-api.eu-west-2.amazonaws.com/default/loan";
            String functionURI = "?function=getLoanOffers";
            String amountURI = "&amount=" + amount;

            String fullURI = uri + functionURI + amountURI;
            System.out.println(fullURI);

            Client client = Client.create();
            WebResource resource = client.resource(fullURI);
            ClientResponse response = resource.accept("").get(ClientResponse.class);
            String output = response.getEntity(String.class);
            System.out.println(output);
            System.out.println();
            printOffers(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void printOffers(String input) {
        String[] offers = input.split("\\], \\[");
        String bank = "";
        String duration = "";
        String interest = "";
        String totalPayback = "";


        for(int index =  0; index < offers.length; index++){
            String[] offer = offers[index].split(",");

            bank = offer[3].replace("[","").replace("]","");
            duration = offer[1].replace("[","").replace("]","");
            interest = offer[2].replace("[","").replace("]","");
            totalPayback = offer[0].replace("[","").replace("]","");
            totalPayback = totalPayback.split("\\.")[0];
            //double totalPaybackDouble = Float.parseFloat(totalPayback);

            //System.out.println(totalPaybackDouble);

            System.out.println(index+1 + ". The institute " + bank + " offers you a " + duration + " month loan, with an interest of " + interest + ". The total payback would be: " + totalPayback + "€");
        }
        //System.out.println(offers[1]);
    }

   public static String getLoginSuccess(String username, String password){
       try {
           String uri = "https://14gk32jt4c.execute-api.eu-west-2.amazonaws.com/default/calc";
           String functionURI = "?function=checkPW";
           String userURI = "&username=" + username;
           String passwordURI = "&password=" + password;
           String fullURI = uri + functionURI + userURI + passwordURI;
           //System.out.println(fullURI);

           Client client = Client.create();
           WebResource resource = client.resource(fullURI);
           ClientResponse response = resource.accept("").get(ClientResponse.class);
           String output = response.getEntity(String.class);
           //System.out.println(output);
           //System.out.println("Output: " + output);

           if(/*response.getStatus() == 200 && */output.contains("Success")){
               //System.out.println(output);
               return "Success";

           }else {
               System.out.println("Something went wrong..!");
               return "Fail";
           }

       } catch (Exception e) {
           e.printStackTrace();
       }
       return "Fail";
   }
}