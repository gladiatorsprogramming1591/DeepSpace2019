// example of how to make a button that is triggered when two buttons are pressed
// we should trigger the reset gyro using this

package org.usfirst.frc1591.DeepSpace2019;

import edu.wpi.first.wpilibj.buttons.Button;

public class DoubleButton extends Button{
    Button m_buttonA,m_ButtonB;
    public DoubleButton(Button buttonA, Button buttonB){
        m_ButtonB = buttonB;
        m_buttonA = buttonA;
    }

    public boolean get(){
        return m_buttonA.get()&&m_ButtonB.get();        
    }
}