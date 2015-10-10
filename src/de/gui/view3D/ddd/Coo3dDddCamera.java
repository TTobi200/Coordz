package de.gui.view3D.ddd;

import de.gui.view3D.ddd.Coo3dAxis.CoordSystem;
import de.gui.view3D.ddd.Coo3dAxis.TransformOrder;
import de.gui.view3D.ddd.util.CooDddUtil;
import javafx.beans.property.DoubleProperty;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Coo3dDddCamera extends PerspectiveCamera
{
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
			CooDddUtil.getTransformFor(coordSystem),
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
