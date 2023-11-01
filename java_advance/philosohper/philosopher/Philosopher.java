package philosopher;
import java.util.Random;

public class Philosopher implements Runnable{
    private final Fork leftFork;
    private final Fork rightFork;
    private final int id;
    //you could check about three status, leftFork, rightFork and id
    
    private enum State {AFFAME, MANGER, REFLECHIR};
    private State state;

    private Random random = new Random();

    private int turns = 0;

    public Philosopher(Fork leftFork, Fork rightFork, int id){
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.id = id;
        this.state = State.REFLECHIR;
        //the normal status is reflechir;
    }

    public void run(){
        try{
            while(!Thread.currentThread().isInterrupted()){
                switch(state){
                    case REFLECHIR:
                        Thread.sleep(random.nextInt(256)); // 1 a 256 milliseconds
                        state = State.AFFAME;
                        System.out.println(id + " is reflecting.");
                        break;
                    
                    case AFFAME:
                    //The philosopher should take the forks
                        System.out.println(id + " is hungry and tries to pick up the left fork.");
                        leftFork.pickUp();
                        try{
                            System.out.println(id + " picked up the left fork and tries to pick up the right fork.");
                            rightFork.pickUp();
                            System.out.println(id + " picked up the right fork.");
                            //If a philosopher takes the both forks, then change status into manger
                            state = State.MANGER;
                            turns++;
                        } catch (InterruptedException e){
                            System.out.println(id + " couldn't pick up the right fork and put down the left fork");
                            leftFork.putDown();
                            Thread.sleep(100); //Add a delay to avoid an infinite loop if a philosopher failing to take the right fork.
                            break;
                        }
                        break;
                    case MANGER:
                        System.out.println(id + " is eating.");
                        Thread.sleep(random.nextInt(256));
                        leftFork.putDown();//First put down the left fork
                        rightFork.putDown();
                        System.out.println(id + " puts down the left fork and right fork.");
                        state = State.REFLECHIR;
                        break;
                }
            }
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    //Add this getter to access the turns from main class
    public int  getTurns(){
        return turns;
    }

    public int getId(){
        return this.id;
    }

}