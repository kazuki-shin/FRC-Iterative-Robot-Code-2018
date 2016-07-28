
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
	public int step,autoDistance;
	public boolean distDone;
	public Intake intake;
	public static double sensitivity,slowMode, yVal, calculatedAngle;
	public boolean isShooting, isIntaking, pistonOut, mYPushed, isArcade, dBPushed, dXPushed, dYPushed,dAPushed;
	public int rumbleCount;
	public boolean turningDone;
	
	
	final int BIG_OBSTACLE_DISTANCE = 175;
	final int ROUGH_TERRAIN_DISTANCE = 125;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	SmartMaker.driveSpeedChooser();
		SmartMaker.RPMSpeedChooser();
		SmartMaker.AutonSelectorChooser();
		intake = new Intake();
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
    	turningDone = false;
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
    public String choice;
    public void autonomousInit() {
    	winch.enable();
    	winch.setAngle(25);
    	time.reset();
    	time.start();
    	drive.resetEncoders();
    	drive.setDistanceSetpoint(-175);
    	drive.setOutputRange(-.7, .7);
    	drive.pidEnable();
    	Robot.ahrs.reset();
    	step = 1;
    	distDone = false;
    	turningDone = false;
    	choice = (String)SmartMaker.AutonSelector.getSelected();
    	switch(choice){
		case "Moat":
		case "RockWall":
			//winch.setAngle(15);
			drive.setDistanceSetpoint(BIG_OBSTACLE_DISTANCE);
			autoDistance = BIG_OBSTACLE_DISTANCE;
			drive.setOutputRange(-.9,.9);
			drive.pidEnable();
			shooter.shoot();
			break;
		case "RoughTerrain":
			//winch.setAngle(15);
			drive.setDistanceSetpoint(ROUGH_TERRAIN_DISTANCE);
			autoDistance = ROUGH_TERRAIN_DISTANCE;
			drive.setOutputRange(-.8,.8);
			drive.pidEnable();
			break;
		case "ramparts":
			//winch.setAngle(15);
			drive.setDistanceSetpoint(-BIG_OBSTACLE_DISTANCE);
			autoDistance = -BIG_OBSTACLE_DISTANCE;
			drive.setOutputRange(-.9,.9);
			drive.pidEnable();
			break;
		default:break;	
		}
    	
    	
    	
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	winch.controlWinch(-ahrs.getRoll());
    	switch((String)SmartMaker.AutonSelector.getSelected())
    	{
    	case "LowBar":
    	if(step==1){
			System.out.println("Step 1 "+drive.leftEnc.getDistance());
			drive.driveDistanceForwards();
			if(Math.abs(drive.leftEnc.getDistance())>175)
			{
				if(time.get()>5)
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
				//if(time.get()>8)
					//turningDone = true;
				//if(turningDone){
					step++;
					drive.resetEncoders();
					ahrs.reset();
					drive.setDistanceSetpoint(-10);
					drive.pidEnable();
					turningDone = false;
				//}
			}
		}
		
		if(step==3)
		{
			System.out.println("Step 3"); 
			drive.driveDistanceForwards();
			if(Math.abs(drive.leftEnc.getDistance())>10)
			{
				ahrs.reset();
				drive.setAngleSetpoint(-100);
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
				drive.setDistanceSetpoint(35);
				drive.pidEnable();
				//winch.setAngle(35);
			}
		}
		if(step ==5){
			drive.driveDistanceForwards();
			if(Math.abs(drive.leftEnc.getDistance())>35){
				step++;
				winch.setAngle(45);
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
    	break;
    	
    	default:
    		winch.controlWinch(-ahrs.getRoll());
    		if(step==1){
    			System.out.println("Step 1");
    			drive.driveDistanceForwards();
    			if(Math.abs(drive.leftEnc.getDistance())>Math.abs(autoDistance))
    			{
    				if(((String)SmartMaker.AutonSelector.getSelected()).equals("Ramparts"))
    					drive.setAngleSetpoint(-140);
    				else
    					drive.setAngleSetpoint(30);
    				ahrs.reset();
    				step++;
    				drive.pidDisable();
    				drive.setTurnDone(false);
    			}
    		}
    		break;
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
    	dAPushed = dBPushed = dAPushed = dYPushed = false;
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
    	if(oi.dB.get()){
    		dBPushed = true;
    	}
    	
    	if(dBPushed && !oi.dB.get())
    	{
    		isArcade = !isArcade;
    		dBPushed = false;
    		if(isArcade)rumbleCount = 10;
    		else rumbleCount = 50;
    		oi.setDriveVibrate(true);
    	}	
    	
    	
    	if(oi.dA.get()){
    		dAPushed = true;
    	}
    	
    	if(dAPushed && !oi.dA.get())
    	{
    		dAPushed = false;
    		intake.backward();
    	}	
    	
    	
    	if(oi.dY.get()){
    		dYPushed = true;
    	}
    	
    	if(dYPushed && !oi.dY.get())
    	{
    		intake.forward();
    		dYPushed = false;
    	}	
    	
    	if(oi.dX.get()){
    		dXPushed = true;
    	}
    	
    	if(dXPushed && !oi.dX.get())
    	{
    		dXPushed = false;
    		intake.off();
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
    		intake.backward();
    	}
    	else
    	{
    		shooter.stop();
    		intake.off();
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
