package org.usfirst.frc1591.DeepSpace2019.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc1591.DeepSpace2019.Robot;

public class elevatorCargoShip extends Command {

    int posIndex = 1;
    
    public elevatorCargoShip() {
        requires(Robot.elevator);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        Robot.elevator.moveInit(posIndex);
        // Robot.elevator.up();
        // setTimeout(0.5);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return Robot.elevator.moveIsFinished(posIndex);
        // return isTimedOut();
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.elevator.pause();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}
