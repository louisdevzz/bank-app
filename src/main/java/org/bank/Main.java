package org.bank;

import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Manager m = new Manager();
        Scanner input = new Scanner(System.in);
        loop: while (true) {
            System.out.println("Welcome ARB BANK!!!");
            System.out.println("1: Create Account Bank\n2: Update Information\n3: Transfer Money\n4: Deposit Money\n5: Show Account Bank\n6: Quit");
            System.out.print("Enter your chose: ");
            int choice = Integer.parseInt(input.nextLine());
            switch (choice){
                case 1:
                    System.out.println("---------------------------------------------------");
                    System.out.print("Enter Full Name: ");
                    String fullName = input.nextLine();
                    System.out.print("Enter Username: ");
                    String username = input.nextLine();
                    System.out.print("Enter Password: ");
                    String password = input.nextLine();
                    m.createBankAccount(username,password,fullName);
                    System.out.println("---------------------------------------------------");
                    break;
                case 2:
                    System.out.println("---------------------------------------------------");
                    System.out.print("Enter Sex: ");
                    String sex = input.nextLine();
                    System.out.print("Enter Year Of Birth: ");
                    String yearOfBirth = input.nextLine();
                    System.out.print("Enter Address: ");
                    String address = input.nextLine();
                    System.out.print("Enter Phone Number: ");
                    String numberPhone = input.nextLine();
                    System.out.print("Enter Identification Card: ");
                    String identificationCard = input.nextLine();
                    m.updateInformationAccountBank(sex,yearOfBirth,address,numberPhone,identificationCard);
                    System.out.println("---------------------------------------------------");
                    break;
                case 3:
                    System.out.println("---------------------------------------------------");
                    System.out.print("Enter Your Number Account: ");
                    String yourAccountNumber = input.nextLine();
                    System.out.print("Enter Number Account: ");
                    String accountNumber = input.nextLine();
                    System.out.print("Enter Amount: ");
                    String amount = input.nextLine();
                    Long accountNumberForm = 0L;
                    Long accountNumberTo = 0L;
                    ArrayList<Bank> banks = m.Bank();
                    for (Bank b: banks){
                        if (b.getNumberAccount().equals(Long.parseLong(yourAccountNumber))) {
                            accountNumberForm=b.getNumberAccount();
                        }
                        if (b.getNumberAccount().equals(Long.parseLong(accountNumber))) {
                            accountNumberTo=b.getNumberAccount();
                        }
                    }
                    m.transferMoney(accountNumberForm,accountNumberTo,Long.parseLong(amount));
                    System.out.println("---------------------------------------------------");
                    break;
                case 4:
                    System.out.println("---------------------------------------------------");
                    System.out.print("Enter Your Number Account: ");
                    String numberAccount = input.nextLine();
                    System.out.print("Enter Amount: ");
                    String amountMoney = input.nextLine();
                    Long yoursAccountNumber = 0L;
                    ArrayList<Bank> bk = m.Bank();
                    for (Bank b: bk){
                        if (b.getNumberAccount().equals(Long.parseLong(numberAccount))) {
                            yoursAccountNumber = b.getNumberAccount();
                        }
                    }
                    m.depositMoney(yoursAccountNumber,Long.parseLong(amountMoney));
                    System.out.println("---------------------------------------------------");
                    break;
                case 5:
                    System.out.println("---------------------------------------------------");
                    m.showAcouuntBank();
                    System.out.println("---------------------------------------------------");
                    break;
                case 6:
                    break loop;
            }
        }

    }
}