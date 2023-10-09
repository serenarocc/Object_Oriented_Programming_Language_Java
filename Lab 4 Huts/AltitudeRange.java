package mountainhuts;

/**
 * Represent an altitude range
 * that is define by a min and an high value
 * where the max (upper) value is included.
 *
 */
public class AltitudeRange {
	private final int minValue;
	private final int maxValue;

	public final static AltitudeRange DEFAULT = new AltitudeRange("0-"+Integer.MAX_VALUE);
	/**
	 * Creates an altitude range by
	 * parsing a string in the format {@code "000-999"}
	 * 
	 * @param range the range as a string
	 */
	public AltitudeRange(String range) {
		String[] values = range.split("-");
		minValue = Integer.parseInt(values[0]);
		maxValue = Integer.parseInt(values[1]);
		if(minValue>maxValue) throw new IllegalArgumentException("Range should have min value lower than max value");
	}

	/**
	 * Checks whether the given altitude
	 * is comprised in the range
	 * 
	 * @param altitude the altitude to be checked
	 * @return {@code true} if the altitude is included in the range
	 */
	public boolean contains(int altitude) {
		return minValue < altitude && altitude <= maxValue;
	}

	@Override
	public String toString() {
		return minValue + "-" + (maxValue==Integer.MAX_VALUE?"INF":maxValue);
	}

}
