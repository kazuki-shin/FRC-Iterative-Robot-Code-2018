
package org.usfirst.frc.team6171.robot;

import java.util.ArrayList;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	public OI oi;
	public DriveTrain drive;
	public Shooter shooter;
	public Winch winch;
	public static AHRS ahrs;
	public NetworkTable network;
	public PowerDistributionPanel pdp;
	public Timer time;
	public int step;
	public boolean distDone;
	
	public static double sensitivity,slowMode, yVal, calculatedAngle;
	public boolean isShooting, isIntaking, pistonOut, mYPushed, isArcade, dAPushed;
	public int rumbleCount;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	SmartMaker.driveSpeedChooser();
		SmartMaker.RPMSpeedChooser();
    	oi = new OI();
		drive =  new DriveTrain();
    	shooter = new Shooter();
    	winch = new Winch();
    	pdp = new PowerDistributionPanel();
		SmartMaker.startCamera("cam1");
    	try {
	          ahrs = new AHRS(SPI.Port.kMXP); 
	      } catch (RuntimeException ex ) {
	          DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
	      }
    	network = NetworkTable.getTable("SmartDashboard");
    	
    	time = new Timer();
    	step = 0;
    }
    
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomousInit() {
    	winch.enable();
    	winch.setAngle(25);
    	time.reset();
    	time.start();
    	drive.resetEncoders();
    	drive.setDistanceSetpoint(-140);
    	drive.setOutputRange(-.7, .7);
    	drive.pidEnable();
    	Robot.ahrs.reset();
    	step = 1;
    	distDone = false;
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	winch.controlWinch(-ahrs.getRoll());
    	if(step==1){
			System.out.println("Step 1 "+drive.leftEnc.getDistance());
			drive.driveDistanceForwards();
			if(Math.abs(drive.leftEnc.getDistance())>140)
			{
				if(time.get()>6)
					distDone = true;
				if(distDone){
					ahrs.reset();
					drive.setAngleSetpoint(17);
					step++;
					drive.pidDisable();
					drive.setTurnDone(false);
				}
				
			}
		}
    	if(step==2){
			System.out.println("Step 2");
			drive.turnToAngle();
			//System.out.println(ahrs.getYaw());
			//System.out.println(driveTrain.pid.isEnabled());
			if(drive.getTurnDone())
			{
				step++;
				drive.resetEncoders();
				ahrs.reset();
				drive.setDistanceSetpoint(-10);
				drive.pidEnable();
				
			}
		}
		
		if(step==3)
		{
			System.out.println("Step 3"); 
			drive.driveDistanceForwards();
			if(Math.abs(drive.leftEnc.getDistance())>10)
			{
				ahrs.reset();
				drive.setAngleSetpoint(-132);
				step++;
				drive.pidDisable();
				drive.setTurnDone(false);
			}
		}
		if(step==4){
			System.out.println(drive.pid.isEnabled());
			System.out.println(ahrs.getYaw());
			System.out.println("Step 4");
			drive.turnToAngle();
			if(drive.getTurnDone())
			{
				step++;
				ahrs.reset();
				drive.resetEncoders();
				drive.setDistanceSetpoint(25);
				drive.pidEnable();
				//winch.setAngle(35);
			}
		}
		if(step ==5){
			drive.driveDistanceForwards();
			if(Math.abs(drive.leftEnc.getDistance())>20){
				step++;
				winch.setAngle(50);
			}
		}
		if(step == 6){
			shooter.spinUp();
			if(time.get()>14){
				shooter.shoot();
				step++;
			}
		}
		if(step == 7){
			if(time.get()>16){
				shooter.stop();
				shooter.retract();
			}
			
		}
    }

    public void teleopInit(){
    	
    	//winch
    	drive.pidDisable();
    	winch.setAngle(-ahrs.getRoll());
    	winch.enable();
    	shooter.checkRPM();
    	//fly wheels and piston
    	shooter.stop();
    	//shooter.checkRPM();
    	isShooting = isIntaking = mYPushed = pistonOut = false;
    	//drive train
    	sensitivity = 1;
    	slowMode = 1;
    	isArcade = true;
    	dAPushed = false;
    	rumbleCount = 0;
    	//selects the drive speed
    	String driveSpeedSelected = (String )SmartMaker.driveSpeed.getSelected();
    	switch(driveSpeedSelected){
    		case "Comp Speed": sensitivity = 0.7; break;
    		case "Kid Speed": sensitivity = .4; break;
    		case "Max Speed": sensitivity = 1; break;
    		case "No Speed": sensitivity = 0; 
    		default: sensitivity = 0; 
    				 System.out.println("No drive speed selected");
    				 
    	}
    	
    }
    
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	
    	try{
    		double[] points = network.getNumberArray("BFR_COORDINATES");
    		yVal = (points[1]+points[3]+points[5]+points[7])/4;
//    		calculatedAngle = .1544349*yVal +  20.1191;
//    		if(yVal<135)
//    		{
//    			calculatedAngle = 41;
//    		}
    		//calculatedAngle = .1182129115*yVal +  24.26737922189;
    		calculatedAngle = .1000664615*yVal +  25.31444167075;
    		
    	}
    	catch(Exception e){
    		//System.out.println("No camera data");
    	}
    	System.out.println(calculatedAngle);
    	shooter.checkRPM();
    	if(oi.dA.get()){
    		dAPushed = true;
    	}
    	
    	if(dAPushed && !oi.dA.get())
    	{
    		isArcade = !isArcade;
    		dAPushed = false;
    		if(isArcade)rumbleCount = 10;
    		else rumbleCount = 50;
    		oi.setDriveVibrate(true);
    	}	
    	
    	if(oi.dLB.get() && oi.dRB.get())
		{
			//driveTrain.drive.setMaxOutput(Math.abs(oi.getSliderValue()-1));
			slowMode = .7;
		}
		else if(oi.getDriveRightTrigger()>.5 && oi.getDriveLeftTrigger()>.5)
		{
			drive.setMaxOutput(1);
			slowMode = 1;
		}
		else
		{
			drive.setMaxOutput(.7);
			slowMode = 1;
		}
    	
    	
    	if(isArcade)
    	{
    		drive.arcadeDrive(oi.getDriveLeftY()*sensitivity*slowMode, oi.getDriveRightX()*sensitivity*slowMode);
    		SmartDashboard.putString("Type", "arcade");
    	}
    	else{
    		drive.tankDrive(oi.getDriveLeftY()*sensitivity*slowMode, oi.getDriveRightY()*sensitivity*slowMode);
    		SmartDashboard.putString("Type", "tank");
    	}
    	if(rumbleCount>0)rumbleCount--;
    	if(rumbleCount==0)oi.setDriveVibrate(false);
    	
    	//fly wheels in
		if(oi.mX.get())
    	{
    		isShooting = false;
    		isIntaking = true;
    	}
		//fly wheels out
    	if(oi.mB.get())
    	{
    		isShooting = true;
    		isIntaking = false;
    	}
    	//stop fly wheels
    	if(oi.mA.get())
    	{
    		isShooting = false;
    		isIntaking = false;
    		winch.enable();
    	}
    	//piston in/out
    	if(mYPushed && !oi.mY.get())
    	{
    		mYPushed = false;
    		pistonOut = !pistonOut;
    		if(pistonOut)
    			shooter.shoot();
    		else
    			shooter.retract();
    		oi.setManipulatorVibrate(pistonOut);
    		
    	}
    	if(oi.mY.get())
    	{
    		mYPushed=true;
    	}
    	if(isShooting)
    	{
    		shooter.spinUp();
    		winch.setWinchTolerance(1);
    		//winch.disable();
    	}
    	else if(isIntaking)
    	{
    		shooter.intakeSpin();
    	}
    	else
    	{
    		shooter.stop();
    		//winch.enable();
    		winch.setWinchTolerance(.7);
    	}
    	
    	//control winch movement only when LB is held down
    	if(oi.mLB.get())
    		winch.controlWinch(oi.getManipulatorLeftY()*-5, -ahrs.getRoll());
    	else
    		winch.controlWinch(-ahrs.getRoll());
    	
    	if(oi.getManipulatorPOV()==0){
    		winch.setAngle(30);
    	}
    	if(oi.getManipulatorPOV()==90){
    		winch.setAngle(0);
    	}
    	if(oi.getManipulatorPOV()==180){
    		winch.setAngle(-15);
    	}
    	if(oi.getManipulatorPOV()==270){
    		winch.setAngle(47);
    	}
    	
    	//selects the drive speed
    	String driveSpeedSelected = (String )SmartMaker.driveSpeed.getSelected();
    	switch(driveSpeedSelected){
    		case "Comp Speed": sensitivity = 0.9; break;
    		case "Kid Speed": sensitivity = .4; break;
    		case "Max Speed": sensitivity = 1; break;
    		case "No Speed": sensitivity = 0;
    		default: sensitivity = 0; 
    				 System.out.println("No drive speed selected");
    	}
 }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
