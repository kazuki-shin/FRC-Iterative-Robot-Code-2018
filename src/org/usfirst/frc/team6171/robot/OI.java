package org.usfirst.frc.team6171.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.RumbleType;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class OI {
	public static final int LEFTX = 0;
	public static final int LEFTY = 1;
	public static final int RTRIGGER = 3;
	public static final int LTRIGGER = 2;
	public static final int RIGHTX = 4;
	public static final int RIGHTY = 5;

	JoystickButton dX, dA, dB, dY, dLB, dRB, dBack, dStart, dLeftJoy, dRightJoy,
				   mX, mA, mB, mY, mLB, mRB, mBack, mStart, mLeftJoy, mRightJoy;
	Joystick drive, manipulator;
	
	public OI(){
		
		drive = new Joystick(1);
		dA = new JoystickButton(drive, 1);
		dB = new JoystickButton(drive, 2);
		dX = new JoystickButton(drive, 3);
		dY = new JoystickButton(drive, 4);
		dLB = new JoystickButton(drive, 5);
		dRB = new JoystickButton(drive, 6);
		dBack = new JoystickButton(drive, 7);
		dStart = new JoystickButton(drive, 8);
		dLeftJoy = new JoystickButton(drive, 9);
		dRightJoy = new JoystickButton(drive, 10);
		
		manipulator = new Joystick(2);
		mA = new JoystickButton(manipulator, 1);
		mB = new JoystickButton(manipulator, 2);
		mX = new JoystickButton(manipulator, 3);
		mY = new JoystickButton(manipulator, 4);
		mLB = new JoystickButton(manipulator, 5);
		mRB = new JoystickButton(manipulator, 6);
		mBack = new JoystickButton(manipulator, 7);
		mStart = new JoystickButton(manipulator, 8);
		mLeftJoy = new JoystickButton(manipulator, 9);
		mRightJoy = new JoystickButton(manipulator, 10);
	
	}
	
	//gets the axis value for the drive joy stick
	public double getDriveLeftY(){
		return drive.getRawAxis(LEFTY);
	}
	public double getDriveLeftX(){
		return drive.getRawAxis(LEFTX);
	}
	public double getDriveRightY(){
		return drive.getRawAxis(RIGHTY);
	}
	public double getDriveRightX(){
		return drive.getRawAxis(RIGHTX);
	}
	public double getDriveRightTrigger()
	{
		return drive.getRawAxis(RTRIGGER);
	}
	public double getDriveLeftTrigger()
	{
		return drive.getRawAxis(LTRIGGER);
	}
	//gets the axis values for the manipulator joy stick
	public double getManipulatorLeftY(){
		return manipulator.getRawAxis(LEFTY);
	}
	public double getManipulatorLeftX(){
		return manipulator.getRawAxis(LEFTX);
	}
	public double getManipulatorRightY(){
		return manipulator.getRawAxis(RIGHTY);
	}
	public double getManipulatorRightX(){
		return manipulator.getRawAxis(RIGHTX);
	}

	public double getManipulatorPOV(){
		return manipulator.getPOV();
	}
	public void setManipulatorVibrate(boolean status)
	{
		if(status)
		{
			manipulator.setRumble(RumbleType.kLeftRumble,1);
			manipulator.setRumble(RumbleType.kRightRumble,1);
		}
		else
		{
			manipulator.setRumble(RumbleType.kLeftRumble,0);
			manipulator.setRumble(RumbleType.kRightRumble,0);
		}
			
	}
	public void setDriveVibrate(boolean status)
	{
		if(status)
		{
			drive.setRumble(RumbleType.kLeftRumble,1);
			drive.setRumble(RumbleType.kRightRumble,1);
		}
		else
		{
			drive.setRumble(RumbleType.kLeftRumble,0);
			drive.setRumble(RumbleType.kRightRumble,0);
		}	
	}
}














