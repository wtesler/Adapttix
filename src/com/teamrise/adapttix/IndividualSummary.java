package com.teamrise.adapttix;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class IndividualSummary extends FragmentActivity implements OnClickListener
{
	BufferedInputStream reader;
	BufferedOutputStream bos;

	char[] choices;
	Button[] buttons;
	int[] buttonIds;
	String[] leftIndividual = new String[20];
	String[] rightIndividual = new String[20];

	Bundle gotBundle;
	String gotName, gotFile;

	private Handler updater;

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
		gotBundle = getIntent().getExtras();
		gotName = gotBundle.getString("name");
		gotFile = gotBundle.getString("file");

		// Try to open the individual-evaluation file, get the user's choices
		// from the
		// file, and close the file.
		choices = new char[20];
		try
		{
			reader = new BufferedInputStream(openFileInput(gotFile));
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
		buttons = new Button[40];
		buttonIds = new int[40];
		for (int i = 0 , j = 0 , k = 0; i < 40; i++)
		{
			buttonIds[i] = getResources().getIdentifier("b" + i + "Review", "id", getPackageName());
			leftIndividual = getResources().getStringArray(R.array.IndividualLeft);
			rightIndividual = getResources().getStringArray(R.array.IndividualRight);
			
			buttons[i] = (Button) findViewById(buttonIds[i]);
			if(i % 2 == 0)
			{
			buttons[i].setText(leftIndividual[j]);
			j++;
			}
			else
			{
			buttons[i].setText(rightIndividual[k]);
			k++;
			}			
			
			buttons[i].setOnClickListener(this);
		}

		highlightButtons();

		updater = new Handler();
		final Runnable r = new Runnable()
		{
			public void run()
			{
				highlightButtons();
			}
		};

		Thread thread = new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					while (true)
					{
						sleep(500);
						updater.post(r);
					}
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		};

		thread.start();

	}

	/**
	 * Press the buttons that the user has chosen.
	 */
	private void highlightButtons()
	{
		int currChoice = 0;
		for (int i = 0; i < 20; i++)
		{
			if (choices[i] == 'L')
			{
				buttons[currChoice].setPressed(true);
				buttons[currChoice + 1].setPressed(false);
				buttons[currChoice + 1].setLongClickable(true);
			} else if (choices[i] == 'R')
			{
				buttons[currChoice].setPressed(false);
				buttons[currChoice].setLongClickable(true);
				buttons[currChoice + 1].setPressed(true);
			}
			currChoice = currChoice + 2;
		}
	}

	public void onClick(View v)
	{
		for (int i = 0; i < 40; i++)
		{
			if (v.getId() == buttonIds[i])
			{
				// buttons[i].setPressed(true);
				if (i % 2 == 0)
				{
					choices[i / 2] = 'L';
				} else
				{
					choices[(int) Math.floor(i / 2)] = 'R';
				}
			}
		}
	}

	/**
	 * When anything that has an OnLongClickListener set is clicked, this
	 * function runs.
	 */

	@Override
	protected void onStop()
	{
		super.onStop();

		// Open self-evaluation for writing.
		try
		{
			bos = new BufferedOutputStream(openFileOutput(gotFile, Context.MODE_PRIVATE));
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

	public static class IndividualEvaluationSummaryAlertDialogFragment extends DialogFragment
	{

		public static IndividualEvaluationSummaryAlertDialogFragment newInstance(String name,
				String description)
		{
			IndividualEvaluationSummaryAlertDialogFragment fragment = new IndividualEvaluationSummaryAlertDialogFragment();

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
			return new AlertDialog.Builder(getActivity()).setTitle(name).setMessage(description)
					.create();
		}
	}

}
