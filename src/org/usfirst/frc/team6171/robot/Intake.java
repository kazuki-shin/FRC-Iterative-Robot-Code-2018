package org.usfirst.frc.team6171.robot;

import edu.wpi.first.wpilibj.VictorSP;

public class Intake {
	
	public VictorSP intake;
	
	public Intake(){
		intake = new VictorSP(4);
	}
	
	public void forward(){
		intake.set(-1);
	}
	
	public void backward()
	{
		intake.set(1);
	}
	
	public void off(){
		intake.set(0);
	}
}
