package org.usfirst.frc.team6171.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends RobotDrive{

	PIDController pid;
    DriveOutput pidOut;
    
    double distanceSetpoint, angleSetpoint;
    public Encoder leftEnc;
    private boolean turnDone;
	
	public static final double Kp = .4;
    public static final double Ki = .006;
    public static final double Kd = .05;
	
	public DriveTrain(){
		super(new VictorSP(0), new VictorSP(1), new VictorSP(2), new VictorSP(3));
		setInvertedMotor(MotorType.kFrontRight, true);
		setInvertedMotor(MotorType.kRearRight, true);
        setInvertedMotor(MotorType.kFrontLeft, true);
        setInvertedMotor(MotorType.kRearLeft, true);
        
        leftEnc = new Encoder(0, 1, false, EncodingType.k4X);
		//rightEnc = new Encoder(2, 3, false, EncodingType.k4X);
		leftEnc.setDistancePerPulse(.08726646);
        //rightEnc.setDistancePerPulse(.08726646);
        leftEnc.setMinRate(1);
        //rightEnc.setMinRate(1);
        leftEnc.setSamplesToAverage(5);
        //rightEnc.setSamplesToAverage(5);
        leftEnc.setPIDSourceType(PIDSourceType.kDisplacement);
        
        String driveSpeedSelected = (String )SmartMaker.driveSpeed.getSelected();
    	switch(driveSpeedSelected){
    		case "Comp Speed": Robot.sensitivity = 0.9; break;
    		case "Kid Speed": Robot.sensitivity = .4; break;
    		case "Max Speed": Robot.sensitivity = 1; break;
    		case "No Speed": Robot.sensitivity = 0; 
    		default: Robot.sensitivity = 0; 
    				 System.out.println("No drive speed selected");
    	}
    	
    	pidOut = new DriveOutput();
        pid = new PIDController(Kp, Ki, Kd, leftEnc, pidOut);
        pid.setOutputRange(-.6, .6);
        //pid.setInputRange(-255, 5);
        pid.setPercentTolerance(10);
        pid.setContinuous();
        turnDone = false;
	}
	
	public void resetEncoders()
	{
		leftEnc.reset();
    	//rightEnc.reset();
	}
	
	public void setOutput(double out){
		pid.setOutputRange(out, out);
	}
	
	public void setDistanceSetpoint(double setPoint)
	{
		this.distanceSetpoint = setPoint;
		pid.setSetpoint(distanceSetpoint);
	}
	
	public void pidEnable(){
		pid.enable();
	}
	public void pidDisable(){
		pid.disable();
	}
	
	public void setAngleSetpoint(double a){
		angleSetpoint = a;
	}
	
	public boolean getTurnDone(){
		return turnDone;
	}
	public void setTurnDone(boolean b){
		turnDone = b;
	}
	
	public boolean getDistanceDone(){
		return !pid.isEnabled();
	}
	public void setOutputRange(double a, double b)
	{
		pid.setOutputRange(a, b);
	}
	
	public void turnToAngle(){
		double temp = Robot.ahrs.getYaw() - angleSetpoint;
		//System.out.println(temp);
		double output = temp * .08;
		output = Math.max(-.9,Math.min(.9,output));
		//System.out.println(output);
		arcadeDrive(0,-output);
		if(Math.abs(temp)<5)
			turnDone = true;
	}
	public void driveDistanceForwards()
	{
		log();
		SmartDashboard.putNumber("Yaw", Robot.ahrs.getYaw());
		//while(!pid.onTarget())
		//{
			//drive.drive(.3, 0.0);
		//	setAngle(Robot.ahrs.getYaw() * .1);
		//}
		//pid.disable();
		pidOut.setAngle(-Robot.ahrs.getYaw() * .2);
	    	if(pid.onTarget())
	    		pid.disable();

	}
	public void driveDistanceBackwards()
	{
		log();
		SmartDashboard.putNumber("Yaw", Robot.ahrs.getYaw());
		//while(!pid.onTarget())
		//{
			//drive.drive(.3, 0.0);
		//	setAngle(Robot.ahrs.getYaw() * .1);
		//}
		//pid.disable();
		pidOut.setAngle(Robot.ahrs.getYaw() * .2);
	    	if(pid.onTarget())
	    		pid.disable();

	}
	
	
	
	public void log(){
		SmartDashboard.putNumber("Left Speed", leftEnc.getRate());
		//SmartDashboard.putNumber("Right Speed", rightEnc.getRate());
		SmartDashboard.putNumber("Left Distance", leftEnc.getDistance());
		//SmartDashboard.putNumber("Right Distance", rightEnc.getDistance());
	}
	private class DriveOutput implements PIDOutput {
		double angle;
		public DriveOutput()
		{
			angle = 0;
		}
		public void setAngle(double angle)
		{
			this.angle = Math.max(-.3,Math.min(.3,angle));
		}
		
		public void pidWrite(double output) {
			arcadeDrive(-output, angle);
		}
	}


}
