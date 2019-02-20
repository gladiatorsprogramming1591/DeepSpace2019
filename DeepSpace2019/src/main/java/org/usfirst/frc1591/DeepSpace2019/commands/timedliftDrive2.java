package org.usfirst.frc1591.DeepSpace2019.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc1591.DeepSpace2019.Robot;

public class timedliftDrive2 extends Command {
    double m_timeout = 2.0;
    double m_speed = 0.65;
    private int modValue;
    
    public timedliftDrive2(double time) {
        requires(Robot.lift);
        m_timeout = time;
    }

    public timedliftDrive2(double time, double speed) {
        requires(Robot.lift);
        m_timeout = time;
    }

    @Override
    protected void initialize() {
        setTimeout(m_timeout);
        Robot.lift.turnWheel(m_speed);
    }

    @Override
    protected void execute() {
        if ((modValue++ % 25) == 0) {
            System.out.println("frontHeight: " + Robot.lift.getFrontHeight());
        }
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut() ;// (Robot.lift.getFrontHeight() <= 5));
    }

    @Override
    protected void end() {
        Robot.lift.stopWheel();
    }

    @Override
    protected void interrupted() {
        end();
    }
}
