package com.teamrise.adapttix;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class IndividualEvaluation extends FragmentActivity implements OnClickListener
{
	int ARRAYSIZE = 20;
	String individualsName;
	Bundle gotBasket;
	TextView name;

	int counter; // This denotes the pair of traits that the activity is on
	Button back, forward, left, right, neutral;
	String[] leftDescription = new String[ARRAYSIZE];
	String[] rightDescription = new String[ARRAYSIZE];
	String[] files;
	char[] sideChoice;
	ContextWrapper cw = new ContextWrapper(this);

	BufferedOutputStream bos;
	String INDIVIDUALEVALUATIONFILENAME, CHECKBOXFILENAME;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.individual_evaluation);

		setVariables();
	}

	/**
	 * Initialize the variables.
	 */
	private void setVariables()
	{
		counter = 0;

		gotBasket = getIntent().getExtras();
		individualsName = gotBasket.getString("Individual Name");

		/**
		 * Replaces whitespace with "_", and change all characters to lowercase.
		 */
		INDIVIDUALEVALUATIONFILENAME = individualsName.replaceAll(" ", "_").toLowerCase()
				+ "_individual";
		
		CHECKBOXFILENAME = individualsName.replaceAll(" ", "_").toLowerCase()
				+ "_checkbox";

		leftDescription = getResources().getStringArray(R.array.IndividualLeft);
		rightDescription = getResources().getStringArray(R.array.IndividualRight);

		back = (Button) findViewById(R.id.bIndividualEvaluationBack);
		back.setOnClickListener(this);

		forward = (Button) findViewById(R.id.bIndividualEvaluationForward);
		forward.setOnClickListener(this);

		left = (Button) findViewById(R.id.bIndividualEvaluationLeft);
		left.setOnClickListener(this);

		right = (Button) findViewById(R.id.bIndividualEvaluationRight);
		right.setOnClickListener(this);

		neutral = (Button) findViewById(R.id.bIndividualEvaluationNeither);
		neutral.setOnClickListener(this);

		sideChoice = new char[ARRAYSIZE]; // LEFT CHOICE = 'L' , RIGHT CHOICE =
											// 'R', NEITHER = 'N', NEW CHOICES =
											// 'X'
		for (int i = 0; i < ARRAYSIZE; i++)
		{
			sideChoice[i] = 'X';
		}

		/**
		 * Make the Back and Forward buttons invisible for the first choice set
		 * (to avoid a null pointer exception in the arrays, and just because it
		 * makes no sense to have them there).
		 */
		back.setVisibility(View.INVISIBLE);
		forward.setVisibility(View.INVISIBLE);
	}

	public void onClick(View v)
	{

		/**
		 * The Back button is set to invisible on the first choice set, so make
		 * it visible now that the user is beyond the first choice set.
		 */
		back.setVisibility(View.VISIBLE);

		switch (v.getId())
		{
		case R.id.bIndividualEvaluationLeft:
			sideChoice[counter] = 'L';
			counter++;
			break;
		case R.id.bIndividualEvaluationRight:
			sideChoice[counter] = 'R';
			counter++;
			break;
		case R.id.bIndividualEvaluationNeither:
			sideChoice[counter] = 'N';
			counter++;
			break;
		case R.id.bIndividualEvaluationBack:
			counter--;
			break;
		case R.id.bIndividualEvaluationForward:
			counter++;
			break;
		}
		if (counter < ARRAYSIZE)
		{
			if (sideChoice[counter] == 'X')
			{
				left.setPressed(false);
				right.setPressed(false);
				forward.setVisibility(View.INVISIBLE);
			} else if (sideChoice[counter] == 'L')
			{
				left.setPressed(true);
				right.setPressed(false);
				forward.setVisibility(View.VISIBLE);
			} else if (sideChoice[counter] == 'R')
			{
				left.setPressed(false);
				right.setPressed(true);
				forward.setVisibility(View.VISIBLE);
			}

			left.setText(leftDescription[counter]);
			right.setText(rightDescription[counter]);

			/**
			 * If the user has backed up to the first choice set, make the Back
			 * button invisible again.
			 */
			if (counter == 0)
			{
				back.setVisibility(View.INVISIBLE);
			}
		} else
		{

			back.setClickable(false);
			left.setClickable(false);
			right.setClickable(false);
			neutral.setClickable(false);

			try
			{
				bos = new BufferedOutputStream(openFileOutput(INDIVIDUALEVALUATIONFILENAME,Context.MODE_PRIVATE));
				/*
				 * Writes choices to INDIVIDUALEVALUATIONFILENAME as a list of
				 * chars with NO spaces.
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
				cw.deleteFile(CHECKBOXFILENAME);
				
				setResult(RESULT_OK); // End GetName.

				Bundle bundle = new Bundle();
				bundle.putString("filename", INDIVIDUALEVALUATIONFILENAME);
				Intent intent = new Intent("com.teamrise.adapttix.PRESCRIPTIONSCALCULATOR");
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
		}
	}

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

	@Override
	protected void onPause()
	{
		super.onPause();
		finish();
	}

}
