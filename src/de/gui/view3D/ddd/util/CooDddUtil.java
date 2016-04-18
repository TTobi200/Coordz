package de.gui.view3D.ddd.util;

import java.util.EnumSet;
import java.util.Objects;

import de.gui.view3D.ddd.Coo3dAxis;
import de.gui.view3D.ddd.Coo3dAxis.CoordSystem;
import de.gui.view3D.ddd.Coo3dAxis.System;
import javafx.scene.transform.*;

public class CooDddUtil
{
	/** angle for quarter a rotation */
	public static final double ROTATE_QUARTER = 90d;
	/** angle for half a rotation */
	public static final double ROTATE_HALF = ROTATE_QUARTER * 2;

	/** The length-axis from left to right */
	public static final Coo3dAxis FIRST_AXIS = Coo3dAxis.X;
	/** the depth-axis from back to front */
	public static final Coo3dAxis SECOND_AXIS = Coo3dAxis.Z;
	/** the height-axis from ground to sky */
	public static final Coo3dAxis THIRD_AXIS = Coo3dAxis.Y;

	public static final CoordSystem DEFAULT_FX_SYSTEM = CoordSystem.X_Z_NY;

	/**
	 * enumset of all system in which all axis point in the positive direction after rotation.<br>
	 * all other systems have their first and second axis pointing in the positiv and the third
	 * pointing in the negative direction after rotation
	 */
	private static final EnumSet<System> INTUITIVE_ALL_POSITIVE = EnumSet.of(System.X_Y_Z, System.Y_Z_X, System.Z_X_Y);

	/**
	 * Crete a new transfor for changing the default fx-system to the given one.<br>
	 * When applying this tranformation the resulting system either has all three axis pointing
	 * in positive-direction (belonging to {@link #INTUITIVE_ALL_POSITIVE}) or only the third axis
	 * is pointing negative.
	 *
	 * @param system The system to create the transform to
	 * @return a transform for changing the default fx-system to the given one
	 */
	public static Transform getTransformFor(System system)
	{
		Transform t;

		switch(system)
		{
			case X_Y_Z:
			{
				Rotate r = new Rotate();
				r.setAxis(Rotate.X_AXIS);
				r.setAngle(-ROTATE_QUARTER);
				t = r;
				break;
			}
			case Y_X_Z:
			{
				t = getTransformFor(System.Y_Z_X);
				Rotate r = new Rotate();
				r.setAxis(Rotate.X_AXIS);
				r.setAngle(ROTATE_QUARTER);
				t = t.createConcatenation(r);
				break;
			}
			case Y_Z_X:
			{
				Rotate r = new Rotate();
				r.setAxis(Rotate.Z_AXIS);
				r.setAngle(ROTATE_QUARTER);
				t = r;
				break;
			}
			case Z_Y_X:
			{
				t = getTransformFor(System.Z_X_Y);
				Rotate r = new Rotate();
				r.setAxis(Rotate.X_AXIS);
				r.setAngle(ROTATE_QUARTER);
				t = t.createConcatenation(r);
				break;
			}
			case Z_X_Y:
			{
				Rotate r = new Rotate();
				r.setAxis(Rotate.Y_AXIS);
				r.setAngle(-ROTATE_QUARTER);
				t = r;
				r.setAxis(Rotate.X_AXIS);
				r.setAngle(180d);
				t = t.createConcatenation(r);
				break;
			}
			case X_Z_Y:
			default:
			{
				t = new Affine();
				break;
			}
		}

		return t;
	}

	/**
	 * @param coordSystem the system wished to transform to
	 * @return a transform for changing the default-fx-system to the wished one
	 */
	public static Transform getTransformFor(CoordSystem coordSystem)
	{
		Transform t = getTransformFor(coordSystem.getSystem());

		if(INTUITIVE_ALL_POSITIVE.contains(coordSystem.getSystem()))
		{
			t = t.createConcatenation(new Affine(
				(coordSystem.isFirstNegativ() ? -1 : 1), 0, 0, 0,
				0, (coordSystem.isThirdNegativ() ? -1 : 1), 0, 0,
				0, 0, (coordSystem.isSecondNegativ() ? -1 : 1), 0));
		}
		else
		{
			t = t.createConcatenation(new Affine(
				(coordSystem.isFirstNegativ() ? -1 : 1), 0, 0, 0,
				0, (coordSystem.isThirdNegativ() ? 1 : -1), 0, 0,
				0, 0, (coordSystem.isSecondNegativ() ? -1 : 1), 0));
		}

		return t;
	}

	/**
	 * @param axis The axis to rotate around
	 * @return a rotation-object around the given axis
	 */
	public static Rotate creRotateForAxis(Coo3dAxis axis)
	{
		Objects.requireNonNull(axis);

		Rotate r = new Rotate();

		switch(axis)
		{
			case X:
			{
				r.setAxis(Rotate.X_AXIS);
				break;
			}
			case Y:
			{
				r.setAxis(Rotate.Y_AXIS);
				break;
			}
			case Z:
			{
				r.setAxis(Rotate.Z_AXIS);
				break;
			}
		}

		return r;
	}
}
