package org.impact2585.lib2585;

import edu.wpi.first.wpilibj.RobotDrive;

/**
 * This class contains drivetrain code for the robot
 */
public class Drivetrain implements Destroyable {

	private double deadzone;
	private double ramp;
	private double primaryRotationExponent;
	private double secondaryRotationExponent;
	private RobotDrive drivetrain;
	private double currentRampForward;
	private boolean inverted;
	private Toggler invertToggler;
	private Toggler rotationExponentToggler;
	private boolean usePrimaryRotationExponent = true;


	/** 
	 * @param inputDeadzone joystick deadzone 
	 * @param ramping ramping acceleration constant 
	 * @param primaryEx primary rotation exponent
	 * @param secondEx secondary rotation exponent
	 * @param drivebase robot drive object
	 */
	public Drivetrain(double inputDeadzone, double ramping, double primaryEx, double secondEx, RobotDrive drivebase){
		deadzone = inputDeadzone;
		ramp = ramping;
		primaryRotationExponent = primaryEx;
		secondaryRotationExponent = secondEx;
		drivetrain = drivebase;
		invertToggler = new Toggler(inverted);
		rotationExponentToggler = new Toggler(usePrimaryRotationExponent);
	}

	/**
	 * @param desiredRampForward movement input
	 * @param rotationValue rotation input
	 * @param invertInput direction changing input
	 * @param toggleRotationExponentInput rotation exponent changing input
	 */
	public void arcadeControl(double desiredRampForward, double rotationValue, boolean invertInput, boolean toggleRotationExponentInput){

		//inverts if the input tells it to and the previous invert command was false
		inverted = invertToggler.toggle(invertInput);

		//toggles the rotation exponent if the input tells it to and the previous invert command was false
		usePrimaryRotationExponent = rotationExponentToggler.toggle(toggleRotationExponentInput);

		if(desiredRampForward < deadzone && desiredRampForward > -deadzone)
			desiredRampForward = 0;
		if(rotationValue < deadzone && rotationValue > -deadzone)
			rotationValue = 0;

		// adjusts sensitivity of the turns 
		rotationValue = Math.signum(rotationValue) * Math.abs(Math.pow(rotationValue, usePrimaryRotationExponent ? primaryRotationExponent : secondaryRotationExponent));

		if(desiredRampForward != 0) {


			//inverts the desiredRampForward if the wheel system should be inverted 
			if(inverted) {
				desiredRampForward *= -1;
			}

			//ramps up or down depending on the difference between the desired ramp and the current ramp multiplied by the RAMP constant
			if(currentRampForward < desiredRampForward) {
				double inc = desiredRampForward - currentRampForward;

				if(inc <= 0.01)
					currentRampForward = desiredRampForward;
				else
					currentRampForward += (inc*ramp);
			} else if (currentRampForward > desiredRampForward) {
				double decr = currentRampForward - desiredRampForward;

				if(decr > 0.01)
					currentRampForward -= (decr*ramp);
				else
					currentRampForward = desiredRampForward;
			}
		}else
			//sets currentRampForward immediately to 0 if the input is 0
			currentRampForward = 0;

		arcadeDrive(currentRampForward, rotationValue);

	}
	
	/**
	 * drives drivetrain without ramping
	 * @param moveValue movement output
	 * @param rotationValue rotation output
	 */
	public void arcadeDrive(double moveValue, double rotationValue){
		drivetrain.drive(moveValue, rotationValue);
	}

	/**
	 * @return the invertToggler
	 */
	public Toggler getInvertToggler() {
		return invertToggler;
	}

	/**
	 * @param invertToggler the invertToggler to set
	 */
	public void setInvertToggler(Toggler invertToggler) {
		this.invertToggler = invertToggler;
	}

	/**
	 * @return the rotationExponentToggler
	 */
	public Toggler getRotationExponentToggler() {
		return rotationExponentToggler;
	}

	/**
	 * @param rotationExponentToggler the rotationExponentToggler to set
	 */
	public void setRotationExponentToggler(Toggler rotationExponentToggler) {
		this.rotationExponentToggler = rotationExponentToggler;
	}

	/**
	 * @return the deadzone
	 */
	public double getDeadzone() {
		return deadzone;
	}

	/**
	 * @param deadzone the deadzone to set
	 */
	public void setDeadzone(double deadzone) {
		this.deadzone = deadzone;
	}

	/**
	 * @return the ramp
	 */
	public double getRamp() {
		return ramp;
	}

	/**
	 * @param ramp the ramp to set
	 */
	public void setRamp(double ramp) {
		this.ramp = ramp;
	}

	/**
	 * @return the primaryRotationExponent
	 */
	public double getPrimaryRotationExponent() {
		return primaryRotationExponent;
	}

	/**
	 * @param primaryRotationExponent the primaryRotationExponent to set
	 */
	public void setPrimaryRotationExponent(double primaryRotationExponent) {
		this.primaryRotationExponent = primaryRotationExponent;
	}

	/**
	 * @return the secondaryRotationExponent
	 */
	public double getSecondaryRotationExponent() {
		return secondaryRotationExponent;
	}

	/**
	 * @param secondaryRotationExponent the secondaryRotationExponent to set
	 */
	public void setSecondaryRotationExponent(double secondaryRotationExponent) {
		this.secondaryRotationExponent = secondaryRotationExponent;
	}

	/**
	 * @return the currentRampForward
	 */
	public double getCurrentRampForward() {
		return currentRampForward;
	}

	/**
	 * @param currentRampForward the currentRampForward to set
	 */
	public void setCurrentRampForward(double currentRampForward) {
		this.currentRampForward = currentRampForward;
	}

	/* (non-Javadoc)
	 * @see org.impact2585.lib2585.Destroyable#destroy()
	 */
	@Override
	public void destroy() {
		drivetrain.free();
	}
}
