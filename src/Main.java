import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        collectMatrix();
        boolean initialize = true;
        try {
            while (initialize) {
                System.out.print("Would you like to do it all again? (Y/N): ");
                char answer1 = userInput.next().charAt(0);
                if (answer1 == 'Y' || answer1 == 'y') {
                    System.out.println();
                    collectMatrix();
                } else if (answer1 == 'N' || answer1 == 'n') {
                    initialize = false;
                    System.out.println("The program is now terminated.");
                } else {
                    System.out.println("Input invalid, input Y/N only. Please try again!");
                    initialize = true;
                }
            }
        } catch (Exception e) {
            System.out.println("Input invalid. Please try again!");
        }
        userInput.close(); // close scanner to avoid memory leak
    }

    // This method gathers the input from the user.
    public static void collectMatrix() {
        Scanner userInput = new Scanner(System.in);
        System.out.println("Reduce Row Echelon Form");

        try {
            System.out.print("Enter the number of unknowns: ");
            int unknwn = userInput.nextInt();

            double[][] standardMatrix = new double[unknwn][unknwn + 1];
            double[][] reducedEchelonMatrix = new double[unknwn][unknwn + 1];

            System.out.println("Input Values: ");
            for (int i = 0; i < unknwn; i++) {
                for (int j = 0; j < unknwn + 1; j++) {
                    System.out.print("Enter Row " + (i + 1) + ", Column " + (j + 1) + " value: ");
                    while (true) {
                        try {
                            String input = userInput.next();
                            if (input.equals("00")) {
                                System.out.print("Invalid Input! Cannot be 00. Please enter a number again: ");
                                userInput.nextLine();
                            } else {
                                try {
                                    standardMatrix[i][j] = Double.parseDouble(input);
                                    reducedEchelonMatrix[i][j] = Double.parseDouble(input);
                                    break;
                                } catch (NumberFormatException e) {
                                    System.out.print("Invalid Input! Please enter a number: ");
                                    userInput.nextLine();
                                }
                            }
                        } catch (Exception e) {
                            System.out.print("Invalid Input! Please enter a number: ");
                            userInput.next();
                            j--;
                        }
                    }
                }
            }
            int solutionType = gsjrd(reducedEchelonMatrix);
            if (solutionType == 0) {
                for (int i = 0; i < unknwn; i++) {
                    reducedEchelonMatrix[i][unknwn] /= reducedEchelonMatrix[i][i];
                    reducedEchelonMatrix[i][i] /= reducedEchelonMatrix[i][i];
                }
            }

            // This will ask the user on what to do with the input that they have entered
            // for the matrix.
            boolean repeat_loop = true;
            while (repeat_loop) {
                try {
                    System.out.print("\n================================================\n" +
                            "What would you like to do?\n" +
                            "1. Print Matrix\n" +
                            "2. Print Matrix in Linear Form\n" +
                            "3. Print Augmented Matrix\n" +
                            "4. Print Reduced Row Echelon Form\n" +
                            "5. Show solutions\n" +
                            "6. Verify solutions\n" +
                            "7. Input another values\n" +
                            "8. Exit Program\n" +
                            "Enter the number of your choice: ");

                    int option = userInput.nextInt();
                    if (option == 8) {
                        repeat_loop = false;
                        break;
                    } else if (option == 7) { // Input
                        collectMatrix();
                        break;
                    } else if (option == 1) { // Print Matrix
                        System.out.print("\n");
                        System.out.print("Here is the Matrix Form of the system: \n");
                        printMatrixForm(standardMatrix);
                    } else if (option == 2) { // Print Linear Equation
                        System.out.print("\n");
                        System.out.print("Here is the Linear Equation: \n");
                        printLinearEquations(standardMatrix);
                    } else if (option == 3) { // Print Augmented Matrix
                        System.out.print("\n");
                        System.out.print("Here is the Augmented Identity Matrix Form of the system: \n");
                        printaugidtMat(standardMatrix);
                    } else if (option == 4) { // Print Reduced Row Echelon Form
                        boolean some_loop = true;
                        while (some_loop) {
                            try {
                                System.out.print("\n================================================\n" +
                                        "What would you like to do?\n" +
                                        "0. Return to Choices\n" +
                                        "1. Print Matrix with all RREF Steps\n" +
                                        "2. Print Last Step of RREF\n" +
                                        "Enter the number of your choice: ");
                                int option2 = userInput.nextInt();
                                if (option2 == 0) {
                                    some_loop = false;
                                    break;
                                } else if (option2 == 1) {
                                    System.out.print("\n");
                                    System.out.print(
                                            "Here is the Reduced Row Echelon Form of the system (with its steps): \n");
                                    printRdcechStep(standardMatrix);
                                    System.out.print("\nHere is the Reduced Row Echelon Form of the system: \n");
                                    printaugidtMat(reducedEchelonMatrix);
                                    break;
                                } else if (option2 == 2) {
                                    System.out.print("\n");
                                    System.out.print("Here is the Reduced Row Echelon Form of the system: \n");
                                    printaugidtMat(reducedEchelonMatrix);
                                    break;
                                }
                            } catch (Exception e) {
                                System.out.println("Input invalid. Please try again!");
                                userInput.next();
                            }
                        }
                    } else if (option == 5) { // Show solutions
                        System.out.print("\n");
                        System.out.print("The solution of the system: \n");
                        int rows_num = reducedEchelonMatrix.length;
                        int cols_num = reducedEchelonMatrix[0].length;
                        if (solutionType == 1 && reducedEchelonMatrix[rows_num - 1][cols_num - 1] != 0) {
                            System.out.println("The system has no solution.");
                        } else if (solutionType == 1 && reducedEchelonMatrix[rows_num - 1][cols_num - 1] == 0) {
                            double[] equation = standardMatrix[0];
                            double answer = equation[unknwn];
                            String[] Var = extractVar(unknwn);
                            double[] solutions = extractSolutions(reducedEchelonMatrix);
                            showSolution(reducedEchelonMatrix, Var);
                        } else {
                            double[] solutions = extractSolutions(reducedEchelonMatrix);
                            String[] Var = extractVar(unknwn);
                            for (int i = 0; i < unknwn; i++) {
                                if (Math.abs(solutions[i] - Math.floor(solutions[i])) < 0.001) {
                                    double dbv = solutions[i];
                                    int intv = (int) dbv;
                                    System.out.println(Var[i] + " = " + intv);
                                    dbv = (double) intv;
                                    dbv = solutions[i];
                                } else if (solutions[i] == 0 || solutions[i] == -0) {
                                    double dbv = solutions[i];
                                    int intv = (int) dbv;
                                    System.out.println(Var[i] + " = " + intv);
                                    dbv = (double) intv;
                                    dbv = solutions[i];
                                } else {
                                    // By using %.2f, it will reduce the number of decimal places that will be
                                    // printed into two only.
                                    System.out.printf(Var[i] + " = " + "%.2f" + "\n", solutions[i]);
                                }
                            }
                            System.out.println("The system has a unique solution.");
                        }
                    } else if (option == 6) { // Verify
                        int rows_num = reducedEchelonMatrix.length;
                        int cols_num = reducedEchelonMatrix[0].length;

                        System.out.print("\n");
                        System.out.print("The Matrix Form of the Equation: \n");
                        printMatrixForm(standardMatrix);

                        System.out.print("\n");
                        System.out.print("The Reduced Row Echelon Form of the Equation: \n");
                        printaugidtMat(reducedEchelonMatrix);

                        System.out.print("\n");
                        System.out.print("The Values of the Variables: \n");
                        if (solutionType == 1 && reducedEchelonMatrix[rows_num - 1][cols_num - 1] != 0) {
                            System.out.println("The system has no solution.");
                        } else if (solutionType == 1 && reducedEchelonMatrix[rows_num - 1][cols_num - 1] == 0) {
                            double[] equation = standardMatrix[0];
                            double answer = equation[unknwn];
                            String[] Var = extractVar(unknwn);
                            double[] solutions = extractSolutions(reducedEchelonMatrix);
                            showSolution(reducedEchelonMatrix, Var);
                        } else {
                            double[] solutions = extractSolutions(reducedEchelonMatrix);
                            String[] Var = extractVar(unknwn);
                            for (int i = 0; i < unknwn; i++) {
                                if (Math.abs(solutions[i] - Math.floor(solutions[i])) < 0.001) {
                                    double dbv = solutions[i];
                                    int intv = (int) dbv;
                                    System.out.println(Var[i] + " = " + intv);
                                    dbv = (double) intv;
                                    dbv = solutions[i];
                                } else if (solutions[i] == 0 || solutions[i] == -0) {
                                    double dbv = solutions[i];
                                    int intv = (int) dbv;
                                    System.out.println(Var[i] + " = " + intv);
                                    dbv = (double) intv;
                                    dbv = solutions[i];
                                } else {
                                    // By using %.2f, it will reduce the number of decimal places that will be
                                    // printed into two only.
                                    System.out.printf(Var[i] + " = " + "%.2f" + "\n", solutions[i]);
                                }
                            }
                        }

                        System.out.print("\n");
                        System.out.print("Verifying the solution of the system: \n");
                        if (solutionType == 1 && reducedEchelonMatrix[rows_num - 1][cols_num - 1] != 0) {
                            System.out.println("The system has no solution. Cannot be verified!");
                        } else if (solutionType == 1 && reducedEchelonMatrix[rows_num - 1][cols_num - 1] == 0) {
                            double[] equation = standardMatrix[0];
                            double answer = equation[unknwn];
                            String[] Var = extractVar(unknwn);
                            double[] solutions = extractSolutions(reducedEchelonMatrix);
                            verifySolution(reducedEchelonMatrix, reducedEchelonMatrix, Var);
                        } else {
                            double[] equation = standardMatrix[0];
                            double answer = equation[unknwn];
                            String[] Var = extractVar(unknwn);
                            double[] solutions = extractSolutions(reducedEchelonMatrix);
                            System.out.println("For equation 1: ");
                            // Printing the equation
                            for (int i = 0; i < unknwn; i++) {
                                double cffcnt = equation[i];
                                if (cffcnt != 1) {
                                    System.out.print("(" + cffcnt + ")");
                                }
                                System.out.print("(" + Var[i] + ")");
                                if (i != unknwn - 1) {
                                    System.out.print(" + ");
                                } else {
                                    System.out.print(" = ");
                                }
                            }
                            if (Math.abs(answer - Math.floor(answer)) < 0.001) {
                                double dbv = answer;
                                int intv = (int) dbv;
                                System.out.print(intv + "\n");
                                dbv = (double) intv;
                                dbv = answer;
                            } else if (answer == 0 || answer == -0) {
                                double dbv = answer;
                                int intv = (int) dbv;
                                System.out.print(intv + "\n");
                                dbv = (double) intv;
                                dbv = answer;
                            } else {
                                // By using %.2f, it will reduce the number of decimal places that will be
                                // printed into two only.
                                System.out.printf("%.2f\n", answer);
                            }

                            // Subtituting the values of the variables.
                            for (int i = 0; i < unknwn; i++) {
                                double cffcnt = equation[i];
                                if (cffcnt != 1) {
                                    System.out.print("(" + cffcnt + ")");
                                }
                                System.out.print("(" + solutions[i] + ")");
                                if (i != unknwn - 1) {
                                    System.out.print(" + ");
                                } else {
                                    System.out.print(" = ");
                                }
                            }
                            if (Math.abs(answer - Math.floor(answer)) < 0.001) {
                                double dbv = answer;
                                int intv = (int) dbv;
                                System.out.print(intv + "\n");
                                dbv = (double) intv;
                                dbv = answer;
                            } else if (answer == 0 || answer == -0) {
                                double dbv = answer;
                                int intv = (int) dbv;
                                System.out.print(intv + "\n");
                                dbv = (double) intv;
                                dbv = answer;
                            } else {
                                // By using %.2f, it will reduce the number of decimal places that will be
                                // printed into two only.
                                System.out.printf("%.2f\n", answer);
                            }

                            // Evaluation by term
                            for (int i = 0; i < unknwn; i++) {
                                double cffcnt = equation[i];
                                double term = cffcnt * solutions[i];
                                System.out.print("(" + term + ")");
                                if (i != unknwn - 1) {
                                    System.out.print(" + ");
                                } else {
                                    System.out.print(" = ");
                                }
                            }
                            if (Math.abs(answer - Math.floor(answer)) < 0.001) {
                                double dbv = answer;
                                int intv = (int) dbv;
                                System.out.print(intv + "\n");
                                dbv = (double) intv;
                                dbv = answer;
                            } else if (answer == 0 || answer == -0) {
                                double dbv = answer;
                                int intv = (int) dbv;
                                System.out.print(intv + "\n");
                                dbv = (double) intv;
                                dbv = answer;
                            } else {
                                // By using %.2f, it will reduce the number of decimal places that will be
                                // printed into two only.
                                System.out.printf("%.2f\n", answer);
                            }

                            // Last equation
                            if (Math.abs(answer - Math.floor(answer)) < 0.001) {
                                double dbv = answer;
                                int intv = (int) dbv;
                                System.out.println(intv + " = " + intv + "\n");
                                if (answer == answer) {
                                    System.out.println("Solution is therefore true and unique!");
                                } else if (answer != answer) {
                                    System.out.println("Solution is not true!");
                                }
                                dbv = (double) intv;
                                dbv = answer;
                            } else if (answer == 0 || answer == -0) {
                                double dbv = answer;
                                int intv = (int) dbv;
                                System.out.println(intv + " = " + intv + "\n");
                                if (answer == answer) {
                                    System.out.println("Solution is therefore true and unique!");
                                } else if (answer != answer) {
                                    System.out.println("Solution is not true!");
                                }
                                dbv = (double) intv;
                                dbv = answer;
                            } else {
                                // By using %.2f, it will reduce the number of decimal places that will be
                                // printed into two only.
                                System.out.printf("%.2f = %.2f\n", answer, answer);
                                if (answer == answer) {
                                    System.out.println("Solution is therefore true and unique!");
                                } else if (answer != answer) {
                                    System.out.println("Solution is not true!");
                                }
                            }
                        }
                    } else {
                        System.out.println("Input invalid. Please try again!");
                    }
                } catch (Exception e) {
                    System.out.println("Input invalid. Please try again!");
                    userInput.next();
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid Input!");
            collectMatrix();
        }
        userInput.close();
    }

    // This will generate the variable by the number of unknowns.
    public static String[] extractVar(int unknwn) {
        int var = 122 - unknwn + 1;
        String[] Var = new String[unknwn];
        for (int i = 0; i < unknwn; i++) {
            Var[i] = String.valueOf((char) var);
            var++;
        }
        return Var;
    }

    // This will extract the solutions from the Augmented Identity Matrix.
    public static double[] extractSolutions(double[][] matrix) {
        int rows_num = matrix.length;
        int cols_num = matrix[0].length;
        double[] solutions = new double[rows_num];
        for (int i = 0; i < rows_num; i++) {
            solutions[i] = matrix[i][cols_num - 1];
        }
        return solutions;
    }

    // This will convert the Augmented Matrix into reducedEchelonMatrix while using
    // Gauss-Jordan Elimination.
    public static int gsjrd(double[][] a) {
        int n = a.length;
        int i, j, k = 0, c, value = 0;

        // Elementary Operations
        for (i = 0; i < n; i++) {
            if (a[i][i] == 0) {
                c = 1;
                while ((i + c) < n && a[i + c][i] == 0)
                    c++;
                if ((i + c) == n) {
                    value = 1;
                    break;
                }
                for (j = i, k = 0; k <= n; k++) {
                    double temp = a[j][k];
                    a[j][k] = a[j + c][k];
                    a[j + c][k] = temp;
                }
            }

            for (j = 0; j < n; j++) {
                // We will exclude all i == j
                if (i != j) {
                    // This will convert the matrix into reduced row echelon form.
                    double p = a[j][i] / a[i][i];
                    for (k = 0; k <= n; k++)
                        a[j][k] = a[j][k] - (a[i][k]) * p;
                }
            }
        }
        return value;
    }

    public static int getValidity(double[][] reducedEchelonMatrix, double[] solutions, boolean[] allZero) {
        int rows_num = reducedEchelonMatrix.length;
        int cols_num = reducedEchelonMatrix[0].length;

        int slTyp = 1; // unique solution
        // populate, if there is 0, check corresponding row if it is all 0;
        for (int i = 0; i < rows_num; i++) {
            solutions[i] = reducedEchelonMatrix[i][cols_num - 1];
            allZero[i] = true;
            for (int j = 0; j < cols_num - 1; j++) {
                if (reducedEchelonMatrix[i][j] != 0) {
                    allZero[i] = false;
                    break;
                }
            }
            if (allZero[i]) { // all zero coefficients so far
                if (solutions[i] == 0) {
                    slTyp = 2; // infinite solutions
                } else {
                    allZero[i] = false;
                    slTyp = 3; // no solution
                    break;
                }
            }
        }
        return slTyp;
    }

    public static void showSolution(double[][] reducedEchelonMatrix, String[] variables) {
        int rows_num = reducedEchelonMatrix.length;
        int cols_num = reducedEchelonMatrix[0].length;
        double[] solutions = extractSolutions(reducedEchelonMatrix);
        boolean[] allZero = new boolean[rows_num];
        int slTyp = getValidity(reducedEchelonMatrix, solutions, allZero);

        if (slTyp == 3) { // no solution
            System.out.println("The system has no solution.");
            ;
        } else if (slTyp == 1) { // unique solution
            for (int i = 0; i < rows_num; i++) {
                System.out.println(variables[i] + " = " + solutions[i]);
            }
        } else { // infinite solutions
            System.out.println("The system has infinitely many solutions.");
            for (int i = 0, rCount = 0; i < rows_num; i++) {
                if (!allZero[i]) {
                    System.out.print(variables[i]);
                    for (int j = i + 1; j < cols_num - 1; j++) {
                        if (reducedEchelonMatrix[i][j] != 0) {
                            System.out.print(" + ");
                            if (reducedEchelonMatrix[i][j] != 1) {
                                System.out.print("(" + reducedEchelonMatrix[i][j] + ")");
                            }
                            System.out.print(variables[j]);
                        }
                    }
                    System.out.println(" = " + reducedEchelonMatrix[i][cols_num - 1]);
                } else {
                    System.out.println(variables[i] + " = " + (char) ('r' + rCount));
                    rCount = rCount + 1;
                }
            }
        }
    }

    public static void verifySolution(double[][] reducedEchelonMatrix, double[][] matrix, String[] variables) {
        int rows_num = reducedEchelonMatrix.length;
        int cols_num = reducedEchelonMatrix[0].length;
        double[] solutions = extractSolutions(reducedEchelonMatrix);
        boolean[] allZero = new boolean[rows_num];
        int slTyp = getValidity(reducedEchelonMatrix, solutions, allZero);

        if (slTyp == 3) { // no solution
            System.out.println("The system has no solution.");
        } else {
            if (slTyp == 2) { // infinite solutions so solutions must be altered first
                System.out.println("The system has infinitely many solutions.");
                // Printing initial equations from reducedEchelonMatrix
                for (int i = 0, rCount = 0; i < rows_num; i++) {
                    if (!allZero[i]) {
                        System.out.print(variables[i]);
                        for (int j = i + 1; j < cols_num - 1; j++) {
                            if (reducedEchelonMatrix[i][j] != 0) {
                                System.out.print(" + ");
                                if (reducedEchelonMatrix[i][j] != 1) {
                                    System.out.print("(" + reducedEchelonMatrix[i][j] + ")");
                                }
                                System.out.print(variables[j]);
                            }
                        }
                        System.out.println(" = " + reducedEchelonMatrix[i][cols_num - 1]);
                    } else {
                        System.out.println(variables[i] + " = " + (char) ('r' + rCount));
                        rCount = rCount + 1;
                    }
                }

                System.out.print("Do you want to input something that will substitute to R? (Y/N): ");
                Scanner userInput = new Scanner(System.in);
                char answer1 = userInput.next().charAt(0);
                try {
                    if (answer1 == 'Y' || answer1 == 'y') {
                        // Setting values for variables with all zero rows_num and calculating the rest
                        // from it
                        boolean rsub = true;
                        while (rsub) {
                            try {
                                System.out.print("Input any real number to substitute to R: ");
                                String input = userInput.next();
                                if (input.equals("00")) {
                                    System.out.print("Invalid Input! Cannot be 00. Please enter a number again: ");
                                    userInput.nextLine();
                                } else {
                                    try {
                                        for (int i = rows_num - 1; i >= 0; i--) { // reverse loop for better efficiency
                                            if (!allZero[i]) { // no more all zero rows_num because of reduced row
                                                               // echelon form nature
                                                // calculate other variables
                                                for (int j = 0; j <= i; j++) {
                                                    solutions[j] = reducedEchelonMatrix[j][cols_num - 1];
                                                    for (int k = j + 1; k < cols_num - 1; k++) { // transposing each
                                                                                                 // other term to other
                                                                                                 // side
                                                        double term = reducedEchelonMatrix[j][k] * solutions[k];
                                                        solutions[j] = solutions[j] - term;
                                                    }
                                                }
                                                break;
                                            }
                                            System.out.print(variables[i] + " = ");
                                            solutions[i] = Double.parseDouble(input);
                                        }
                                        System.out.println();
                                        // Printing equations substituted with new solution
                                        for (int i = 0; i < rows_num; i++) {
                                            if (!allZero[i]) {
                                                System.out.print(variables[i]);
                                                for (int j = i + 1; j < cols_num - 1; j++) {
                                                    if (reducedEchelonMatrix[i][j] != 0) {
                                                        System.out.print(" + ");
                                                        if (reducedEchelonMatrix[i][j] != 1) {
                                                            System.out.print("(" + reducedEchelonMatrix[i][j] + ")");
                                                        }
                                                        System.out.print("(" + solutions[j] + ")");
                                                    }
                                                }
                                                System.out.println(" = " + reducedEchelonMatrix[i][cols_num - 1]);
                                            } else {
                                                System.out.println(variables[i] + " = " + solutions[i]);
                                            }
                                        }
                                        System.out.println();

                                        // Printing equations with evaluated terms
                                        for (int i = 0; i < rows_num; i++) {
                                            if (!allZero[i]) {
                                                System.out.print(variables[i]);
                                                for (int j = i + 1; j < cols_num - 1; j++) {
                                                    if (reducedEchelonMatrix[i][j] != 0) {
                                                        System.out.print(" + ");
                                                        System.out.print(
                                                                "(" + reducedEchelonMatrix[i][j] * solutions[j] + ")");
                                                    }
                                                }
                                                System.out.println(" = " + reducedEchelonMatrix[i][cols_num - 1]);
                                            } else {
                                                System.out.println(variables[i] + " = " + solutions[i]);
                                            }
                                        }
                                        System.out.println();

                                        // Printing equations with transposed terms
                                        for (int i = 0; i < rows_num; i++) {
                                            if (!allZero[i]) {
                                                System.out.print(
                                                        variables[i] + " = " + reducedEchelonMatrix[i][cols_num - 1]);
                                                for (int j = i + 1; j < cols_num - 1; j++) {
                                                    if (reducedEchelonMatrix[i][j] != 0) {
                                                        System.out.print(" - ");
                                                        System.out.print(
                                                                "(" + reducedEchelonMatrix[i][j] * solutions[j] + ")");
                                                    }
                                                }
                                                System.out.println();
                                            } else {
                                                System.out.println(variables[i] + " = " + solutions[i]);
                                            }
                                        }
                                        System.out.println();

                                        // Printing solutions
                                        for (int i = 0; i < rows_num; i++) {
                                            System.out.println(variables[i] + " = " + solutions[i]);
                                        }

                                        System.out.println(" ");
                                        // Verifying
                                        System.out.print("Solution: {(");
                                        for (int i = 0; i < rows_num; i++) {
                                            System.out.print(solutions[i]);
                                            if (i != rows_num - 1) {
                                                System.out.print(", ");
                                            } else {
                                                System.out.print(")}");
                                            }
                                        }
                                        System.out.println("\n");
                                        // Printing the equations
                                        for (int i = 0; i < rows_num; i++) {
                                            for (int j = 0; j < cols_num - 1; j++) {
                                                System.out.print("(" + matrix[i][j] + ")" + variables[j]);
                                                if (j != cols_num - 2) {
                                                    System.out.print(" + ");
                                                } else {
                                                    System.out.println(" = " + matrix[i][cols_num - 1]);
                                                }
                                            }
                                        }
                                        System.out.println("\nTesting for equation 1: ");
                                        // Substitution
                                        for (int j = 0; j < cols_num - 1; j++) {
                                            System.out.print("(" + matrix[0][j] + ")(" + solutions[j] + ")");
                                            if (j != cols_num - 2) {
                                                System.out.print(" + ");
                                            } else {
                                                System.out.println(" = " + matrix[0][cols_num - 1]);
                                            }
                                        }
                                        // Evaluating the terms
                                        for (int j = 0; j < cols_num - 1; j++) {
                                            System.out.print("(" + matrix[0][j] * solutions[j] + ")");
                                            if (j != cols_num - 2) {
                                                System.out.print(" + ");
                                            } else {
                                                System.out.println(" = " + matrix[0][cols_num - 1]);
                                            }
                                        }
                                        System.out.println();
                                    } catch (NumberFormatException e) {
                                        System.out.print("Invalid Input! Please enter a number: ");
                                        userInput.nextLine();
                                    }
                                }
                            } catch (Exception e) {
                                System.out.print("Invalid Input! Please enter a number: ");
                            }

                            System.out.println("Would you like to input another substitute again? (Y/N): ");
                            char answer2 = userInput.next().charAt(0);
                            try {
                                if (answer2 == 'Y' || answer2 == 'y') {
                                    rsub = true;
                                } else if (answer2 == 'N' || answer2 == 'n') {
                                    rsub = false;
                                    break;
                                } else {
                                    System.out.println("Input invalid, input Y/N only. Please try again!");
                                }
                            } catch (Exception e) {
                                System.out.println("Input invalid. Please try again!");
                            }
                        }
                    } else if (answer1 == 'N' || answer1 == 'n') {
                        System.out.println("Understood.");
                    } else {
                        System.out.println("Input invalid, input Y/N only. Please try again!");
                    }
                } catch (Exception e) {
                    System.out.println("Input invalid. Please try again!");
                }
                userInput.close(); // close scanner to avoid memory leak
            }
        }

    }

    public static void printRdcechStep(double[][] matrix) {
        int rows_num = matrix.length;
        int cols_num = matrix[0].length;
        int step = 1;

        // Converting to Gauss-Jordan
        for (int x = 0; x < rows_num; x++) {
            double ind = matrix[x][x];
            for (int y = 0; y < 1 + rows_num; y++) {
                matrix[x][y] = matrix[x][y] / ind;
            }
            for (int y = 0; y < rows_num; y++) {
                if (x != y) {
                    double fac = matrix[y][x];
                    for (int z = 0; z < 1 + rows_num; z++) {
                        matrix[y][z] -= fac * matrix[x][z];
                    }
                }
                int l = 0;
                for (int s = 0; s < rows_num; s++)// Checking if no solution or infinite solution
                {
                    if (matrix[rows_num - 1][s] == 0.0) {
                        l = l + 1;
                    }
                }

                // Printing the steps
                System.out.println("Step " + step + " of RREF: ");
                for (int i = 0; i < rows_num; i++) {
                    System.out.print("|  ");
                    for (int j = 0; j < cols_num - 1; j++) {
                        if (Math.abs(matrix[i][j] - Math.floor(matrix[i][j])) < 0.001) {
                            double dbv = matrix[i][j];
                            int intv = (int) dbv;
                            System.out.print(intv + "   ");
                            dbv = (double) intv;
                            dbv = matrix[i][j];
                        } else if (matrix[i][j] == 0 || matrix[i][j] == -0) {
                            double dbv = matrix[i][j];
                            int intv = (int) dbv;
                            System.out.print(intv + "   ");
                            dbv = (double) intv;
                            dbv = matrix[i][j];
                        } else {
                            // By using %.2f, it will reduce the number of decimal places that will be
                            // printed into two only.
                            System.out.printf("%.2f" + "   ", matrix[i][j]);
                        }
                    }
                    if (Math.abs(matrix[i][cols_num - 1] - Math.floor(matrix[i][cols_num - 1])) < 0.001) {
                        double dbv = matrix[i][cols_num - 1];
                        int intv = (int) dbv;
                        System.out.println(":  " + intv + "  |");
                        dbv = (double) intv;
                        dbv = matrix[i][cols_num - 1];
                    } else if (matrix[i][cols_num - 1] == 0 || matrix[i][cols_num - 1] == -0) {
                        double dbv = matrix[i][cols_num - 1];
                        int intv = (int) dbv;
                        System.out.println(":  " + intv + "  |");
                        dbv = (double) intv;
                        dbv = matrix[i][cols_num - 1];
                    } else {
                        // By using %.2f, it will reduce the number of decimal places that will be
                        // printed into two only.
                        System.out.printf(":  " + "%.2f" + "  |\n", matrix[i][cols_num - 1]);
                    }
                }
                step = step + 1;
                if (l == rows_num) {
                    break;
                }
            }
        }
    }

    // This will print out the Augmented Identity Matrix Form of the called Matrix
    public static void printaugidtMat(double[][] matrix) {
        int rows_num = matrix.length;
        int cols_num = matrix[0].length;

        for (int i = 0; i < rows_num; i++) {
            System.out.print("|  ");
            for (int j = 0; j < cols_num - 1; j++) {
                if (Math.abs(matrix[i][j] - Math.floor(matrix[i][j])) < 0.001) {
                    double dbv = matrix[i][j];
                    int intv = (int) dbv;
                    System.out.print(intv + "   ");
                    dbv = (double) intv;
                    dbv = matrix[i][j];
                } else if (matrix[i][j] == 0 || matrix[i][j] == -0) {
                    double dbv = matrix[i][j];
                    int intv = (int) dbv;
                    System.out.print(intv + "   ");
                    dbv = (double) intv;
                    dbv = matrix[i][j];
                } else {
                    // By using %.2f, it will reduce the number of decimal places that will be
                    // printed into two only.
                    System.out.printf("%.2f" + "   ", matrix[i][j]);
                }
            }
            if (Math.abs(matrix[i][cols_num - 1] - Math.floor(matrix[i][cols_num - 1])) < 0.001) {
                double dbv = matrix[i][cols_num - 1];
                int intv = (int) dbv;
                System.out.println(":  " + intv + "  |");
                dbv = (double) intv;
                dbv = matrix[i][cols_num - 1];
            } else if (matrix[i][cols_num - 1] == 0 || matrix[i][cols_num - 1] == -0) {
                double dbv = matrix[i][cols_num - 1];
                int intv = (int) dbv;
                System.out.println(":  " + intv + "  |");
                dbv = (double) intv;
                dbv = matrix[i][cols_num - 1];
            } else {
                // By using %.2f, it will reduce the number of decimal places that will be
                // printed into two only.
                System.out.printf(":  " + "%.2f" + "  |\n", matrix[i][cols_num - 1]);
            }
        }
    }

    public static void printLinearEquations(double[][] matrix) {
        double[] solutions = extractSolutions(matrix);
        String[] variables = extractVar(matrix.length);

        for (int i = 0; i < matrix.length; i++) {
            boolean hasCoefficient = false; // Track if any non-zero coefficient has been encountered
            for (int j = 0; j < matrix[i].length - 1; j++) {
                if (matrix[i][j] != 0) {
                    if (hasCoefficient) {
                        System.out.print(" + ");
                    }
                    if (matrix[i][j] == 1) {
                        System.out.print(variables[j]);
                    } else {
                        int coefficient = (int) matrix[i][j]; // Cast the coefficient to an integer
                        System.out.print(coefficient + variables[j]);
                    }
                    hasCoefficient = true;
                }
            }
            int solution = (int) solutions[i]; // Cast the solution to an integer
            System.out.println(" = " + solution);
        }
    }

    // This method will print out the output of the Matrix that was called.
    public static void printMatrixForm(double[][] matrix) {
        double[] solutions = extractSolutions(matrix);
        String[] Var = extractVar(matrix.length);

        int rows_num = matrix.length;
        int cols_num = matrix[0].length;
        for (int i = 0; i < rows_num; i++) {
            System.out.print("|  ");
            for (int j = 0; j < cols_num - 1; j++) {
                if (Math.abs(matrix[i][j] - Math.floor(matrix[i][j])) < 0.001) {
                    double dbv = matrix[i][j];
                    int intv = (int) dbv;
                    System.out.print(intv + "   ");
                    dbv = (double) intv;
                    dbv = matrix[i][j];
                } else if (matrix[i][j] == 0 || matrix[i][j] == -0) {
                    double dbv = matrix[i][j];
                    int intv = (int) dbv;
                    System.out.print(intv + "   ");
                    dbv = (double) intv;
                    dbv = matrix[i][j];
                } else {
                    // By using %.2f, it will reduce the number of decimal places that will be
                    // printed into two only.
                    System.out.printf("%.2f" + "   ", matrix[i][j]);
                }
            }
            System.out.print("|  | " + Var[i] + " |  ");
            if (i == 1) {
                System.out.print("= ");
            } else {
                System.out.print("   ");
            }
            if (Math.abs(solutions[i] - Math.floor(solutions[i])) < 0.001) {
                double dbv = solutions[i];
                int intv = (int) dbv;
                System.out.println("|  " + intv + "  |");
                dbv = (double) intv;
                dbv = solutions[i];
            } else if (solutions[i] == 0 || solutions[i] == -0) {
                double dbv = solutions[i];
                int intv = (int) dbv;
                System.out.println("|  " + intv + "  |");
                dbv = (double) intv;
                dbv = solutions[i];
            } else {
                // By using %.2f, it will reduce the number of decimal places that will be
                // printed into two only.
                System.out.printf("|  " + "%.2f" + "  |", solutions[i]);
            }
        }
    }
}