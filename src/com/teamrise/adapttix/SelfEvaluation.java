package com.teamrise.adapttix;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SelfEvaluation extends FragmentActivity implements OnClickListener, OnLongClickListener
{
	int ARRAYSIZE = 20;
	String SELFEVALUATIONFILENAME;
	BufferedOutputStream bos;

	int counter; // This denotes the pair of traits that the activity is on
	Button back, forward;
	RadioButton left, right;
	RadioGroup rg;
	String[] leftTraits = new String[ARRAYSIZE];
	String[] rightTraits = new String[ARRAYSIZE];
	String[] leftDescription = new String[ARRAYSIZE];
	String[] rightDescription = new String[ARRAYSIZE];
	char[] sideChoice;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		setContentView(R.layout.self_evaluation);

		setVariables();
	}

	// Initialize the variables.
	private void setVariables()
	{
		SELFEVALUATIONFILENAME = getString(R.string.self_evaluation_FILENAME);

		back = (Button) findViewById(R.id.bSelfEvaluationBack);
		back.setOnClickListener(this);

		forward = (Button) findViewById(R.id.bSelfEvaluationForward);
		forward.setOnClickListener(this);

		left = (RadioButton) findViewById(R.id.rbLeft);
		left.setOnClickListener(this);
		left.setOnLongClickListener(this);

		right = (RadioButton) findViewById(R.id.rbRight);
		right.setOnClickListener(this);
		right.setOnLongClickListener(this);

		rg = (RadioGroup) findViewById(R.id.rgChoice);

		counter = 0;

		leftTraits = getResources().getStringArray(R.array.LeftEvaluationTraits);
		rightTraits = getResources().getStringArray(R.array.RightEvaluationTraits);

		leftDescription = getResources().getStringArray(R.array.LeftSelfDescriptions);
		rightDescription = getResources().getStringArray(R.array.RightSelfDescriptions);

		// NEW CHOICES = 'X', LEFT CHOICE = 'L', RIGHT CHOICE = 'R'
		sideChoice = new char[ARRAYSIZE];
		for (int i = 0; i < ARRAYSIZE; i++)
		{
			sideChoice[i] = 'X';
		}

		/**
		 * Make the Back and Forward buttons gone for the first choice set (to
		 * avoid a null pointer exception in the arrays, and just because it
		 * makes no sense to have them there).
		 */
		back.setVisibility(View.GONE);
		forward.setVisibility(View.GONE);
	}

	/**
	 * When anything that has an OnClickListener set is clicked, this function
	 * runs.
	 */
	public void onClick(View v)
	{
		/**
		 * The Back button is set to invisible on the first choice set, so make
		 * it visible now that the user is beyond the first choice set.
		 */

		back.setVisibility(View.VISIBLE);

		switch (v.getId())
		{
		case R.id.rbLeft:
			left.toggle();
			sideChoice[counter] = 'L';
			counter++;
			break;

		case R.id.rbRight:
			right.toggle();
			sideChoice[counter] = 'R';
			counter++;
			break;

		case R.id.bSelfEvaluationBack:
			counter--;
			break;

		case R.id.bSelfEvaluationForward:
			counter++;
			break;
		}

		if (counter < ARRAYSIZE)
		{
			if (sideChoice[counter] == 'X')
			{
				rg.clearCheck();
				left.setTextColor(Color.WHITE);
				right.setTextColor(Color.WHITE);

				forward.setVisibility(View.GONE);
			}
			else if (sideChoice[counter] == 'L')
			{
				left.toggle();
				left.setTextColor(Color.BLACK);
				right.setTextColor(Color.WHITE);
				forward.setVisibility(View.VISIBLE);
			}
			else if (sideChoice[counter] == 'R')
			{
				right.toggle();
				right.setTextColor(Color.BLACK);
				left.setTextColor(Color.WHITE);
				forward.setVisibility(View.VISIBLE);
			}

			left.setText(leftTraits[counter]);
			right.setText(rightTraits[counter]);

			/**
			 * If the user has backed up to the first choice set, make the Back
			 * button invisible again.
			 */
			if (counter == 0)
			{
				back.setVisibility(View.GONE);
			}
		}
		else
		{

			back.setClickable(false);
			left.setClickable(false);
			right.setClickable(false);

			try
			{

				bos = new BufferedOutputStream(openFileOutput(SELFEVALUATIONFILENAME, Context.MODE_PRIVATE));
				/*
				 * Write to SELFEVALUATIONFILENAME as a list of integers with no
				 * spaces.
				 */
				for (int i = 0; i < ARRAYSIZE; i++)
				{
					bos.write(sideChoice[i]);
				}
				/**
				 * Ensure all data gets flushed from the BufferedOutputStream
				 * before closing the file.
				 */
				bos.flush();
				bos.close();

			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			} finally
			{
				Intent intent = new Intent("com.teamrise.adapttix.SELFEVALUATIONSUMMARY");
				startActivity(intent);
				finish();
			}
		}
	}

	public boolean onLongClick(View v)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		switch (v.getId())
		{

		case R.id.rbLeft:

			builder.setTitle(leftTraits[counter])
					.setMessage(leftDescription[counter])
					.create()
					.show();

			break;

		case R.id.rbRight:

			builder.setTitle(rightTraits[counter])
					.setMessage(rightDescription[counter])
					.create()
					.show();

			break;
		}

		return false;
	}

	/**
	 * Disable the actual Back button.
	 */
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		finish();
	}
}
