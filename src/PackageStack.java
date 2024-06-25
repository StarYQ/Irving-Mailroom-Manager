/**
 * Name: Arnab Bhowmik
 * ID: 115610923
 * Recitation Section: 1
 */

import java.util.Stack;

/**
 * Represents the stacks that store the mail packages
 */
public class PackageStack extends Stack<Package> {
    private final int CAPACITY = 7;
    /**
     * Pushes a package onto the top of the stack
     *
     * @param x The package to be pushed
     * @throws FullStackException if the stack is at capacity
     */
    public void pushPackage(Package x) throws FullStackException {
        if (isFull()) {
            throw new FullStackException("The stack is at capacity.");
        }
        push(x);
    }
    /**
     * Removes and returns the topmost package from the stack
     *
     * @return The topmost package
     * @throws EmptyStackException if the stack is empty
     */
    public Package popPackage() throws EmptyStackException {
        if (isEmpty()) {
            throw new EmptyStackException("Source stack is empty. Cannot move a package.");
        }
        return pop();
    }

    /**
     * Reverses a stack recursively
     */
    public void reverse(){
        if (!isEmpty()) {
            Package p = this.peek();
            this.pop();
            reverse();
            addToBottom(p);
        }
    }

    /**
     * Helper method for reversing a stack recursively
     * @param p topmost package of a stack to remove from the top of a
     *          stack and add to its bottom
     */
    public void addToBottom(Package p){
        if (isEmpty())
            this.push(p);
        else{
            Package p2 = this.peek();
            this.pop();
            addToBottom(p);
            this.push(p2);
        }
    }
    /**
     * Returns the topmost package from the stack without removing it
     * @return The topmost package
     * @throws EmptyStackException if the stack is empty
     */
    public Package peekPackage() throws EmptyStackException {
        if (isEmpty()) {
            throw new EmptyStackException("Empty Stack therefore cannot get packages.");
        }
        return peek();
    }

    /**
     * Checks if the stack is at full capacity
     * @return True if the stack is full, false otherwise
     */
    public boolean isFull() {
        return size() == CAPACITY;
    }
}
