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
import frc.commands.pivot.DrivePivotWithJoysticks;
import frc.utils.consoleprinter.ConsolePrinter;
import frc.Robot;

/**
 * Add your docs here.
 */
public class Pivot extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private VictorSPX pivotMotor;

  private volatile double pivotVoltage;

  private static Pivot _instance;


  private Pivot()
  {
    pivotMotor = new VictorSPX(RobotMap.PLUNGER_PIVOT_MOTOR_PDP);

        
    ConsolePrinter.putNumber("Pivot motor voltage", () -> {return pivotVoltage;}, true, false);
    ConsolePrinter.putNumber("Cargo Intake right motor current", () -> {return Robot.pdp.getChannelCurrent(RobotMap.PLUNGER_PIVOT_MOTOR_PDP);}, true, false);
  }

  /**
   * Singleton constructor of the plunger arm pivot
   * 
   */

  public static Pivot getInstance()
  {
    if (_instance == null)
      _instance = new Pivot();
    return _instance;
  }

  /**
   * Calls plunger arm pivot motor and creates a local variable "speed" Refers to
   * boolean in Robot map and if true, speed = - speed Uses set() command to
   * assign the new speed to plunger arm pivot motor.
   * 
   * 
   * @param double speed between -1 and 1 positive values rotate pivot to the
   *        front of the robot, negative values rotate pivot to the back of the
   *        robot, 0 is stationary
   */
  public void drivePlungerArmPivotMotor(double speed)
  {
    if (RobotMap.PLUNGER_ARM_PIVOT_REVERSE)
      speed = -speed;
    pivotMotor.set(ControlMode.PercentOutput, speed);
    pivotVoltage = Robot.pdp.getBatteryVoltage() * speed; // not currently used
  }

  /**
   * Returns the last commanded voltage of pivot motor
   * 
   * @return Double in volts between 0 and 12
   */
  public double getPivotVoltage()
  {
    return pivotVoltage;
  }


  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new DrivePivotWithJoysticks());
  }
}
