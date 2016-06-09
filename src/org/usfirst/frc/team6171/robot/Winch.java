package org.usfirst.frc.team6171.robot;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class Winch {
	CANTalon winch;
	double setpoint;
	MyPIDController winchPID;
	
	public static double WinchKp = .035;
	
	public Winch(){
		winch = new CANTalon(RobotMap.KWinch);
		winch.changeControlMode(TalonControlMode.PercentVbus);
		setpoint = 0;
		winchPID = new MyPIDController(.01715, .03, 0.004);
		winchPID.enable();
		winchPID.setOutputRange(-.25, .25);
		winchPID.setTolerance(.7);
		winchPID.setDivide(-4);
	
	 }
	 
	public void enable()
	{
		winchPID.enable();
		winchPID.setOutputRange(-.25, .25);
		winchPID.setTolerance(.7);
	}
	public void disable()
	{
		winchPID.disable();
	}
	public void controlWinch(double roll) {

		 SmartDashboard.putNumber("Set Point", setpoint);
		 SmartDashboard.putNumber("Roll", roll);
		 winchPID.setSetPoint(setpoint);
		 double output = -winchPID.getOutput(roll);
		 winch.set(output);
	 }
	 
	 public void controlWinch(double mInput, double roll){
			// SmartDashboard.putNumber("joyInput", joyInput);
			 winchPID.changeSetPoint(-mInput*.2);
			 changeAngle(-mInput*.2);
			 double output = -winchPID.getOutput(roll);
			 winch.set(output);
	 }
	 
	 public void changeAngle(double angle){
		 setpoint += angle;
		 if(setpoint>60)
		 {
			 setpoint = 60;
		 }
		 if(setpoint<-20)
		 {
			 setpoint = -20;
		 }
	 }
	 
	 public void setAngle(double angle) {
		 setpoint = angle;
		 winchPID.enable();
	 }
	 
	 public void setWinchTolerance(double tol){
		 winchPID.setTolerance(tol);
	 }
 }
 

