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

	public static final int A_NUM = 1;
	public static final int B_NUM = 2;
	public static final int X_NUM = 3;
	public static final int Y_NUM = 4;
	public static final int LB_NUM = 5;
	public static final int RB_NUM = 6;
	public static final int BACK_NUM = 7;
	public static final int START_NUM = 8;
	public static final int LEFT_JOY_NUM = 9;
	public static final int RIGHT_JOY_NUM = 10;
	
	public static final float RUMBLE_ON = 1;
	public static final float RUMBLE_OFF = 0;
	
	
	JoystickButton dX, dA, dB, dY, dLB, dRB, dBack, dStart, dLeftJoy, dRightJoy,
				   mX, mA, mB, mY, mLB, mRB, mBack, mStart, mLeftJoy, mRightJoy;
	Joystick drive, manipulator;
	
	public OI(){
		
		drive = new Joystick(1);
		dA = new JoystickButton(drive, A_NUM);//swtiches drive mode
		dB = new JoystickButton(drive, B_NUM);
		dX = new JoystickButton(drive, X_NUM);
		dY = new JoystickButton(drive, Y_NUM);
		dLB = new JoystickButton(drive, LB_NUM);//LB and RB trigger slow mode
		dRB = new JoystickButton(drive, RB_NUM);//
		dBack = new JoystickButton(drive, BACK_NUM);
		dStart = new JoystickButton(drive, START_NUM);
		dLeftJoy = new JoystickButton(drive, LEFT_JOY_NUM);
		dRightJoy = new JoystickButton(drive, RIGHT_JOY_NUM);
		
		manipulator = new Joystick(2);
		mA = new JoystickButton(manipulator, A_NUM);//stops flywheels
		mB = new JoystickButton(manipulator, B_NUM);//fly wheels shooting
		mX = new JoystickButton(manipulator, X_NUM);//fly wheels Intake
		mY = new JoystickButton(manipulator, Y_NUM);//piston in/out
		mLB = new JoystickButton(manipulator, LB_NUM);//allows control of winch
		mRB = new JoystickButton(manipulator, RB_NUM);
		mBack = new JoystickButton(manipulator, BACK_NUM);
		mStart = new JoystickButton(manipulator, START_NUM);
		mLeftJoy = new JoystickButton(manipulator, LEFT_JOY_NUM);
		mRightJoy = new JoystickButton(manipulator, RIGHT_JOY_NUM);
	
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
			manipulator.setRumble(RumbleType.kLeftRumble,RUMBLE_ON);
			manipulator.setRumble(RumbleType.kRightRumble,RUMBLE_ON);
		}
		else
		{
			manipulator.setRumble(RumbleType.kLeftRumble,RUMBLE_OFF);
			manipulator.setRumble(RumbleType.kRightRumble,RUMBLE_OFF);
		}
			
	}
	public void setDriveVibrate(boolean status)
	{
		if(status)
		{
			drive.setRumble(RumbleType.kLeftRumble,RUMBLE_ON);
			drive.setRumble(RumbleType.kRightRumble,RUMBLE_ON);
		}
		else
		{
			drive.setRumble(RumbleType.kLeftRumble,RUMBLE_OFF);
			drive.setRumble(RumbleType.kRightRumble,RUMBLE_OFF);
		}	
	}
}














