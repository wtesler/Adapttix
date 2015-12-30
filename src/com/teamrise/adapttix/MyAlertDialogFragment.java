package com.teamrise.adapttix;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class MyAlertDialogFragment extends DialogFragment
{
	/**
	 * Create a new instance of MyDialogFragment, provided a title is given.
	 */
	public static MyAlertDialogFragment newInstance(int title)
	{
		MyAlertDialogFragment frag = new MyAlertDialogFragment();

		// Supply title input as an argument
		Bundle args = new Bundle();
		args.putInt("title", title);
		frag.setArguments(args);

		return frag;
	}

	/**
	 * When 'show()' is used on an instance of a MyAlertDialogFragment,
	 * this method is called.
	 * 
	 * The AlertDialog created is based on the inputted title.
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		// Get the self-evaluation's filename.
		final String SELFEVALUATIONFILENAME = getString(R.string.self_evaluation_FILENAME);
		// Used to delete the self-evaluation (if selected).
		final ContextWrapper cw = new ContextWrapper(getActivity());

		/**
		 * Make different AlertDialog fragments based on the inputted title.
		 */
		int title = getArguments().getInt("title");

		switch (title)
		{
			case R.string.alert_dialog_WELCOME:
				return new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
						.setTitle(title)
						.setMessage(R.string.starting_activity_MESSAGE)
						.setNegativeButton("Start Self-Assessment",
								new DialogInterface.OnClickListener()
								{
									public void onClick(DialogInterface dialog, int id)
									{
										Intent intent = new Intent("com.teamrise.adapttix.SELFEVALUATION");
										startActivity(intent);
									}
								})
						.setPositiveButton("Quit",
								new DialogInterface.OnClickListener()
								{
									public void onClick(DialogInterface dialog, int id)
									{
										getActivity().finish();
									};
								})
						.create();
			case R.string.alert_dialog_ERASE_FILE:
				return new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
						.setTitle(title)
						.setMessage(R.string.erase_file_message)
						.setNegativeButton("Yes",
								new DialogInterface.OnClickListener()
								{
									public void onClick(DialogInterface dialog, int buttonId)
									{
										cw.deleteFile(SELFEVALUATIONFILENAME);
									}
								})
						.setPositiveButton("No. Go to the main menu.",
								new DialogInterface.OnClickListener()
								{
									public void onClick(DialogInterface dialog, int whichButton)
									{
										dialog.cancel();
									}
								})
						.create();
		}
		
		// This shouldn't happen:
		return new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
				.setTitle("Problem")
				.setMessage("Error: Non-valid title given to MyAlertDialogFragment.newInstance(title)")
				.create();
	}
	
	
}
