/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.subsystems;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.RobotMap;
import frc.commands.vacuumClimberPump.DriveVacuumClimberPumpWithJoystick;

/**
 * Add your docs here.
 */
public class VacuumClimberPump extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private Victor climberPumpMotor;

  private static VacuumClimberPump _instance;

  private VacuumClimberPump()
  {
    climberPumpMotor = new Victor(RobotMap.CLIMBER_PUMP_MOTOR_PDP);
  }

  /**
   * Singleton constructor of the vacuum climber pmmp
   * 
   */

  public static VacuumClimberPump getInstance()
  {
    if (_instance == null)
      _instance = new VacuumClimberPump();
    return _instance;
  }

  public void driveClimberPumpMotor(double speed)
  {
    if(RobotMap.CLIMBER_PUMP_MOTOR_REVERSE)
    {
      speed = -speed;
    }
    climberPumpMotor.set(speed);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new DriveVacuumClimberPumpWithJoystick());
  }
}
