package de.gui.view3D.ddd;

import java.util.Collections;
import java.util.List;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;

import javafx.scene.input.MouseEvent;

public class CooView3dDddBehavior extends BehaviorBase<CooView3dDdd>
{
	private static final List<KeyBinding> keyBindings = Collections.emptyList();

	public CooView3dDddBehavior(CooView3dDdd control)
	{
		super(control, keyBindings);
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);

		if(!getControl().isFocused())
		{
			getControl().requestFocus();
		}
	}
}
