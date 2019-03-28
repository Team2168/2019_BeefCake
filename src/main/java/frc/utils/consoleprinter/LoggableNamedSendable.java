/**
 * 
 */
package frc.utils.consoleprinter;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.NamedSendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author James
 *
 */
public class LoggableNamedSendable implements Loggable {
	private Supplier<NamedSendable> value;

	public LoggableNamedSendable(Supplier<NamedSendable> value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see frc.utils.consoleprinter.Loggable#put(java.lang.String)
	 */
	@Override
	public void put(String key) {
		SmartDashboard.putData(value.get());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see frc.utils.consoleprinter.Loggable#valueToString()
	 */
	@Override
	public String valueToString() {
		return value.get().getName();
	}

}
