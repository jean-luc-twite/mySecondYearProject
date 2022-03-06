/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package restaurantserviceapp;

import ac.ac.tut.restaurant.Restaurant;
import ac.za.tut.chickenLicken.ChickenLicken;
import ac.za.tut.customerLogin.CustomerLogin;
import ac.za.tut.kfc.KFC;
import ac.za.tut.location.Location;
import ac.za.tut.mcDonal.McDonald;
import ac.za.tut.nando.Nando;
import ac.za.tut.restaurentManager.RestaurentManager;
import ac.za.tut.steers.Steers;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class RestaurantServiceApp {
 
    public static void main(String[] args) {
        // TODO code application logic here
             // declare variables
        List<Restaurant> restaurant = new ArrayList<>();
        double amountDue=0;
        double totalAmountDue;
        double totalAmountMade,payment;
    String name ="";
    String surName ="";
    String city ="";
    String state ="";
    boolean thereStillCustomer;
        InetAddress addr = null;
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in =null;
        int option,answers;
        int menus;
        Date date;
        String size,data="",response,customerName,email="";
        int quantity,odersNumber;
        String quiery = "select* from customers_tbl",mealName,RestName;
        boolean isRestaurentOpen =false,isPaymentValid ;
        CustomerLogin customers;
        int numberCustomers;

        Restaurant mcdonald,chickenLicken,nandos,steers,kfc;
       
        // instantiate the restaurant Manager
        RestaurentManager rm = new RestaurentManager();
        try {
            do{
                // create the address
                addr = InetAddress.getLocalHost();
                System.out.println("the IP Address " + addr);
                //create the socket 
                socket = new Socket(addr,9191);
                System.out.println(" connected...");
                // create the stream 
               out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
               in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 // call customers
                     //ask user to enter the name of the current customer  
                name = JOptionPane.showInputDialog(null,"please enter name");
               //ask user to enter the surName of current customer;
               surName = JOptionPane.showInputDialog(null," surName");;
               //ask user to enter city of the current customers
               city = JOptionPane.showInputDialog(null,"city");
               //ask user to enter the state of the current customer
               state = JOptionPane.showInputDialog(null,"state");
               //ask user to enter the previous customers number
               numberCustomers =Integer.parseInt( JOptionPane.showInputDialog(null,"enter the number of the previous customers","input",JOptionPane.INFORMATION_MESSAGE));
               //increase the number of customer
                 numberCustomers++;
                 odersNumber =Integer.parseInt( JOptionPane.showInputDialog(null,"enter the number of the previous order","input",JOptionPane.INFORMATION_MESSAGE));
                 //increase orders
                 odersNumber++; 
             ;
                 customers = getcustomers(numberCustomers,name,surName,email,city,state);
                 
               option = getUserOption();
               while(option != 6){
                
                   switch (option){
                       case 1:
                           mcdonald = new McDonald();
                           //add restaurant into the list
                         rm.addRestaurant(mcdonald);
                        amountDue = mcdonald.determineAmountDue();
                        break;
                       case 2:
                           steers = new Steers();
                           //add steers to into the list
                            rm.addRestaurant(steers);
                            //calculate amountDue
                            amountDue = steers.determineAmountDue();
                            break;
                      case 3:
                           chickenLicken = new ChickenLicken();
                           //add steers to into the list
                            rm.addRestaurant(chickenLicken);
                            //calculate amountDue
                            amountDue = chickenLicken.determineAmountDue();
                            break;
                     case 4:
                           nandos = new Nando();
                           //add steers to into the list
                            rm.addRestaurant(nandos);
                            //calculate amountDue
                            amountDue = nandos.determineAmountDue();
                            break;
                     case 5:
                           kfc = new KFC();
                           //add steers to into the list
                            rm.addRestaurant(kfc);
                            //calculate amountDue
                            amountDue = kfc.determineAmountDue();
                            break;
                     default:
                         JOptionPane.showMessageDialog(null, option + " is invalid please enter the correct option");
                   }
                  // calculte  the total amountDue
                  totalAmountDue = rm.determineTotalAmountDue(amountDue);
                  //ask user to make payment 
                   do{
                      payment =Double.parseDouble(JOptionPane.showInputDialog(null,"please make payement"));
                      if (payment > totalAmountDue){
                          double change = payment - totalAmountDue;
                          JOptionPane.showMessageDialog(null, "your change is R" + change );
                            isPaymentValid = true;
                      }else{
                              JOptionPane.showMessageDialog(null, " payment is invlid please make a valid payment");
                              isPaymentValid = false;
                      }
                  
                   }while(!isPaymentValid);
                   //ask the current customers if he/she want to keep chopping 
                   answers = JOptionPane.showConfirmDialog(null,"do you want to continue chopping","continue shopping",JOptionPane.YES_NO_OPTION);
                   //check  the customer want to continue shopping 
                    if(answers == JOptionPane.YES_OPTION){
                        //display the menue again
                        option = getUserOption();
                   }else{
                       option = 6;
                    }
                                    
               }//end of while loop
               
               //menus for database
                   menus = menus();
                   switch(menus){
                       case 1:
                           String namm = name;
                           String Surname= surName;
                           String cit = city;
                           String sta = state;
                           int custNum = numberCustomers;
                           int orderNum = odersNumber;
                     data = AddCustomers( custNum,namm,Surname, cit, sta,orderNum);
                   break;
                       case 2:
                           //concatenate
                       data = quiery + "#" + "2";                   
                       break;
                       case 3:
                           numberCustomers =Integer.parseInt(JOptionPane.showInputDialog(null,"please enter the number of customer ","search",JOptionPane.PLAIN_MESSAGE));
                           //call invoke method search 
                           
                           data = search(numberCustomers);
                           break;
                       
                       default:
                           JOptionPane.showMessageDialog(null, "invallid");
                   }
                   //send data 
                   out.println(data);
                   //get response 
                   response = in.readLine();
                   //display
                     JOptionPane.showMessageDialog(null, response);
                   answers =JOptionPane.showConfirmDialog(null, "do you want to close ","closing",JOptionPane.YES_NO_OPTION);
                   if(answers == JOptionPane.NO_OPTION){
                       isRestaurentOpen  =true;
                   }else if(answers == JOptionPane.YES_OPTION){
                       isRestaurentOpen =false;
                   }
               
            }while (!isRestaurentOpen);
            
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null,ex.getMessage());
        }
    }
 public static int getUserOption(){
        int option;
        // ask user to  choice restaurant
        option =Integer.parseInt(JOptionPane.showInputDialog(null,"please select the restaurent below" + "\n" +
                                                                       "---------------------------------" + "\n" +
                                                                        "press 1 -->for macDonald" + "\n" +
                                                                        "press 2 -->for Steers " + "\n" +
                                                                        "pree  3 --> for chickenLicken " + "\n" +
                                                                        "press 4--> for Nando's " + "\n" +
                                                                        "press 5--> for KFC " + "\n" +
                                                                        "press 6-->  to exit the program" + "\n" +
                                                                        " your choice ??","restaurants",JOptionPane.PLAIN_MESSAGE));
        return option;
    }
  public static CustomerLogin getcustomers(int customerNumbers,String name,String surName,String email,String city,String state){
     CustomerLogin customers;
     String emails;
     String cellPhone;
     boolean isCodeValid,isCellPhoneValid;
     String postalCode;
     String quiery;
     int min=1000;
     int max =9000;
     int random;
     int code;
     int answer;
     boolean thereStillCustomers =false;
    
     // ask user to enter emails
     emails = JOptionPane.showInputDialog(null,"email");
     //ask user to enter the  postal code
     postalCode = JOptionPane.showInputDialog(null,"postalCode");
     do{
     //ask user to enter the  cellPhone 
     cellPhone = JOptionPane.showInputDialog(null, "cellphone");
     if(cellPhone.length() == 10){
        random =(int)Math.floor(Math.random()*(max-min+1) + min);
       //display the code
       JOptionPane.showMessageDialog(null, random);
       //ask user to enter the code
       do{
       code = Integer.parseInt(JOptionPane.showInputDialog("please enter code"));
       if (code == random){
            isCodeValid = true;
       }else{
           isCodeValid = false;
       }
     }while(!isCodeValid);
       isCellPhoneValid =true;
     }else{
         JOptionPane.showMessageDialog(null, cellPhone + "cellPhone is invalid ");
            isCellPhoneValid =false;
     }
     }while(!isCellPhoneValid);
     
     //create customers
     customers = new CustomerLogin(name,surName,emails,cellPhone,new Location(city,state,cellPhone));

     return customers;
    

}
public static int  menus(){
    int menus;
   menus =Integer.parseInt(JOptionPane.showInputDialog(null,"please select the menus below" + "\n" +
                                                            "press 1 --> add items " + "\n" + 
                                                            "press 2 --> to have the record of each customers" + "\n" + 
                                                           "press  3 --> to seacrh for specifique order??" + "\n\n" +
                                                            
                                                          "your choice? "));
    return menus;       
   }

 public static String AddCustomers(int customerNumbers,String firstName,String surName,String city,String state,int orderNum){
    //call method customerLogin

   String data =customerNumbers  +","+firstName+","+surName+"," +city +"," + state + "," + orderNum + "#" + "1";
   
   return data;
 }
 public static String search(int custNumber){
     String data;
     data = custNumber + "#" + "3";
     return data;
 }
}
