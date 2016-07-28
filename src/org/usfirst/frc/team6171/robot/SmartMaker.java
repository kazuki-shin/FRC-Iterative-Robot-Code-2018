package org.usfirst.frc.team6171.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class SmartMaker extends SmartDashboard{

	public static SendableChooser driveSpeed,RPMSpeed,AutonSelector;
	
	public static CameraServer server;
	final static String compSpeed = "Comp Speed";
	final static String kidSpeed = "Kid Speed";
	final static String maxSpeed = "Max Speed";
	final static String noSpeed = "No Speed";
	
	//drive speed chooser
	public static void driveSpeedChooser(){
		driveSpeed = new SendableChooser();
		driveSpeed.addObject("Comp Speed", compSpeed);
		driveSpeed.addObject("Kid Speed", kidSpeed);
		driveSpeed.addObject("Max Speed", maxSpeed);
		driveSpeed.addObject("No Speed", noSpeed);
		putData("Speed Chooser",driveSpeed);
	}
	//shows the view from specified camera
	public static void startCamera(String cameraKey){
		try{
	    	server = CameraServer.getInstance();
	        server.setQuality(50);
	        //the camera name (ex "cam0") can be found through the roborio web interface
	        server.startAutomaticCapture(cameraKey);
	    	}
	    	catch(Exception e){}
		
	}
	public static void RPMSpeedChooser(){
		RPMSpeed = new SendableChooser();
		RPMSpeed.addObject("Comp Speed", compSpeed);
		RPMSpeed.addObject("Kid Speed", kidSpeed);
		putData("RPM Speed Chooser",RPMSpeed);
	}
	public static void AutonSelectorChooser()
	{
		AutonSelector = new SendableChooser();
		AutonSelector.addObject("LowBar", "LowBar");
		AutonSelector.addObject("RockWall", "RockWall");
		AutonSelector.addObject("RockyTerrain", "RockyTerrain");
		AutonSelector.addObject("Moat", "Moat");
		AutonSelector.addObject("Rampart", "Rampart");
		putData("Auton Chooser",AutonSelector);
	}
}
