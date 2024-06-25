/**
 * Name: Arnab Bhowmik
 * ID: 115610923
 * Recitation Section: 1
 */
import java.util.Scanner;
/**
 * Represents the simulation of the Irving mailroom
 * mail package stacks and takes user inputs to do so
 */
public class MailroomManager {
    private static PackageStack[] stacks = new PackageStack[5];
    private static PackageStack floor = new PackageStack();
    public static void main(String[] args){
        initializeStacks();
        Scanner scanner = new Scanner(System.in);
        int day = 0;
        System.out.println("Welcome to the Irving Mailroom Manager. It is day 0.");
        while (true) {
            printMenu();
            System.out.print("Please select an option: ");
            String option = scanner.nextLine().toUpperCase();
            switch (option) {
                case "D":
                    deliverPackage(scanner, day);
                    break;
                case "G":
                    getPackages(scanner);
                    break;
                case "T":
                    day++;
                    System.out.println("It is now day " + day + ".");
                    break;
                case "P":
                    printStacks();
                    break;
                case "M":
                    movePackage(scanner);
                    break;
                case "F":
                    findAndMoveToFloor();
                    break;
                case "L":
                    listPackages(scanner);
                    break;
                case "E":
                    emptyFloor();
                    break;
                case "Q":
                    System.out.println("Use Amazon Locker next time.");
                    System.out.println("(A-G, H-J, K-M, N-R, S-Z)");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice! Please try again.");
                    break;
            }
        }
    }

