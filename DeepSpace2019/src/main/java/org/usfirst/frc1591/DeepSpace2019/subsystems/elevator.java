package org.usfirst.frc1591.DeepSpace2019.subsystems;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;

public class elevator extends Subsystem {

    private DigitalInput elevatorTopSwitch;
    private DigitalInput elevatorBottomSwitch;
    private Spark elevatorMotor;

    Encoder enc;
    ArrayList<Integer> elevatorPositions;

    final double ELEVATOR_UP_SPEED = 0.5;
    final double ELEVATOR_DOWN_SPEED = -0.3;
    final double ELEVATOR_BRAKE_SPEED = 0.2;
    boolean bottomState; // state true if bottom limit switch is pushed in
    boolean topState; // state true if top limit switch is pushed in

    double encoder;

    boolean state;

    // Commmand variables
    boolean finished = false;
    int direction;

    boolean limitSwitchState = false;
    boolean distanceReached = false;

    // Constants for the elevator stops, CURRENTLY PLACEHOLDERS
    int L1HATCH_POS = -15;
    int CARGO_SHIP_POS = -340;
    int L2HATCH_POS = -660;

    public elevator() {
        elevatorTopSwitch = new DigitalInput(0);
        addChild("elevatorTopSwitch",elevatorTopSwitch);
        
        
        elevatorBottomSwitch = new DigitalInput(1);
        addChild("elevatorBottomSwitch",elevatorBottomSwitch);
        
        
        elevatorMotor = new Spark(1);
        addChild("elevatorMotor",elevatorMotor);
        elevatorMotor.setInverted(true);
        
        // Construct arraylist
        elevatorPositions = new ArrayList<Integer>();
        
        // Construct encoder
        enc = new Encoder(2, 3, false, Encoder.EncodingType.k4X); //default encoder settings, need more info on the one we're using 
        SmartDashboard.putData("encoder", enc);

        // Add elevator switches to smart dashboard
        SmartDashboard.putData("elevatorTopSwitch", elevatorTopSwitch);
        SmartDashboard.putData("elevatorBottomSwitch", elevatorBottomSwitch);
        SmartDashboard.putData("elevatorMotor", elevatorMotor);

        // Create array that holds the different stops on the elevator
        elevatorPositions.add(L1HATCH_POS);
        elevatorPositions.add(CARGO_SHIP_POS);
        elevatorPositions.add(L2HATCH_POS);
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    @Override
    public void periodic() {
        // This code is run every loop
        if (elevatorMotor.get() < 0 && getTopSwitchState() == true) {
            pause();
            System.out.println("Top switch pressed. ABORT ABORT ABORT");
        }
        else if(elevatorMotor.get() >= 0 && getBottomSwitchState() == true) {
            stop();
            enc.reset();
            // System.out.println("Bottom switch pressed. ABORT ABORT ABORT");
        }
    }

    // Get the current position of the elevator
    public int getCurrentPos(){
        return enc.get();
    }

    // Get any elevator position out of the elevatorPositions array
    public int getElevatorPositions(int index) {
        return elevatorPositions.get(index);
    }

    // Get the direction needed to be travelled by the elevator. 1 is up, -1 id down, 0 is not move
    public int getdirection(int targetPosIndex) {
        System.out.println("Moving from " + getCurrentPos() + " to " + getElevatorPositions(targetPosIndex));
        int direction;
        if (getElevatorPositions(targetPosIndex) < getCurrentPos()) {
            direction = 1;
        }
        else if (getElevatorPositions(targetPosIndex) > getCurrentPos()) {
            direction = -1;
        }
        else {
            direction = 0;
        }
        return direction;
    }

    // Check if limit switches are pushed in, true means pushed in INVERTED (DONT KNOW HOW THEY WILL BE WIRED)
    public boolean getBottomSwitchState(){
        return !elevatorBottomSwitch.get();
    }

    public boolean getTopSwitchState(){
        return !elevatorTopSwitch.get();
    }

    // Returns true if either switch is pushed in
    public boolean getSwitchesStates() {
        if(getTopSwitchState() == true || getBottomSwitchState() == true) {
            state = true;
        }
        else {
            state = false;
        }
        return state;
    }

    // Movement of the motor
    public void up(){
        System.out.println("Moving on up!");
        elevatorMotor.set(ELEVATOR_UP_SPEED);
    }

    public void down(){
        System.out.println("Moving down below!");
        elevatorMotor.set(ELEVATOR_DOWN_SPEED);
    }

    public void directionMove(int direction) {
        switch(direction) {
            case 1:
                up();
                break;
            case -1:
                down();
                break;
            case 0:
                stop();
                break;
        }
    }

    // Reset encoder
    public void resetEncoder() {
        enc.reset();
    }

    // Called when an elevator command is interrupted or ended
    public void stop(){
        elevatorMotor.set(0);
    }

    // Called when an elevator command is interrupted or ended
    public void pause(){
        elevatorMotor.set(ELEVATOR_BRAKE_SPEED);
    }
    
    // Command methods

    public void moveInit(int posIndex) {
        distanceReached = false;
        finished = false;

        System.out.println("Moving to position " + posIndex);
        direction = getdirection(posIndex); // get direction and speed motor needs to move
        directionMove(direction);
        System.out.println("Motor moving in direction: " + direction);
    }

    public boolean moveIsFinished(int posIndex) {
        switch (direction) {
            case 1:
                if (getCurrentPos() <= getElevatorPositions(posIndex)) {
                    distanceReached = true;
                }
                break;
            case -1:
                if (getCurrentPos() >= getElevatorPositions(posIndex)) {
                    distanceReached = true;
                }
                break;
            case 0:
                distanceReached = true;
        }
        
        if (distanceReached == true) {
            finished = true;
            System.out.println("Motor stopping!");
        }
        return finished;
    }
}

