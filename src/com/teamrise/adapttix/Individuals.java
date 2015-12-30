package com.teamrise.adapttix;

import java.util.ArrayList;
import java.util.Collections;

import android.R.color;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Individuals extends ListActivity implements OnItemLongClickListener
{
	ArrayList<String> individualsFileList, individualsNameList;
	String files[];
	String individualsName;
	ContextWrapper cw = new ContextWrapper(this);

	final CharSequence[] longClickChoice =
	{ "Reassess", "Delete" };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setVariables();
		setListAdapter(new ArrayAdapter<String>(Individuals.this,
				android.R.layout.simple_list_item_1, individualsNameList));

		ListView lv = getListView();
		lv.setBackgroundColor(Color.argb(100, 51, 255, 255));
		lv.setOnItemLongClickListener(this);
	}

	private void setVariables()
	{
		individualsFileList = new ArrayList<String>();
		individualsNameList = new ArrayList<String>();

		files = fileList();
		for (int i = 0; i < files.length; i++)
		{
			
			if (files[i].contains("_individual"))
			{
				individualsFileList.add(files[i]);

				individualsName = files[i].replace("_individual", "").replace("_", " ");
				capitalizeFirstLetters();
				individualsNameList.add(individualsName);
			}
		}

		Collections.sort(individualsFileList);
		Collections.sort(individualsNameList);
	}

	/**
	 * Capitalize the first first letters of all of the words in name.
	 */
	private void capitalizeFirstLetters()
	{
		String[] words = individualsName.split(" ");
		individualsName = "";
		String word;
		for (int i = 0; i < words.length; i++)
		{
			word = words[i].substring(0, 1).toUpperCase() + words[i].substring(1);
			individualsName = individualsName + " " + word.toString();
		}
		individualsName.trim();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);

		Bundle bundle = new Bundle();


		bundle.putString("filename", individualsFileList.get(position));
		Intent intent = new Intent("com.teamrise.adapttix.PRESCRIPTIONSCALCULATOR");
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public boolean onItemLongClick(AdapterView<?> arg0, View v, final int position, long id)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setItems(longClickChoice, new DialogInterface.OnClickListener()
		{

			public void onClick(DialogInterface dialog, int which)
			{
				switch(which)
				{
				case 0: editIndividual(position);
				break;
				case 1: deleteIndividual(position);
				break;
				}
			}
		})
		.create()
		.show();
		
		return false;
	}
	
	void deleteIndividual(int position)
	{
		String IndividualToDelete = individualsNameList.get(position);
		String FileToDelete = individualsFileList.get(position);
		boolean FileWasDeleted = cw.deleteFile(FileToDelete);

		if (FileWasDeleted)
		{
			Toast.makeText(getApplicationContext(), IndividualToDelete + " was deleted",
					Toast.LENGTH_SHORT).show();
		}

		// Refreshes Activity
		startActivity(getIntent());
		finish();
	}
	
	void editIndividual(int position)
	{
		String IndividualToEdit = individualsNameList.get(position);
		Intent intent = new Intent(Individuals.this,IndividualEvaluationExplanation.class);
		Bundle bundle = new Bundle();
		bundle.putString("Individual Name", IndividualToEdit.trim());
		intent.putExtras(bundle);
		startActivity(intent);
	}
}
