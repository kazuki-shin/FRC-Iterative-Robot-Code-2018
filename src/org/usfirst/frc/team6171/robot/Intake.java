package org.usfirst.frc.team6171.robot;

import edu.wpi.first.wpilibj.VictorSP;

public class Intake {
	
	public VictorSP intake;
	
	public Intake(){
		intake = new VictorSP(4);
	}
	
	public void On(){
		intake.set(1);
	}
	
	public void Off(){
		intake.set(0);
	}
}
