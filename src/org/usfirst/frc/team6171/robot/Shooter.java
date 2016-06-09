package org.usfirst.frc.team6171.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter {
	//public CANTalon talonLeft, talonRight;
	CANTalon leftTalon, rightTalon;
	Compressor comp;
    DoubleSolenoid ds;
    boolean out;
	
    //set speeds of intake and shooting
	final static int SHOOT_DESIRED_RPM_COMP = 3000;
    final static int INTAKE_DESIRED_RPM_COMP = 2500;
    final static int SHOOT_DESIRED_RPM_KID = 2000;
    final static int INTAKE_DESIRED_RPM_KID = 1500;
    private int shootSpeed,intakeSpeed;
    
	public static final double Kp = .1;
    public static final double Ki = .001;
    public static final double Kd = .01;

	public Shooter(){
		leftTalon = new CANTalon(RobotMap.KLeftTalon);
        rightTalon = new CANTalon(RobotMap.KRightTalon);
		shootSpeed = SHOOT_DESIRED_RPM_COMP;
		intakeSpeed = INTAKE_DESIRED_RPM_COMP;
//      Calibration for right talon motor
		rightTalon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
        rightTalon.reverseSensor(false);
        rightTalon.configNominalOutputVoltage(0.0, 0.0);
        rightTalon.configPeakOutputVoltage(12.0, -12.0);
        rightTalon.setProfile(0);
        rightTalon.setF(.02445367);
        rightTalon.setP(.02);
        rightTalon.setI(.0001);
        rightTalon.setD(.1);
        
//        Calibration for left talon motor
        leftTalon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
        leftTalon.reverseSensor(true);
        leftTalon.configNominalOutputVoltage(0.0, 0.0);
        leftTalon.configPeakOutputVoltage(12.0, -12.0);
        leftTalon.setProfile(0);
        leftTalon.setF(.02445367); 
        leftTalon.setP(.02);
        leftTalon.setI(.0001);
        leftTalon.setD(.1);
        
        comp = new Compressor(1);
        comp.setClosedLoopControl(true);
        ds = new DoubleSolenoid(0, 1);
        out = false;
        
        switch((String)SmartMaker.RPMSpeed.getSelected())
        {
        case "Comp Speed": shootSpeed=SHOOT_DESIRED_RPM_COMP;intakeSpeed=INTAKE_DESIRED_RPM_COMP;break;
        case "Kid Speed":shootSpeed=SHOOT_DESIRED_RPM_KID;intakeSpeed=INTAKE_DESIRED_RPM_KID;break;
        }
	}
	public void checkRPM()
	{
		 switch((String)SmartMaker.RPMSpeed.getSelected())
	        {
	        case "Comp Speed": shootSpeed=SHOOT_DESIRED_RPM_COMP;intakeSpeed=INTAKE_DESIRED_RPM_COMP;break;
	        case "Kid Speed":shootSpeed=SHOOT_DESIRED_RPM_KID;intakeSpeed=INTAKE_DESIRED_RPM_KID;break;
	        }
	}
	//Spins flywheels to shoot
	public void spinUp(){
		leftTalon.changeControlMode(TalonControlMode.Speed);
		rightTalon.changeControlMode(TalonControlMode.Speed);
		leftTalon.set(shootSpeed);
		rightTalon.set(shootSpeed);
	}
	
	//Spins flywheels to intake
	public void intakeSpin(){
		leftTalon.changeControlMode(TalonControlMode.Speed);
		rightTalon.changeControlMode(TalonControlMode.Speed);
		leftTalon.set(-intakeSpeed);
		rightTalon.set(-intakeSpeed);	
	}
	
	//Stops any current flywheel movement
	public void stop(){
		leftTalon.changeControlMode(TalonControlMode.PercentVbus);
		rightTalon.changeControlMode(TalonControlMode.PercentVbus);
		leftTalon.set(0);
		rightTalon.set(0);
	}
	
	//Pushes solenoid piston out
	public void shoot(){
		ds.set(Value.kForward);
		out = true;
	}
	
	//Retracts solenoid piston
	public void retract(){
		ds.set(Value.kReverse);
		out = false;
	}
	
	public void log(){
		SmartDashboard.putNumber("Left Talon", leftTalon.getSpeed());
		SmartDashboard.putNumber("Right Talon", rightTalon.getSpeed());
		SmartDashboard.putBoolean("Piston", out);
	}
}