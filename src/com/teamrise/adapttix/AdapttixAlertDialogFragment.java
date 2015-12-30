package com.teamrise.adapttix;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class AdapttixAlertDialogFragment extends DialogFragment
{
	/**
	 * Create a new instance of ApadttixAlertDialogFragment, provided a title is given.
	 */
	public static AdapttixAlertDialogFragment newInstance(int title)
	{
		AdapttixAlertDialogFragment frag = new AdapttixAlertDialogFragment();

		// Supply title input as an argument
		Bundle args = new Bundle();
		args.putInt("title", title);
		frag.setArguments(args);

		return frag;
	}

	/**
	 * When 'show()' is used on an instance of a AdapttixAlertDialogFragment,
	 * this method is called.
	 * 
	 * The AlertDialog created is based on the inputted title.
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		/**
		 * Make different AlertDialog fragments based on the inputted title.
		 */
		int title = getArguments().getInt("title");
		switch (title)
		{
			case R.string.alert_dialog_WELCOME:
				return new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
						.setTitle(title)
						.setMessage(R.string.starting_activity_MESSAGE)
						.setPositiveButton("Start Self-Assessment",
								new DialogInterface.OnClickListener()
								{
									public void onClick(DialogInterface dialog, int id)
									{
										Intent intent = new Intent("com.teamrise.adapttix.SELFEVALUATION");
										startActivity(intent);
									}
								})
								.create();
		}
		
		// This shouldn't happen:
		return new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
				.setTitle("Problem")
				.setMessage("Error: Non-valid title given to AdapttixAlertDialogFragment.newInstance(title)")
				.create();
	}
}
