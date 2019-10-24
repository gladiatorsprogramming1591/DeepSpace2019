// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc1591.DeepSpace2019;

import org.usfirst.frc1591.DeepSpace2019.commands.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.usfirst.frc1591.DeepSpace2019.subsystems.*;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);

    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.

    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:

    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());

    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());

    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());

    public Joystick driveStick;
    public Joystick manipulatorStick;
    public JoystickButton clawOpenButton;
    public JoystickButton clawFlapButton;
    public JoystickButton liftCommandGroupButton;
    public JoystickButton elevatorTesButton;
    public JoystickButton toggleClawButton;
    public JoystickButton toggleClawFoldButton;
    public JoystickButton elevator1Button;
    public JoystickButton cargoshipButton;
    public JoystickButton elevator2Button;
    public JoystickButton liftButton;
    public JoystickButton fieldDrive;
    public JoystickButton robotDrive;
    public JoystickButton Gyro1;
    public JoystickButton Gyro2;
    public JoystickButton compressorButton;
    public DoubleButton resetGyro;

    public OI() {

        manipulatorStick = new Joystick(1);
        // clawOpenButton = new JoystickButton(manipulatorStick, 5);
        // clawFlapButton = new JoystickButton(manipulatorStick, 6);
        driveStick = new Joystick(0);

        // SmartDashboard Buttons
        SmartDashboard.putData("elevatorL2Hatch", new elevatorL2Hatch());
        SmartDashboard.putData("elevatorL1Hatch", new elevatorL1Hatch());
        SmartDashboard.putData("elevatorCargoShip", new elevatorCargoShip());
        // SmartDashboard.putData("driveManual", new driveManual());
        SmartDashboard.putData("clawOpen", new clawOpen());
        SmartDashboard.putData("clawClose", new clawClose());
        SmartDashboard.putData("clawFold", new clawFold());
        SmartDashboard.putData("clawUnfold", new clawUnfold());
        // SmartDashboard.putData("compressorOn", new compressorOn());
        // SmartDashboard.putData("compressorOff", new compressorOff());
        // SmartDashboard.putData("driveSlow", new driveSlow());
        // SmartDashboard.putData("timedDrive", new timedDrive(0, 0, 0, 0));
        // SmartDashboard.putData("distanceDrive", new distanceDrive());
        SmartDashboard.putData("liftRear", new liftRear());
        SmartDashboard.putData("unliftRear", new unliftRear());
        SmartDashboard.putData("liftFront", new liftFront());
        SmartDashboard.putData("unliftFront", new unliftFront());
        SmartDashboard.putData("resetGyro", new resetGyro());
        SmartDashboard.putData("liftCommandGroup1", new liftCommandGroup1());
        SmartDashboard.putData("resetGyro", new resetGyro());


        // manipulator stick buttons
        toggleClawButton = new JoystickButton(manipulatorStick, 6);
        toggleClawFoldButton = new JoystickButton(manipulatorStick, 5);
        elevator1Button = new JoystickButton(manipulatorStick, 2);
        cargoshipButton = new JoystickButton(manipulatorStick, 3);
        elevator2Button = new JoystickButton(manipulatorStick, 4);
        compressorButton = new JoystickButton(manipulatorStick, 1);
        liftButton = new JoystickButton(manipulatorStick, 9);
        
        fieldDrive = new JoystickButton(driveStick, 2);
        robotDrive = new JoystickButton(driveStick, 3);

        Gyro1 = new JoystickButton(driveStick, 9);
        Gyro2 = new JoystickButton(driveStick, 10);
        resetGyro = new DoubleButton(Gyro1, Gyro2);

        toggleClawButton.whenPressed(new toggleClaw());
        toggleClawFoldButton.whenPressed(new toggleClawFold());
        elevator1Button.whenPressed(new elevatorL1Hatch());
        cargoshipButton.whenPressed(new elevatorCargoShip());
        elevator2Button.whenPressed(new elevatorL2Hatch());
        liftButton.whenPressed(new liftCommandGroup1());
        fieldDrive.whenPressed(new driveManualExp());
        robotDrive.whenPressed(new driveManualRobot());
        resetGyro.whenPressed(new resetGyro());
        // compressorButton.whileHeld(new compressorRun());
    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
    public Joystick getdriveStick() {
        return driveStick;
    }

    public Joystick getmanipulatorStick() {
        return manipulatorStick;        
    }
   

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
    
}

