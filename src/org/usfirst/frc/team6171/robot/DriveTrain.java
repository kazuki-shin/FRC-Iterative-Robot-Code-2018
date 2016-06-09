package org.usfirst.frc.team6171.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;

public class DriveTrain extends RobotDrive{

	public DriveTrain(){
		super(new VictorSP(0), new VictorSP(1), new VictorSP(2), new VictorSP(3));
		setInvertedMotor(MotorType.kFrontRight, true);
		setInvertedMotor(MotorType.kRearRight, true);
        setInvertedMotor(MotorType.kFrontLeft, true);
        setInvertedMotor(MotorType.kRearLeft, true);
        String driveSpeedSelected = (String )SmartMaker.driveSpeed.getSelected();
    	switch(driveSpeedSelected){
    		case "Comp Speed": Robot.sensitivity = 0.7; break;
    		case "Kid Speed": Robot.sensitivity = .4; break;
    		case "Max Speed": Robot.sensitivity = 1; break;
    		case "No Speed": Robot.sensitivity = 0; 
    		default: Robot.sensitivity = 0; 
    				 System.out.println("No drive speed selected");
    				 
    	}
        
	}
	
}
