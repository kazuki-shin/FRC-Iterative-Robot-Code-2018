
package org.usfirst.frc.team6171.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
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
	
	public VictorSP leftFront, leftRear, rightFront, rightRear;
	//public RobotDrive drive;
	public OI oi;
	public DriveTrain drive;
	public Shooter shooter;
	public Winch winch;
	public AHRS ahrs;
	
	public static double sensitivity,slowMode;
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
		SmartMaker.startCamera("cam1");
    	try {
	          ahrs = new AHRS(SPI.Port.kMXP); 
	      } catch (RuntimeException ex ) {
	          DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
	      }
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

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	
    }

    public void teleopInit(){
    	
    	//winch
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
    		case "Comp Speed": sensitivity = 0.7; break;
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
