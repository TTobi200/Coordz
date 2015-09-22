package de.gui.view3D.ddd;

public enum Coo3dAxis
{
	X,
	Y,
	Z;

	public static enum System
	{
		X_Y_Z(X, Y, Z),
		X_Z_Y(X, Z, Y),
		Y_X_Z(Y, X, Z),
		Y_Z_X(Y, Z, X),
		Z_X_Y(Z, X, Y),
		Z_Y_X(Z, Y, X);

		private Coo3dAxis axis1;
		private Coo3dAxis axis2;
		private Coo3dAxis axis3;

		private System(Coo3dAxis axis1, Coo3dAxis axis2, Coo3dAxis axis3)
		{
			this.axis1 = axis1;
			this.axis2 = axis2;
			this.axis3 = axis3;
		}

		public Coo3dAxis getFirstAxis()
		{
			return axis1;
		}

		public Coo3dAxis getSecondAxis()
		{
			return axis2;
		}

		public Coo3dAxis getThirdAxis()
		{
			return axis3;
		}
	}

	public static enum TransformOrder
	{
		T_X_Y_Z(System.X_Y_Z),
		T_X_Z_Y(System.X_Z_Y),
		T_Y_X_Z(System.Y_X_Z),
		T_Y_Z_X(System.Y_Z_X),
		T_Z_X_Y(System.Z_X_Y),
		T_Z_Y_X(System.Z_Y_X),

		X_Y_Z_T(System.X_Y_Z),
		X_Z_Y_T(System.X_Z_Y),
		Y_X_Z_T(System.Y_X_Z),
		Y_Z_X_T(System.Y_Z_X),
		Z_X_Y_T(System.Z_X_Y),
		Z_Y_X_T(System.Z_Y_X);

		private System system;

		private TransformOrder(System system)
		{
			this.system = system;
		}

		public System getSystem()
		{
			return system;
		}

		public boolean isTranslateFirst()
		{
			return ordinal() < 6;
		}

		public boolean isTranslateLast()
		{
			return ordinal() > 5;
		}
	}

	/**
	 * coord system in the order
	 * <ol>
	 * <li>length-axis in direction right
	 * <li>depth-axis in direction 'front'
	 * <li>height-axis in direction up
	 * </ol>
	 * @author ddd
	 *
	 */
	public static enum CoordSystem
	{
		X_Y_Z(System.X_Y_Z, false, false, false),
		NX_Y_Z(System.X_Y_Z, true, false, false),
		X_NY_Z(System.X_Y_Z, false, true, false),
		X_Y_NZ(System.X_Y_Z, false, false, true),
		NX_NY_Z(System.X_Y_Z, true, true, false),
		NX_Y_NZ(System.X_Y_Z, true, false, true),
		X_NY_NZ(System.X_Y_Z, false, true, true),
		NX_NY_NZ(System.X_Y_Z, true, true, true),

		X_Z_Y(System.X_Z_Y, false, false, false),
		NX_Z_Y(System.X_Z_Y, true, false, false),
		X_NZ_Y(System.X_Z_Y, false, true, false),
		X_Z_NY(System.X_Z_Y, false, false, true),
		NX_NZ_Y(System.X_Z_Y, true, true, false),
		NX_Z_NY(System.X_Z_Y, true, false, true),
		X_NZ_NY(System.X_Z_Y, false, true, true),
		NX_NZ_NY(System.X_Z_Y, true, true, true),

		Y_X_Z(System.Y_X_Z, false, false, false),
		NY_X_Z(System.Y_X_Z, true, false, false),
		Y_NX_Z(System.Y_X_Z, false, true, false),
		Y_X_NZ(System.Y_X_Z, false, false, true),
		NY_NX_Z(System.Y_X_Z, true, true, false),
		NY_X_NZ(System.Y_X_Z, true, false, true),
		Y_NX_NZ(System.Y_X_Z, false, true, true),
		NY_NX_NZ(System.Y_X_Z, true, true, true),

		Y_Z_X(System.Y_Z_X, false, false, false),
		NY_Z_X(System.Y_Z_X, true, false, false),
		Y_NZ_X(System.Y_Z_X, false, true, false),
		Y_Z_NX(System.Y_Z_X, false, false, true),
		NY_NZ_X(System.Y_Z_X, true, true, false),
		NY_Z_NX(System.Y_Z_X, true, false, true),
		Y_NZ_NX(System.Y_Z_X, false, true, true),
		NY_NZ_NX(System.Y_Z_X, true, true, true),

		Z_X_Y(System.Z_X_Y, false, false, false),
		NZ_X_Y(System.Z_X_Y, true, false, false),
		Z_NX_Y(System.Z_X_Y, false, true, false),
		Z_X_NY(System.Z_X_Y, false, false, true),
		NZ_NX_Y(System.Z_X_Y, true, true, false),
		NZ_X_NY(System.Z_X_Y, true, false, true),
		Z_NX_NY(System.Z_X_Y, false, true, true),
		NZ_NX_NY(System.Z_X_Y, true, true, true),

		Z_Y_X(System.Z_Y_X, false, false, false),
		NZ_Y_X(System.Z_Y_X, true, false, false),
		Z_NY_X(System.Z_Y_X, false, true, false),
		Z_Y_NX(System.Z_Y_X, false, false, true),
		NZ_NY_X(System.Z_Y_X, true, true, false),
		NZ_Y_NX(System.Z_Y_X, true, false, true),
		Z_NY_NX(System.Z_Y_X, false, true, true),
		NZ_NY_NX(System.Z_Y_X, true, true, true);

		private System system;
		private boolean neg1;
		private boolean neg2;
		private boolean neg3;

		private CoordSystem(System system, boolean neg1, boolean neg2, boolean neg3)
		{
			this.system = system;
			this.neg1 = neg1;
			this.neg2 = neg2;
			this.neg3 = neg3;
		}

		public System getSystem()
		{
			return system;
		}

		public boolean isFirstNegativ()
		{
			return neg1;
		}

		public boolean isSecondNegativ()
		{
			return neg2;
		}

		public boolean isThirdNegativ()
		{
			return neg3;
		}
	}
}
