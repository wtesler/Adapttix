package com.teamrise.adapttix;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.RadioButton;
import android.widget.Toast;

public class SelfEvaluationSummary extends FragmentActivity implements OnClickListener,
		OnLongClickListener
{

	String SELFEVALUATIONFILENAME;
	BufferedInputStream reader;
	BufferedOutputStream bos;

	char[] choices;
	RadioButton[] leftButtons, rightButtons;
	int[] leftButtonIds, rightButtonIds;
	String[] leftTrait = new String[20];
	String[] rightTrait = new String[20];
	String[] leftDescription = new String[20];
	String[] rightDescription = new String[20];

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.self_evaluation_summary);

		setVariables();
	}

	/**
	 * Initialize the variables.
	 */
	private void setVariables()
	{
		SELFEVALUATIONFILENAME = getString(R.string.self_evaluation_FILENAME);

		leftTrait = getResources().getStringArray(R.array.LeftEvaluationTraits);
		rightTrait = getResources().getStringArray(R.array.RightEvaluationTraits);
		leftDescription = getResources().getStringArray(R.array.LeftSelfDescriptions);
		rightDescription = getResources().getStringArray(R.array.RightSelfDescriptions);

		// Try to open the self-evaluation file, get the user's choices from the
		// file, and close the file.
		choices = new char[20];
		try
		{
			reader = new BufferedInputStream(openFileInput(SELFEVALUATIONFILENAME));
			for (int i = 0; i < 20; i++)
			{
				choices[i] = (char) reader.read();
			}
			reader.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		/*
		 * Get all of the button's ids, connect them to this class, and set the
		 * text, descriptions, onClick, and onLongClick listeners.
		 */
		leftButtons = new RadioButton[20];
		leftButtonIds = new int[20];
		int i = 0;
		int j = 0;
		while (i < 40)
		{
			leftButtonIds[j] = getResources().getIdentifier("radio" + i, "id", getPackageName());
			leftButtons[j] = (RadioButton) findViewById(leftButtonIds[j]);

			leftButtons[j].setText(leftTrait[j]);
			leftButtons[j].setGravity(Gravity.CENTER);
			leftButtons[j].setPadding(10, 10, 10, 10);

			leftButtons[j].setOnClickListener(this);
			leftButtons[j].setOnLongClickListener(this);

			i += 2;
			j += 1;
		}

		rightButtons = new RadioButton[20];
		rightButtonIds = new int[20];
		int a = 1;
		int b = 0;
		while (a < 40)
		{
			rightButtonIds[b] = getResources().getIdentifier("radio" + a, "id", getPackageName());
			rightButtons[b] = (RadioButton) findViewById(rightButtonIds[b]);

			rightButtons[b].setText(rightTrait[b]);
			rightButtons[b].setGravity(Gravity.CENTER);
			rightButtons[b].setPadding(10, 10, 10, 10);

			rightButtons[b].setOnClickListener(this);
			rightButtons[b].setOnLongClickListener(this);

			a += 2;
			b += 1;
		}

		initializeButtons();
	}

	/**
	 * Press the buttons that the user has chosen.
	 */
	private void initializeButtons()
	{
		for (int i = 0; i < 20; i++)
		{
			if (choices[i] == 'L')
			{
				leftButtons[i].setChecked(true);
				rightButtons[i].setTextColor(Color.WHITE);
			} else if (choices[i] == 'R')
			{
				rightButtons[i].setChecked(true);
				leftButtons[i].setTextColor(Color.WHITE);
			}
		}
	}

	private void switchButtons(int buttonIndex)
	{

		if (choices[buttonIndex] == 'L')
		{
			leftButtons[buttonIndex].setChecked(true);
			leftButtons[buttonIndex].setTextColor(Color.BLACK);
			rightButtons[buttonIndex].setTextColor(Color.WHITE);
		} else if (choices[buttonIndex] == 'R')
		{
			rightButtons[buttonIndex].setChecked(true);
			rightButtons[buttonIndex].setTextColor(Color.BLACK);
			leftButtons[buttonIndex].setTextColor(Color.WHITE);
		}

	}

	public void onClick(View v)
	{
		for (int i = 0; i < 20; i++)
		{
			if (v.getId() == leftButtonIds[i])
			{
				choices[i] = 'L';
				switchButtons(i);
			}

			else if (v.getId() == rightButtonIds[i])
			{
				choices[i] = 'R';
				switchButtons(i);
			}
		}
	}

	/**
	 * When anything that has an OnLongClickListener set is clicked, this
	 * function runs.
	 */

	// WILL TESLER EDITED LAST
	public boolean onLongClick(View v)
	{
		int viewId = v.getId();

		for (int i = 0; i < 20; i++)
		{
			// IF LEFT BUTTON
			if (viewId == leftButtonIds[i])
			{
				DialogFragment newFragment = SelfEvaluationSummaryAlertDialogFragment.newInstance(
						"" + leftButtons[i].getText(), leftDescription[i]);
				newFragment.show(getSupportFragmentManager(), "dialog");
			}
			// IF RIGHT BUTTON
			if (viewId == rightButtonIds[i])
			{
				DialogFragment newFragment = SelfEvaluationSummaryAlertDialogFragment.newInstance(
						"" + rightButtons[i].getText(), rightDescription[i]);
				newFragment.show(getSupportFragmentManager(), "dialog");
			}
		}

		return false;
	}

	@Override
	protected void onStop()
	{
		super.onStop();

		// Open self-evaluation for writing.
		try
		{
			bos = new BufferedOutputStream(openFileOutput(SELFEVALUATIONFILENAME,
					Context.MODE_PRIVATE));
			for (int i = 0; i < 20; i++)
			{
				bos.write(choices[i]);
			}
			bos.flush();
			bos.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		finish();
	}

	public static class SelfEvaluationSummaryAlertDialogFragment extends DialogFragment
	{

		public static SelfEvaluationSummaryAlertDialogFragment newInstance(String name,
				String description)
		{
			SelfEvaluationSummaryAlertDialogFragment fragment = new SelfEvaluationSummaryAlertDialogFragment();
			Bundle args = new Bundle();
			args.putString("name", name);
			args.putString("description", description);
			fragment.setArguments(args);
			return fragment;
		}

		/**
		 * When 'show()' is used on an instance of a
		 * SelfEvaluationSummaryAlertDialogFragment, this method is called.
		 */
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{ 
			String name = getArguments().getString("name");
			String description = getArguments().getString("description");
			return new AlertDialog.Builder(getActivity()).setTitle(name).setMessage(description).setIcon(0)
					.create();
		}
	}
}
