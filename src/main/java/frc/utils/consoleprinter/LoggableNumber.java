/**
 * 
 */
package frc.utils.consoleprinter;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author James
 *
 */
public class LoggableNumber implements Loggable {
	private Supplier<Double> value;

	public LoggableNumber(Supplier<Double> value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see frc.utils.consoleprinter.Loggable#put(java.lang.String)
	 */
	@Override
	public void put(String key) {
		SmartDashboard.putNumber(key, value.get());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see frc.utils.consoleprinter.Loggable#valueToString()
	 */
	@Override
	public String valueToString() {
		return value.get().toString();
	}
}
