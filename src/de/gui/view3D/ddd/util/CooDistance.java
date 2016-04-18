package de.gui.view3D.ddd.util;

/**
 * class for describing a distance
 *
 * @author ddd
 */
public class CooDistance
{
	public static final double FACTOR_MILLI_METRES = 1;
	public static final double FACTOR_CENTI_METRES = FACTOR_MILLI_METRES * 10;
	public static final double FACTOR_DEZI_METRES = FACTOR_CENTI_METRES * 10;
	public static final double FACTOR_METRES = FACTOR_DEZI_METRES * 10;
	/**
	 * @param distance the distance in mm the new object should show
	 * @return
	 */
	public static CooDistance ofMilliMetres(long distance)
	{
		return new CooDistance((long)(distance * FACTOR_MILLI_METRES));
	}

	/**
	 * @param distance the distance in cm the new object should show
	 * @return
	 */
	public static CooDistance ofCentiMetres(double distance)
	{
		return new CooDistance((long)(distance * FACTOR_CENTI_METRES));
	}

	/**
	 * @param distance the distance in dm the new object should show
	 * @return
	 */
	public static CooDistance ofDeziMetres(double distance)
	{
		return new CooDistance((long)(distance * FACTOR_DEZI_METRES));
	}

	/**
	 * @param distance the distance in m the new object should show
	 * @return
	 */
	public static CooDistance ofMetres(double distance)
	{
		return new CooDistance((long)(distance * FACTOR_METRES));
	}

	/** the distance shown in mm */
	private long distance;

	protected CooDistance(long distance)
	{
		this.distance = distance;
	}

	/**
	 * @return the mm distance shown by this object
	 */
	public long toMilliMetres()
	{
		return (long)(distance / FACTOR_MILLI_METRES);
	}

	/**
	 * @return the cm distance shown by this object
	 */
	public double toCentiMetres()
	{
		return distance / FACTOR_CENTI_METRES;
	}

	/**
	 * @return the dm distance shown by this object
	 */
	public double toDeziMetres()
	{
		return distance / FACTOR_DEZI_METRES;
	}

	/**
	 * @return the m distance shown by this object
	 */
	public double toMetres()
	{
		return distance / FACTOR_METRES;
	}
}
