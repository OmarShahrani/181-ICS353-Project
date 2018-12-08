package strassen;

import com.sun.corba.se.impl.util.PackagePrefixChecker;
import static java.lang.Integer.parseInt;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import omar.utils.FileReaderWriter;

/**
 *
 * @author omarm
 */
public class matrixMultiply {

    /**
     * @param args the command line arguments
     */
    private static Matrix A, B, Product;
    private static final Scanner KEYBOARD = new Scanner(System.in);
    private static String feedBackMsg = "";
    private static String inputFileName,
            outputFileName;
    private static int n;
    private static int size;
    private static long timeOfExcution;

    public static void main(String[] args) {
        //
        /* development */
 /* This part only during development, comment this for production */
        /*n = 10;
        inputFileName = "../../Input/matrix_10.txt";
        outputFileName = "../../matrix_10_product.txt";
*/
        // process input arguments
        startProduction(args);
        // read data from input file and define n, matrix A and matrix B
        readData(inputFileName);
        run();

    }

    private static void startProduction(String[] args) {
        if (args.length < 3) {
            System.out.println("You must provide three arguments.");
            System.out.println("As an example you may type");
            System.out.println("matrixMultiply 8 inputfile.txt outputfile.txt");
            throw new RuntimeException("Invalid number of arguments");
        } else {
            try {
                n = parseInt(args[0]);
                inputFileName = args[1];
                outputFileName = args[2];
            } catch (Exception e) {
                System.out.println("The first argument must be an integer.");
                System.out.println("As an example you may type");
                System.out.println("matrixMultiply 8 inputfile.txt outputfile.txt");
                throw new RuntimeException("TypeError: First argument must be an integer.");
            }
        }
    }

    private static void readData(String fileName) {
        size = (int) Math.pow(2, n);
        FileReaderWriter reader;
        List<String> content;
        reader = new FileReaderWriter(fileName);
        content = reader.read();
        A = loadMatrix(content, 0, size);
        B = loadMatrix(content, size, 2 * size);;
    }

    private static void run() {
        String option;
        RUNNING:
        while (true) {
            option = getUserOption();
            switch (option) {
                case "a": {
                    performAlgorithm("CLASSICAL_ITER", 0);
                    break;
                }
                case "b": {
                    performAlgorithm("CLASSICAL_RECURSIVE", 0);
                    break;
                }
                case "c": {
                    performAlgorithm("STRASSEN", 1);
                    break;
                }
                case "d": {
                    int d;
                    String tmpMsg = "";
                    CASE4:
                    while (true) {
                        System.out.println(tmpMsg);
                        System.out.println("\n Please provide the base (it should be less than " + n + ")");
                        String dim = KEYBOARD.next();
                        d = parseInt(dim);
                        if (d > n) {
                            tmpMsg = "The base should be less than " + n + ".";
                        } else {
                            break CASE4;
                        }
                    }
                    performAlgorithm("STRASSEN", d);
                    break;
                }

                case "q": {
                    System.out.println("Ending ... ");
                    break RUNNING;
                }
                default: {
                    feedBackMsg = "Please choose a correct option";
                }

            }
        }
    }

    private static void warmUp() {
        Matrix TempA, TempB, Temp;
        int m;
        m = Math.min(8,n);
        int tempSize;
        tempSize = (int) Math.pow(2, m);
        TempA = A.getRange(0, 0, tempSize);
        TempB = B.getRange(0, 0, tempSize);

        MatrixOperations Op = new MatrixOperations();
        // warmup 
        for (int time = 1; time <= 5; time++) {
            Temp = Op.mult(TempA, TempB, m, "CLASSICAL_ITER", 0);
        }
        Temp = null;
        TempA = null;
        TempB = null;
    }

    private static void performAlgorithm(String ALOGITHM, int base) {
        warmUp();
        long start, end;
        StringBuilder summary = new StringBuilder();
        MatrixOperations Op = new MatrixOperations();
        start = System.nanoTime();
        Product = Op.mult(A, B, n, ALOGITHM, base);
        end = System.nanoTime();
        timeOfExcution = (end- start) / 1000000;
        saveProduct(Product);
        String algorithName;
        algorithName = ALOGITHM;
        algorithName += (base > 0) ? "(base is n = " + base + ")" : "";
        summary.append("++++ SUMMARY (average time in milliseconds of ");
        summary.append(" one run)++++++");
        summary.append("--------------------------------------------------\n");
        summary.append(String.format("|%3s\t", "n"));
        summary.append(String.format("|%-10s\t", algorithName));
        summary.append("\n--------------------------------------------------\n");

        summary.append(String.format("|%3d\t", n));
        summary.append(String.format("|%-10d milliseconds\t", timeOfExcution));
        summary.append("\n");
        System.out.println(summary.toString());

    }

    private static Matrix loadMatrix(List<String> list, int firstRow, int lastRow) {
        Matrix Temp = new Matrix(size);
        String[] rowString;
        for (int row = firstRow; row < lastRow; row++) {
            rowString = list.get(row).split("\t");
            for (int col = 0; col < size; col++) {
                Temp.push(parseInt(rowString[col]), row % size, col);
            }
        }
        return Temp;
    }

    private static String getUserOption() {
        if (!feedBackMsg.equals("")) {
            System.out.println("++++++++++++++++++++++++++++");
            System.out.println(feedBackMsg);
            System.out.println("++++++++++++++++++++++++++++");
        }
        String introduction = "********************************************************************** \n"
                + "* We have loaded two matrices \n"
                + "*    A (" + size + "x" + size + ")  and   B (" + size + "x" + size + ")\n"
                + "* This program finds the product \n"
                + "*    C = A x B \n"
                + "* and report the average time (in milliseconds) running the method you choose as many as you specify \n"
                + "* the default is one time.\n"
                + "* Please choose the method you want to use to perform by typing the method number. \n"
                + "* (a) The classical iterative version of the matrix multiplication algorithm,\n"
                + "* (b) The classical divide and conquer recursive version of the matrix multiplication algorithm.\n"
                + "* (c) The classical Strassen’s divide and conquer recursive algorithm for matrix multiplication \n"
                + "*     (with base case value of n=1.) \n"
                + "* (d) Strassen’s divide and conquer recursive algorithm for matrix multiplication with base case \n"
                + "*     case value of n>1\n"
                + "* (q) End and exit program.\n"
                + "\n"
                + "****************************************************************************** \n";

        System.out.println(introduction);
        String option = KEYBOARD.next();

        return option;
        /*System.out.println("How many times you want to run the method?");
            try {
                String timesStr = KEYBOARD.next();
                times = parseInt(timesStr);
            } catch (Exception e) {
                times = 1;
            }*/
        //  CLASSICAL_ITER CLASSICAL_RECURSIVE STRASSEN STRASSEN STRASSEN_2

    }

    private static void saveProduct(Matrix C) {
        FileReaderWriter writer;
        String rowString = "";
        List<String> content = new ArrayList<>();
        content.add(timeOfExcution + "");
        writer = new FileReaderWriter();
        writer.setOutputFileName(outputFileName);

        for (int row = 0; row < C.size; row++) {
            rowString = "";
            for (int col = 0; col < C.size; col++) {
                rowString += C.get(row, col) + "\t";
            }
            content.add(rowString);
        }
        writer.write(content);
    }

}
