package com.teamrise.adapttix;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PrescriptionsCalculator extends Activity implements OnClickListener
{
	int ARRAYSIZE = 20;
	String SELFEVALUATIONFILENAME;
	ArrayList<Integer> prescriptionList;

	BufferedInputStream SelfEvaluationFile, IndividualEvaluationFile;
	char[] selfEvaluationChoices, individualEvaluationChoices;
	String[] files;
	Boolean checkboxFileExists, commentFileExists;
	Bundle gotChest;
	String individualFILENAME, checkboxFILENAME, commentFILENAME;
	TextView tv;
	Button b;
	
	BufferedOutputStream bos;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.prescriptions_calculator);

		setVariables();

		try
		{
			// READ IN SELF ASSESSMENT DATA INTO A CHAR ARRAY
			SelfEvaluationFile = new BufferedInputStream(
					openFileInput(SELFEVALUATIONFILENAME));
			for (int i = 0; i < ARRAYSIZE; i++)
			{
				selfEvaluationChoices[i] = (char) SelfEvaluationFile.read();
			}
			SelfEvaluationFile.close();

			// READ IN INDIVIDUAL ASSESSMENT DATA INTO A CHAR ARRAY
			IndividualEvaluationFile = new BufferedInputStream(
					openFileInput(individualFILENAME));
			for (int i = 0; i < ARRAYSIZE; i++)
			{
				individualEvaluationChoices[i] = (char) IndividualEvaluationFile
						.read();
			}
			IndividualEvaluationFile.close();

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		// CROSS REFERENCE THE TWO FILES TO DETERMINE PRESCRIPTIONS
		prescriptionList = new ArrayList<Integer>();
		for (int i = 0; i < ARRAYSIZE; i++)
		{
			if (individualEvaluationChoices[i] == 'L'
					&& selfEvaluationChoices[i] != 'L')
			{
				prescriptionList.add(i);
			}
			if (individualEvaluationChoices[i] == 'R'
					&& selfEvaluationChoices[i] != 'R')
			{
				prescriptionList.add(i + ARRAYSIZE);
			}
		}

		// Check to see if a checkbox file has been created for this
		// individual.
		checkboxFILENAME = individualFILENAME.replace("_individual",
				"_checkbox");
		checkboxFileExists = false;
		files = fileList();
		for (int i = 0; i < files.length; i++)
		{
			if (files[i].contains(checkboxFILENAME))
			{
				checkboxFileExists = true;
			}
		}

		// Creates Checkbox settings in new file if !prescriptionFileExists
		if (checkboxFileExists == false)
		{
			try
			{
				bos = new BufferedOutputStream(openFileOutput(checkboxFILENAME,
						Context.MODE_PRIVATE));
				/*
				 * Writes the letter 'N' as many times as there are elements.
				 * This will later be used to tell if checkbox should be checked
				 * ('Y') or unchecked ('N').
				 */
				for (int i = 0; i < prescriptionList.size(); i++)
				{
					bos.write('N');
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
		}
		// Check to see if a comment file has been created for this
		// individual.
		commentFILENAME = individualFILENAME.replace("_individual", "_comment");
		commentFileExists = false;
		for (int i = 0; i < files.length; i++)
		{
			if (files[i].contains(checkboxFILENAME))
			{
				commentFileExists = true;
			}
		}

		// Creates Comment settings in new file if !commentFileExists
		if (commentFileExists == false)
		{
			try
			{
				// FILE CREATED
				bos = new BufferedOutputStream(openFileOutput(commentFILENAME,
						Context.MODE_PRIVATE));
				bos.close();
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		// for(int i = 0 ; i < prescriptionList.size() ; i++)
		// Toast.makeText(getApplicationContext(),
		// prescriptionList.get(i).toString(), Toast.LENGTH_SHORT).show();
	}

	private void setVariables()
	{
		// Get the self-evaluation filename (from strings.xml).
		SELFEVALUATIONFILENAME = getString(R.string.self_evaluation_FILENAME);

		gotChest = getIntent().getExtras();
		individualFILENAME = gotChest.getString("filename");

		SelfEvaluationFile = null;
		IndividualEvaluationFile = null;

		selfEvaluationChoices = new char[ARRAYSIZE];
		individualEvaluationChoices = new char[ARRAYSIZE];
		
		tv = (TextView) findViewById(R.id.tvCalculator);
		tv.setPadding(15, 15, 15, 15);
		tv.setText("Your Prescriptions are Ready!");
		b = (Button) findViewById(R.id.bCalculator);
		b.setOnClickListener(this);
	}

	protected void onPause()
	{
		super.onPause();
		finish();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		finish();
	}

	public void onClick(View arg0)
	{
		Bundle bundle = new Bundle();
		bundle.putString("checkbox", checkboxFILENAME);
		bundle.putIntegerArrayList("prescriptions", prescriptionList);
		Intent intent = new Intent("com.teamrise.adapttix.PRESCRIPTIONS");
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}

}
