/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.RobotMap;
import frc.commands.cargoIntake.DriveCargoIntakeWithJoysticks;
import frc.utils.consoleprinter.ConsolePrinter;
import frc.Robot;

/**
 * Add your docs here.
 */
public class CargoIntakeWheels extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private VictorSPX cargoIntakeMotorLeft;
  private VictorSPX cargoIntakeMotorRight;

  private volatile double cargoIntakeMotorLeftVoltage;
  private volatile double cargoIntakeMotorRightVoltage;

  private static CargoIntakeWheels _instance;



  private CargoIntakeWheels()
  {
    cargoIntakeMotorLeft = new VictorSPX(RobotMap.CARGO_INTAKE_WHEELS_LEFT_PDP);
    cargoIntakeMotorRight = new VictorSPX(RobotMap.CARGO_INTAKE_WHEELS_RIGHT_PDP); 
    
    ConsolePrinter.putNumber("Cargo Intake right motor voltage", () -> {return cargoIntakeMotorLeftVoltage;}, true, false);
    ConsolePrinter.putNumber("Cargo Intake left motor voltage", () -> {return cargoIntakeMotorRightVoltage;}, true, false);
    ConsolePrinter.putNumber("Cargo Intake right motor current", () -> {return Robot.pdp.getChannelCurrent(RobotMap.CARGO_INTAKE_WHEELS_LEFT_PDP);}, true, false);
    ConsolePrinter.putNumber("Cargo Intake left motor current", () -> {return Robot.pdp.getChannelCurrent(RobotMap.CARGO_INTAKE_WHEELS_RIGHT_PDP);}, true, false);
  }

  /**
   * Singleton constructor of the cargo intake wheels
   * 
   */

  public static CargoIntakeWheels getInstance()
  {
    if (_instance == null)
      _instance = new CargoIntakeWheels();
    return _instance;
  }

   /**
   * Calls cargo intake left motor and creates a local variable "speed" Refers to
   * boolean in Robot map and if true, speed = - speed Uses set() command to
   * assign the new speed to cargo intake left motor.
   * 
   * 
   * @param double speed between -1 and 1 positive values move cargo out,
   *           negative values move cargo in, 0 is stationary
   */
  public void driveCargoIntakeMotorLeft(double speed)
  {
    if (RobotMap.CARGO_INTAKE_LEFT_REVERSE)
      speed = -speed;
    cargoIntakeMotorLeft.set(ControlMode.PercentOutput, speed);
    cargoIntakeMotorLeftVoltage = Robot.pdp.getBatteryVoltage() * speed;
  }

  /**
   * Calls cargo intake right motor and creates a local variable "speed" Refers to
   * boolean in Robot map and if true, speed = - speed Uses set() command to
   * assign the new speed to cargo intake right motor.
   * 
   * 
   * @param double speed between -1 and 1 positive values move cargo out,
   *           negative values move cargo in, 0 is stationary
   */
  public void driveCargoIntakeMotorRight(double speed)
  {
    if (RobotMap.CARGO_INTAKE_RIGHT_REVERSE)
      speed = -speed;
    cargoIntakeMotorRight.set(ControlMode.PercentOutput, speed);
    cargoIntakeMotorRightVoltage = Robot.pdp.getBatteryVoltage() * speed;
  }

  public void driveCargoIntakeMotors(double speed)
  {
    driveCargoIntakeMotorLeft(speed);
    driveCargoIntakeMotorRight(speed);

  }

    /**
   * Returns the last commanded voltage of left cargo intake motor
   * 
   * @return Double in volts between 0 and 12
   */
  public double getCargoIntakeLeftVoltage()
  {
    return cargoIntakeMotorLeftVoltage;
  }

      /**
   * Returns the last commanded voltage of right cargo intake motor
   * 
   * @return Double in volts between 0 and 12
   */
  public double getCargoIntakeRightVoltage()
  {
    return cargoIntakeMotorRightVoltage;
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new DriveCargoIntakeWithJoysticks());
  }
}
