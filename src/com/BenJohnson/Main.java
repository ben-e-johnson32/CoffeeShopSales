package com.BenJohnson;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    // A scanner to get input and a formatter to prettify the currency values.
    private static Scanner stringScanner = new Scanner(System.in);
    public static DecimalFormat currency = new DecimalFormat("$0.00");

    public static void main(String[] args)
    {
        // Make an array list of arrays from coffee.txt.
        ArrayList<String[]> products = GetProducts();
        // Make an empty array list for the lines we'll eventually write to the file.
        ArrayList<String> outputLines = new ArrayList<>();
        // Two double values to keep track of total costs and sales.
        Double totalCost = 0.0;
        Double totalSales = 0.0;

        // For each array in the list of products:
        for (String[] product : products)
        {
            // The name of the drink is the first item, its cost per cup is the second, and its price per cup is third.
            String name = product[0];
            Double costPerCup = Double.parseDouble(product[1]);
            Double pricePerCup = Double.parseDouble(product[2]);
            // Include the name of the drink in the prompt.
            String question = "How many cups of " + name + " were sold today? ";

            // A while true loop helps validate the data. If the user doesn't enter an integer, or its below zero,
            // we start back at the top of the while loop.
            while (true)
            {
                try
                {
                    // Get the input. Make sure it's a positive integer.
                    System.out.print(question);
                    String response = stringScanner.nextLine();
                    int cups = Integer.parseInt(response);
                    if (cups < 0)
                    {
                        System.out.println("Number of cups must be positive.");
                        continue;
                    }

                    // Calculate the totals for this drink and add them to the overall totals.
                    Double costForDrinks = cups * costPerCup;
                    Double revenueFromDrinks = cups * pricePerCup;
                    totalCost += costForDrinks;
                    totalSales += revenueFromDrinks;

                    // Build a string for the output file.
                    String outputLine = name + ": Sold " + cups + ", Expenses " + currency.format(costForDrinks) +
                                        ", Revenue " + currency.format(revenueFromDrinks) + ", Profit " +
                                        currency.format(revenueFromDrinks - costForDrinks);
                    outputLines.add(outputLine);

                    // If we made it through all that code, we can move onto another product.
                    break;
                }

                // If the user doesn't enter an integer, we'll be passing an illegal argument to parseInt, which
                // is caught here, again restarting the while true loop.
                catch (IllegalArgumentException iae)
                {
                    System.out.println("You must enter an integer value.");
                    continue;
                }
            }

        }

        // Call the method to write the file and close the scanner.
        WriteReportFile(outputLines, totalCost, totalSales);
        stringScanner.close();

    }

    private static ArrayList<String[]> GetProducts()
    {
        // Open the file - any IOExceptions are caught.
        try (BufferedReader bufReader = new BufferedReader(new FileReader("coffee.txt")))
        {
            // Each line is read into lines. Output will contain arrays of strings (name, cost, price).
            ArrayList<String> lines = new ArrayList<>();
            ArrayList<String[]> output = new ArrayList<>();

            // Loop through the file to fill up lines.
            String line = bufReader.readLine();
            while (line != null)
            {
                lines.add(line);
                line = bufReader.readLine();
            }

            bufReader.close();

            // For each line in lines, split the line into an array and add it to the output array list of string arrays.
            for (String l : lines)
            {
                String[] data = l.split(";");
                output.add(data);
            }

            return output;
        }

        catch (IOException ioe)
        {
            // If the file's missing, the program will crash after printing this super helpful message.
            System.out.println("Error reading coffee.txt");
            return new ArrayList<>();
        }
    }

    private static void WriteReportFile(ArrayList<String> outputLines, Double totalCost, Double totalRevenue)
    {
        // Open a file for writing - catch IOExceptions.
        try (BufferedWriter bufWriter = new BufferedWriter(new FileWriter("sales-report.txt")))
        {
            // Write all the output lines we saved earlier.
            for (String line : outputLines)
            {
                bufWriter.write(line + "\n");
            }

            // Write the totals, close the file, print a success message.
            bufWriter.write("Total expenses: " + currency.format(totalCost) + "\n");
            bufWriter.write("Total revenue: " + currency.format(totalRevenue) + "\n");
            bufWriter.write("Total profit: " + currency.format(totalRevenue - totalCost));
            bufWriter.close();
            System.out.println("Data saved to 'sales-report.txt'.");
        }

        catch (IOException ioe)
        {
            System.out.println("Error writing report file.");
        }
    }
}
