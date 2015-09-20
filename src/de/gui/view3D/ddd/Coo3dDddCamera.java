package de.gui.view3D.ddd;

import javafx.beans.property.DoubleProperty;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Coo3dDddCamera extends PerspectiveCamera
{
	private Coo3dDddCameraTransformOrder transformOrder;

	private Translate pos;
	private Rotate rx;
	private Rotate ry;
	private Rotate rz;

	public Coo3dDddCamera(Coo3dDddCameraTransformOrder transformOrder)
	{
		pos = new Translate();
		rx = new Rotate();
		rx.setAxis(Rotate.X_AXIS);
		ry = new Rotate();
		ry.setAxis(Rotate.Y_AXIS);
		rz = new Rotate();
		rz.setAxis(Rotate.Z_AXIS);

		this.transformOrder = transformOrder;

		initTransforms();
	}

	public final void initTransforms()
	{
		getTransforms().clear();

		switch(transformOrder)
		{
			case TXYZ:
			case XYZT:
			{
				getTransforms().addAll(rx, ry, rz);
				break;
			}
			case TXZY:
			case XZYT:
			{
				getTransforms().addAll(rx, rz, ry);
				break;
			}
			case TYXZ:
			case YXZT:
			{
				getTransforms().addAll(ry, rx, rz);
				break;
			}
			case TYZX:
			case YZXT:
			{
				getTransforms().addAll(ry, rz, rx);
				break;
			}
			case TZXY:
			case ZXYT:
			{
				getTransforms().addAll(rz, rx, ry);
				break;
			}
			case TZYX:
			case ZYXT:
			{
				getTransforms().addAll(rz, ry, rx);
				break;
			}
		}

		if(transformOrder.isTranslateFirst())
		{
			getTransforms().add(0, pos);
		}
		else
		{
			getTransforms().add(pos);
		}
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
