/**
 * 
 */
package frc.utils.consoleprinter;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author James
 *
 */
public class LoggableSendable implements Loggable {
	private Supplier<Sendable> value;

	public LoggableSendable(Supplier<Sendable> value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see frc.utils.consoleprinter.Loggable#put(java.lang.String)
	 */
	@Override
	public void put(String key) {
		SmartDashboard.putData(key, value.get());
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
