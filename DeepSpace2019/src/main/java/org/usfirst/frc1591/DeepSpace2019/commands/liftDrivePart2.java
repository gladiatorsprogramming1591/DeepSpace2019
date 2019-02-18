package org.usfirst.frc1591.DeepSpace2019.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc1591.DeepSpace2019.Robot;

public class liftDrivePart2 extends Command {
    final double highRearHeight = 70.0;

    public liftDrivePart2() {
        requires(Robot.lift);
    }

    @Override
    protected void initialize() {
        Robot.lift.unliftFront();
        // Robot.driveTrain.fieldDrive();
    }

    @Override
    protected void execute() {
    }

    @Override
    protected boolean isFinished() {
        return Robot.lift.getRearHeight() < highRearHeight;
    }

    @Override
    protected void end() {
        // Robot.lift.disableRear();
    }

    @Override
    protected void interrupted() {
        end();
    }
}
