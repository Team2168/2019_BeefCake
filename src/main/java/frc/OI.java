/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc;

import frc.commands.vacuumClimberLift.DriveVacuumClimberLiftWithConstant;
import frc.commands.vacuumClimberPump.DriveVacuumClimberPumpWithConstant;
import frc.utils.F310;
import frc.utils.LinearInterpolator;



/**
 * Add your docs here.
 */
public class OI {

    public static OI _instance;

    public F310 driverJoystick;
    private F310 operatorJoystick;

    private LinearInterpolator gunStyleInterpolator;
	private double[][] gunStyleArray = { { -1.0, -1.0
			}, { -.15, 0.0
			}, { .15, 0.0
			}, { 1.0, 1.0
			}
	};


    private OI()
    {
        driverJoystick = new F310(RobotMap.DRIVER_JOYSTICK_PORT);
        operatorJoystick = new F310(RobotMap.OPERATOR_JOYSTICK_PORT);

        gunStyleInterpolator = new LinearInterpolator(gunStyleArray);

        operatorJoystick.ButtonA().whenPressed(new DriveVacuumClimberLiftWithConstant(0.5));
        operatorJoystick.ButtonA().whenReleased(new DriveVacuumClimberLiftWithConstant(0.0));
        operatorJoystick.ButtonB().whenPressed(new DriveVacuumClimberPumpWithConstant(1.0));
        operatorJoystick.ButtonB().whenReleased(new DriveVacuumClimberPumpWithConstant(0.0));

    }

    /**
     * Returns an instance of the Operator Interface.
     * 
     * @return is the current OI object
     */
    public static OI getInstance()
    {
        if (_instance == null)
            _instance = new OI();

        return _instance;
    }

    public double getPivotJoystickValue()
    {
        return operatorJoystick.getLeftStickRaw_Y();
    }

    public double getCargoIntakeJoystickValue()
    {
        return operatorJoystick.getRightStickRaw_Y();
    }

    public double getHatchIntakeJoystickValue()
    {
        return -operatorJoystick.getLeftTriggerAxisRaw() + operatorJoystick.getRightTriggerAxisRaw();
    }

    public double getVacuumClimberLiftJoystickValue()
    {
        return operatorJoystick.getRightStickRaw_Y();
    }

    /*************************************************************************
     * Drivetrain *
     *************************************************************************/

    public double getGunStyleXValue()
    {
        return driverJoystick.getLeftStickRaw_X();
    }

    public double getGunStyleYValue()
    {

        return gunStyleInterpolator.interpolate(driverJoystick.getLeftStickRaw_Y());
    }

    /**
     * Method that sets that Left side of the drive train so that it drives with
     * LeftStick Y
     * 
     * @author Krystina
     */
    public double getDriveTrainLeftJoystick()
    {
        return driverJoystick.getLeftStickRaw_Y();
    }

    /**
     * Method that sets that Right side of the drive train so that it drives with
     * RightStick Y
     * 
     * @author Krystina
     */
    public double getDriveTrainRightJoystick()
    {
        return driverJoystick.getRightStickRaw_Y();
    }


}
