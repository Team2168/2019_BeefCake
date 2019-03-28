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
import frc.commands.hatchIntakeWheels.DriveHatchIntakeWheelsWithJoysticks;
import frc.utils.consoleprinter.ConsolePrinter;
import frc.Robot;

/**
 * Add your docs here.
 */
public class HatchIntakeWheels extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private VictorSPX hatchIntakeWheelsMotor;

  private volatile double hatchIntakeWheelsMotorVoltage;


  private static HatchIntakeWheels _instance;

  private HatchIntakeWheels()
  {
    hatchIntakeWheelsMotor = new VictorSPX(RobotMap.HATCH_INTAKE_WHEELS_PDP);

        
    ConsolePrinter.putNumber("Hatch intake motor voltage", () -> {return hatchIntakeWheelsMotorVoltage;}, true, false);
    ConsolePrinter.putNumber("Hatch intake motor current", () -> {return Robot.pdp.getChannelCurrent(RobotMap.HATCH_INTAKE_WHEELS_PDP);}, true, false);
  }

  /**
   * Singleton constructor of the cargo intake wheels
   * 
   */

  public static HatchIntakeWheels getInstance()
  {
    if (_instance == null)
      _instance = new HatchIntakeWheels();
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
  public void driveHatchIntakeWheelsMotor(double speed)
  {
    if (RobotMap.HATCH_INTAKE_WHEELS_REVERSE)
      speed = -speed;
    hatchIntakeWheelsMotor.set(ControlMode.PercentOutput, speed);
   hatchIntakeWheelsMotorVoltage = Robot.pdp.getBatteryVoltage() * speed; 
  }

  /**
   * Returns the last commanded voltage of hatch intake motor
   * 
   * @return Double in volts between 0 and 12
   */
  public double getHatchIntakeVoltage()
  {
    return hatchIntakeWheelsMotorVoltage;
  }


  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new DriveHatchIntakeWheelsWithJoysticks());
  }
}
