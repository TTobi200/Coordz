package de.gui.view3D.ddd;

public enum Coo3dDddCameraTransformOrder
{
	TXYZ,
	TXZY,
	TYXZ,
	TYZX,
	TZXY,
	TZYX,
	XYZT,
	XZYT,
	YXZT,
	YZXT,
	ZXYT,
	ZYXT;
	
	public boolean isTranslateFirst()
	{
		switch(this)
		{
			case TXYZ:
			case TXZY:
			case TYXZ:
			case TYZX:
			case TZXY:
			case TZYX:
			{
				return true;
			}
			default:
			{
				return false;
			}
		}
	}
	
	public boolean isTranslateLast()
	{
		return !isTranslateFirst();
	}
}
