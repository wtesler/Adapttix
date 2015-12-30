package com.teamrise.adapttix;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainMenu extends FragmentActivity implements OnClickListener
{
	int title; // "ID" for AlertDialogs.
	String SELFEVALUATIONFILENAME;
	ContextWrapper cw;

	// First time buttons:
	Button firstTimeSelfAssessment, firstTimeQuit;

	// Main menu buttons:
	Button addIndividual, individuals, updateSelfEvaluation;

	Intent intent; // Global variables are bad, but hey, whatcha gonna do?

	// To check for SELFEVALUATIONFILENAME:
	String[] files;
	boolean SEFExists; // true == self-evaluation file exists

	DialogFragment newFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		SEFExists = false;
		setVariables();
		chooseMenuToDisplay();
	}

	/**
	 * Initialize the variables.
	 */
	private void setVariables()
	{
		// Get the self-evaluation filename (from strings.xml).
		SELFEVALUATIONFILENAME = getString(R.string.self_evaluation_FILENAME);
		// Used to delete SELFEVALUATIONFILENAME (if selected).
		cw = new ContextWrapper(this);

		if (SEFExists == true)
		{
			addIndividual = (Button) findViewById(R.id.bNewIndividual);
			addIndividual.setOnClickListener(this);

			individuals = (Button) findViewById(R.id.bIndividuals);
			individuals.setOnClickListener(this);

			updateSelfEvaluation = (Button) findViewById(R.id.bUpdateSelfEvaluation);
			updateSelfEvaluation.setOnClickListener(this);

			/*
			 * spinner = (ImageView) findViewById(R.id.ivSpinner);
			 * spinner.startAnimation(AnimationUtils.loadAnimation(this,
			 * R.id.ivSpinner));
			 */
		}
	}

	/**
	 * Get an array of strings naming the private files associated with this
	 * Context's application package. If the user has already completed a
	 * self-evaluation, it should appear in the list of files. If the user
	 * hasn't, make sure they complete a self-evaluation before doing anything
	 * else (load startingactivity.xml instead of mainmenu.xml).
	 * 
	 */
	private void chooseMenuToDisplay()
	{
		Toast.makeText(getApplicationContext(), "TEST 2", Toast.LENGTH_SHORT).show();

		files = fileList();
		for (int i = 0; i < files.length; i++)
		{
			Toast.makeText(getApplicationContext(), "TEST 3", Toast.LENGTH_SHORT).show();
			if (files[i].contentEquals(SELFEVALUATIONFILENAME))
			{
				Toast.makeText(getApplicationContext(), "TEST 4", Toast.LENGTH_SHORT).show();
				SEFExists = true;
				break;
			}
		}

		showEitherAlertDialogOrMainMenu();

	}

	private void showEitherAlertDialogOrMainMenu()
	{
		/**
		 * If the self-evaluation does not exist/was deleted, show the starting
		 * alert dialog.
		 */
		if (SEFExists == false)
		{
			Toast.makeText(getApplicationContext(), "TEST 5", Toast.LENGTH_SHORT).show();
			title = R.string.alert_dialog_WELCOME;
			showDialog();
		}
	}

	void showDialog()
	{
		if (newFragment == null)
		{
			newFragment = AdapttixAlertDialogFragment.newInstance(title);
		}

		newFragment.show(getSupportFragmentManager(), "dialog");
	}

	/**
	 * When anything that has an OnClickListener set is clicked, this function
	 * runs.
	 */
	public void onClick(View v)
	{
		switch (v.getId())
		{

		case R.id.bNewIndividual:
			intent = new Intent("com.teamrise.adapttix.GETNAME");
			startActivity(intent);
			break;

		case R.id.bIndividuals:
			intent = new Intent("com.teamrise.adapttix.INDIVIDUALS");
			startActivity(intent);
			break;

		case R.id.bUpdateSelfEvaluation:
			intent = new Intent("com.teamrise.adapttix.SELFEVALUATIONSUMMARY");
			startActivity(intent);
			break;
		}
	}

	/*
	 * FOR ACTUAL MENU BUTTON:
	 */

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		MenuInflater blowUp = getMenuInflater();
		blowUp.inflate(R.menu.menu, menu);
		return true;
	}

	/**
	 * When an item in the menu is clicked, this function gets called.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.about:
			intent = new Intent("com.teamrise.adapttix.ABOUT");
			startActivity(intent);
			break;
		case R.id.contact:
			intent = new Intent("com.teamrise.adapttix.CONTACT");
			startActivity(intent);
			break;
		case R.id.legal:
			intent = new Intent("com.teamrise.adapttix.LEGAL");
			startActivity(intent);
			break;
		}
		return false;
	}

	public static class AdapttixAlertDialogFragment extends DialogFragment
	{

		// Create a new instance of ApadttixAlertDialogFragment, provided a
		// title is given.

		public static AdapttixAlertDialogFragment newInstance(int title)
		{
			AdapttixAlertDialogFragment fragment = new AdapttixAlertDialogFragment();

			// Supply title input as an argument
			Bundle args = new Bundle();
			args.putInt("title", title);
			fragment.setArguments(args);
			fragment.setCancelable(false);

			return fragment;
		}

		// When 'show()' is used on an instance of a
		// AdapttixAlertDialogFragment, this method is called.

		// The AlertDialog created is based on the inputted title.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			// Make different AlertDialog fragments based on the inputted title.

			int title = getArguments().getInt("title");
			switch (title)
			{
			case R.string.alert_dialog_WELCOME:
				return new AlertDialog.Builder(getActivity())
						.setTitle(title)
						.setMessage(R.string.starting_activity_MESSAGE)
						.setPositiveButton("Continue",
								new DialogInterface.OnClickListener()
								{
									public void onClick(DialogInterface dialog, int id)
									{
										dialog.dismiss();
										Intent intent = new Intent("com.teamrise.adapttix.SELFEVALUATION");
										startActivity(intent);
									}
								}
						)
						.create();
			}

			// This shouldn't happen:
			return new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
					.setTitle("Problem")
					.setMessage("Error: Non-valid title given to AdapttixAlertDialogFragment.newInstance(title)")
					.create();
		}
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		SEFExists = false;
		Toast.makeText(getApplicationContext(), "TEST 1", Toast.LENGTH_SHORT).show();
		chooseMenuToDisplay();
		setVariables();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

}