    /**
     * Initializes the 6 mail package stacks for the mailroom
     */
    private static void initializeStacks() {
        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = new PackageStack();
        }
    }

    /**
     * Method to print the menu each time it is required
     */
    private static void printMenu() {
        System.out.println("Menu:");
        System.out.println("D) Deliver a package");
        System.out.println("G) Get someone's package");
        System.out.println("T) Make it tomorrow");
        System.out.println("P) Print the stacks");
        System.out.println("M) Move a package from one stack to another");
        System.out.println("F) Find packages in the wrong stack and move to floor");
        System.out.println("L) List all packages awaiting a user");
        System.out.println("E) Empty the floor.");
        System.out.println("Q) Quit");
    }

    /**
     * Method to push a package with attributes specified by user inputs to stack
     * corresponding with recipient's name (if full stack, added to nearest non-full
     * stack instead)
     * @param scanner takes user inputs to fill parameters to construct the new
     * @param day current day
     */
    private static void deliverPackage(Scanner scanner, int day) {
        System.out.print("Please enter the recipient name: ");
        String recipient = scanner.nextLine().trim();
        if (recipient.isEmpty()) {
            System.out.println("Recipient name cannot be empty. Please try again.");
            return;
        }
        System.out.print("Please enter the weight (lbs): ");
        double weight = scanner.nextDouble();
        scanner.nextLine();
        Package pkg = new Package(recipient, day, weight);
        char stackLabel = getStackLabel(recipient);
        if (!stacks[stackLabel - 'A'].isFull()) {
            try {
                stacks[stackLabel - 'A'].pushPackage(pkg);
                System.out.println("A " + Math.round(pkg.getWeight()) + " lb package is awaiting pickup by " + recipient + ".");
            } catch (FullStackException e) {
                System.out.println("Error: Unable to deliver the package to Stack " + stackLabel + ".");
            }
        } else {
            //If designated stack is full, move the package to next nearest non-full stack
            movePackageToNearestNonFullStack(pkg, stackLabel);
        }
    }

    /**
     * Method to move package to nearest non-full stack if original stack is full
     * @param pkg package to be moved to nearest non-full stack
     * @param originalStackLabel the name of the stack (which was full) that the package
     *                           was originally supposed to be added to if it wasn't full
     */
    private static void movePackageToNearestNonFullStack(Package pkg, char originalStackLabel) {
        PackageStack originalStack = stacks[originalStackLabel - 'A'];
        if (!originalStack.isFull()) {
            System.out.println("A " + Math.round(pkg.getWeight()) + " lb package is awaiting pickup by " + pkg.getRecipient() +
                    ". It was added to stack " + (originalStackLabel - 'A' + 1) + ".");
            return;
        }
        int nextStackIndex = (originalStackLabel - 'A' + 1) % stacks.length;
        int iterations = stacks.length;
        while (iterations > 0) {
            PackageStack newStack = stacks[nextStackIndex];
            if (!newStack.isFull()) {
                try {
                    newStack.pushPackage(pkg);
                    System.out.println("A " + Math.round(pkg.getWeight()) + " lb package is awaiting pickup by " + pkg.getRecipient() +
                            ". As stack " + (originalStackLabel - 'A' + 1) + " was full, it was placed in stack " +
                            (nextStackIndex + 1) + ".");
                    return;
                } catch (FullStackException e) {
                    nextStackIndex = (nextStackIndex + 1) % stacks.length;
                }
            }
            iterations--;
            nextStackIndex = (nextStackIndex + 1) % stacks.length;
        }
        //Prints if all stacks are full
        System.out.println("All stacks are at capacity. Unable to deliver the package.");
    }

    /**
     * Gives the topmost package belonging to a specified recipient to said recipient
     * @param scanner takes user input for the recipient name
     */
    private static void getPackages(Scanner scanner){
        System.out.print("Please enter the recipient name: ");
        String recipient = scanner.nextLine();
        boolean allEmpty = true;
        for (PackageStack pkgStack : stacks){
            if (!pkgStack.isEmpty()){
                allEmpty = false;
                break;
            }
        }
        if (allEmpty){
            try {
                stacks[0].peekPackage();
            }
            catch (EmptyStackException e) {
                System.out.println(e.getMessage());
            }
        }
        int totalPackages = 0;
        for (int i = 0; i < stacks.length; i++) {
            PackageStack stack = stacks[i];
            Package foundPackage = null;
            stack.reverse();
            for (Package pkg : stack) {
                if (pkg.getRecipient().equals(recipient)) {
                    foundPackage = pkg;
                    break;
                }
            }
            stack.reverse();
            if (foundPackage!=null){
                try {
                    Package curr = stack.peek();
                    while (curr!=foundPackage){
                        floor.pushPackage(stack.popPackage());
                        curr = stack.peek();
                        totalPackages++;
                    }
                    System.out.println("Move " + totalPackages + " packages from Stack" + i + " to floor.");
                    printStacks();
                    System.out.println("Give " + recipient + " " + Math.round(foundPackage.getWeight()) + " lb package delivered on day " + foundPackage.getArrivalDate() + ".");
                    stack.pop();
                    for (int j=totalPackages; j>=0; j++){
                        stack.pushPackage(floor.popPackage());
                    }
                    System.out.println("Return " + totalPackages + " packages to stack" + i + " from floor.");
                } catch (EmptyStackException | FullStackException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        System.out.println("Return " + totalPackages + " packages to stack from floor.");
        for (int i = 0; i < totalPackages; i++) {
            try {
                stacks[getStackLabel(recipient) - 'A'].pushPackage(floor.popPackage());
            } catch (EmptyStackException | FullStackException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Finds the stack respective to a specified recipient's name based on their first name
     * @param recipient recipient to find their respective stack for
     * @return char value that allows for classification of the stack corresponding with
     * the recipient's first name
     */
    private static char getStackLabel(String recipient) {
        if (recipient == null || recipient.isEmpty()) {
            return 'E';
        }
        char firstLetter = recipient.toUpperCase().charAt(0);
        if (firstLetter >= 'A' && firstLetter <= 'G') return 'A';
        if (firstLetter >= 'H' && firstLetter <= 'J') return 'B';
        if (firstLetter >= 'K' && firstLetter <= 'M') return 'C';
        if (firstLetter >= 'N' && firstLetter <= 'R') return 'D';
        if (firstLetter >= 'S' && firstLetter <= 'Z') return 'E';
        return 'E';
    }

    /**
     * Prints the current state of the 6 package stacks
     */
    private static void printStacks() {
        System.out.println("Current Packages:");
        System.out.println("--------------------------------");
        for (int i = 0; i < stacks.length; i++) {
            String stackName = "";
            if (i==0){
                stackName = "(A-G)";
            }
            else if (i==1){
                stackName = "(H-J)";
            }
            else if (i==2){
                stackName = "(K-M)";
            }
            else if (i==3){
                stackName = "(N-R)";
            }
            else if (i==4){
                stackName = "(S-Z)";
            }
            System.out.printf("Stack %d %s:", i + 1, stackName);
            if (stacks[i].isEmpty()) {
                System.out.println("|empty.");
            } else {
                System.out.print("|");
                for (Package pkg : stacks[i]) {
                    System.out.printf("[%s %d]", pkg.getRecipient(), pkg.getArrivalDate());
                }
                System.out.println();
            }
        }
        System.out.println("Floor: |" + (floor.isEmpty() ? "empty." : floor.toString()));
    }

    /**
     * Method to take user inputs to move the topmost package of a stack
     * to a different specified stack
     * @param scanner takes user inputs to specify the source and destination stacks
     */
    private static void movePackage(Scanner scanner) {
        System.out.print("Please enter the source stack (enter 0 for floor): ");
        int sourceStackIndex = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Please enter the destination stack: ");
        int destStackIndex = scanner.nextInt();
        scanner.nextLine();
        System.out.println(sourceStackIndex);
        try {
            Package pkg;
            if (sourceStackIndex == 0) {
                pkg = floor.popPackage();
            }
            else {
                pkg = stacks[sourceStackIndex - 1].popPackage();
            }
            if (destStackIndex==0){
                floor.pushPackage(pkg);
            }
            else{
                stacks[destStackIndex - 1].pushPackage(pkg);
            }
            System.out.println("Package moved successfully from stack " +
                    sourceStackIndex + " to stack " + destStackIndex);
        } catch (EmptyStackException | FullStackException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Looks for the package(s) in the wrong stack and moves them to the floor
     */
    private static void findAndMoveToFloor() {
        for (int i = 0; i < stacks.length; i++) {
            PackageStack stack = stacks[i];
            Package foundPackage = null;
            for (Package pkg : stack) {
                if (getStackLabel(pkg.getRecipient()) != ('A' + i)) {
                    foundPackage = pkg;
                    break;
                }
            }
            if (foundPackage != null) {
                try {
                    floor.pushPackage(stack.popPackage());
                } catch (EmptyStackException | FullStackException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        System.out.println("Misplaced packages moved to floor.");
    }

    /**
     * Counts and print the packages for the recipient in each stack
     * @param scanner takes user input to specify the recipient name to list packages for
     */
    private static void listPackages(Scanner scanner) {
        System.out.print("Please enter the recipient name: ");
        String recipient = scanner.nextLine();
        int totalPackages = 0;
        for (int i = 0; i < stacks.length; i++) {
            PackageStack stack = stacks[i];
            for (Package pkg : stack) {
                if (pkg.getRecipient().equals(recipient)) {
                    totalPackages++;
                }
            }
        }
        if (totalPackages==0){
            System.out.println("No packages found for " + recipient + ".");
        }
        else {
            if (totalPackages==1){
                System.out.println(recipient + " has 1 package total.");
            }
            else{
                System.out.println(recipient + " has " + totalPackages + " packages total.");
            }
            for (int i = 0; i < stacks.length; i++) {
                PackageStack stack = stacks[i];
                int packageCount = 0;
                for (Package pkg : stack) {
                    if (pkg.getRecipient().equals(recipient)) {
                        totalPackages++;
                        packageCount++;
                        System.out.println("Package " + packageCount + " is in Stack " + (i + 1) + ", it was delivered on day " +
                                pkg.getArrivalDate() + ", and weighs " + Math.round(pkg.getWeight()) + " lbs");
                    }
                }
            }
        }
    }

    /**
     * Method to pop all packages from the floor stack
     */
    private static void emptyFloor() {
        while (!floor.isEmpty()) {
            floor.pop();
        }
        System.out.println("The floor has been emptied. Mr. Trash Can is no longer hungry.");
    }
}