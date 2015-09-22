package de.gui.view3D.ddd;

import java.util.EnumSet;
import java.util.Objects;

import de.gui.view3D.ddd.Coo3dAxis.CoordSystem;
import de.gui.view3D.ddd.Coo3dAxis.System;
import de.gui.view3D.ddd.Coo3dAxis.TransformOrder;
import javafx.beans.property.DoubleProperty;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.*;

public class Coo3dDddCamera extends PerspectiveCamera
{
	private static final double ROTATE_QUARTER = 90d;
	private static final double ROTATE_HALF = ROTATE_QUARTER * 2;

	public static final Coo3dAxis FIRST_AXIS = Coo3dAxis.X;
	public static final Coo3dAxis SECOND_AXIS = Coo3dAxis.Z;
	public static final Coo3dAxis THIRD_AXIS = Coo3dAxis.Y;

	private static final EnumSet<System> INTUITIVE_ALL_POSITIVE = EnumSet.of(System.X_Y_Z, System.Y_Z_X, System.Z_X_Y);

	private TransformOrder transformOrder;
	private CoordSystem coordSystem;

	private Translate pos;
	private Rotate rx;
	private Rotate ry;
	private Rotate rz;

	public Coo3dDddCamera(TransformOrder transformOrder, CoordSystem coordSystem)
	{
		pos = new Translate();
		rx = new Rotate();
		rx.setAxis(Rotate.X_AXIS);
		ry = new Rotate();
		ry.setAxis(Rotate.Y_AXIS);
		rz = new Rotate();
		rz.setAxis(Rotate.Z_AXIS);

		this.transformOrder = transformOrder;
		this.coordSystem = coordSystem;

		initTransforms();
	}

	public final void initTransforms()
	{
		getTransforms().clear();

		getTransforms().addAll(
			getTransformFor(coordSystem),
			getRotateFor(transformOrder.getSystem().getFirstAxis()),
			getRotateFor(transformOrder.getSystem().getSecondAxis()),
			getRotateFor(transformOrder.getSystem().getThirdAxis()));

		if(transformOrder.isTranslateFirst())
		{
			getTransforms().add(0, pos);
		}
		else
		{
			getTransforms().add(pos);
		}
	}

	private static Rotate creRotateForAxis(Coo3dAxis axis)
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

	private Rotate getRotateFor(Coo3dAxis axis)
	{
		switch(axis)
		{
			case X:
			{
				return rx;
			}
			case Y:
			{
				return ry;
			}
			case Z:
			{
				return rz;
			}
		}

		return null;
	}

	private Transform getTransformFor(System system)
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

	private Transform getTransformFor(CoordSystem coordSystem)
	{
		Transform t = getTransformFor(coordSystem.getSystem());

		int negAxis = 0;
		if(coordSystem.isFirstNegativ())
		{
			negAxis++;
		}
		if(coordSystem.isSecondNegativ())
		{
			negAxis++;
		}
		if(coordSystem.isThirdNegativ())
		{
			negAxis++;
		}

		switch(negAxis)
		{
			case 0:
			{
				if(INTUITIVE_ALL_POSITIVE.contains(coordSystem.getSystem()))
				{
					break;
				}
				else
				{
					// TODO $Ddd
					throw new IllegalArgumentException("Coordsystem " + coordSystem + " not supported yet");
				}
			}
			case 1:
			{
				if(INTUITIVE_ALL_POSITIVE.contains(coordSystem.getSystem()))
				{
					// TODO $Ddd
					throw new IllegalArgumentException("Coordsystem " + coordSystem + " not supported yet");
				}
				else
				{
					Rotate r = null;
					if(coordSystem.isThirdNegativ())
					{
						break;
					}
					else if(coordSystem.isSecondNegativ())
					{
						r = creRotateForAxis(FIRST_AXIS);
					}
					else if(coordSystem.isFirstNegativ())
					{
						r = creRotateForAxis(SECOND_AXIS);
					}

					r.setAngle(ROTATE_HALF);
					t = t.createConcatenation(r);
				}
				break;
			}
			case 2:
			{
				if(INTUITIVE_ALL_POSITIVE.contains(coordSystem.getSystem()))
				{
					Rotate r;
					if(!coordSystem.isFirstNegativ())
					{
						r = creRotateForAxis(FIRST_AXIS);
					}
					else if(!coordSystem.isSecondNegativ())
					{

						r = creRotateForAxis(SECOND_AXIS);
					}
					else
					{
						r = creRotateForAxis(THIRD_AXIS);
					}

					r.setAngle(ROTATE_HALF);
					t = t.createConcatenation(r);
				}
				else
				{
					// TODO $Ddd
					throw new IllegalArgumentException("Coordsystem " + coordSystem + " not supported yet");
				}
				break;
			}
			case 3:
			{
				if(INTUITIVE_ALL_POSITIVE.contains(coordSystem.getSystem()))
				{
					// TODO $Ddd
					throw new IllegalArgumentException("Coordsystem " + coordSystem + " not supported yet");
				}
				else
				{
					Rotate r = creRotateForAxis(Coo3dAxis.Y);
//					Rotate r = creRotateForAxis(coordSystem.getSystem().getThirdAxis());
					r.setAngle(ROTATE_HALF);
					t = t.createConcatenation(r);
				}
				break;
			}
		}

		return t;
	}

	public DoubleProperty xAngleProperty()
	{
		return rx.angleProperty();
	}

	public double getXAngle()
	{
		return rx.getAngle();
	}

	public void setXAngle(double xAngle)
	{
		rx.setAngle(xAngle);
	}

	public void rotateX(double xAngle)
	{
		rx.setAngle(rx.getAngle() + xAngle);
	}

	public DoubleProperty yAngleProperty()
	{
		return ry.angleProperty();
	}

	public double getYAngle()
	{
		return ry.getAngle();
	}

	public void setYAngle(double yAngle)
	{
		ry.setAngle(yAngle);
	}

	public void rotateY(double yAngle)
	{
		ry.setAngle(ry.getAngle() + yAngle);
	}

	public DoubleProperty zAngleProperty()
	{
		return rz.angleProperty();
	}

	public double getZAngle()
	{
		return rz.getAngle();
	}

	public void setZAngle(double zAngle)
	{
		rz.setAngle(zAngle);
	}

	public void rotateZ(double zAngle)
	{
		rz.setAngle(rz.getAngle() + zAngle);
	}

	public Translate getPos()
	{
		return pos;
	}

	public void moveX(double deltaX)
	{
		pos.setX(pos.getX() + deltaX);
	}

	public void moveY(double deltaY)
	{
		pos.setY(pos.getY() + deltaY);
	}

	public void moveZ(double deltaZ)
	{
		pos.setZ(pos.getZ() + deltaZ);
	}
}
