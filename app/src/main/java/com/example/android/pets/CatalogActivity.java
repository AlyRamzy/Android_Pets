/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import data.PetContract;
import data.PetCursorAdapter;
import data.PetDbHelper;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private PetDbHelper mDbHelper;
    private static final int PET_LOADER=0;

    PetCursorAdapter mCursorAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);


        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        ListView listView=(ListView)findViewById(R.id.list);
        View emptyView =findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);
        mCursorAdapter=new PetCursorAdapter(this,null);
        listView.setAdapter(mCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent(CatalogActivity.this,EditorActivity.class);
                Uri uri= ContentUris.withAppendedId(PetContract.PetEntry.CONTENT_URI,l);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(PET_LOADER,null,this);
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */

    public void AddPet(){
        ContentValues contentValues=new ContentValues();
        contentValues.put(PetContract.PetEntry.COLUMN_PET_NAME,"Toto");
        contentValues.put(PetContract.PetEntry.COLUMN_PET_BREED,"Terrier");
        contentValues.put(PetContract.PetEntry.COLUMN_PET_GENDER, PetContract.PetEntry.GENDER_MALE);
        contentValues.put(PetContract.PetEntry.COLUMN_PET_WEIGHT,15);
        Uri newUri=getContentResolver().insert(PetContract.PetEntry.CONTENT_URI,contentValues);


    }
    /**
     * Helper method to delete all pets in the database.
     */
    private void deleteAllPets() {
        int rowsDeleted = getContentResolver().delete(PetContract.PetEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                AddPet();
                // Do nothing for now
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllPets();
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection= {PetContract.PetEntry._ID, PetContract.PetEntry.COLUMN_PET_NAME, PetContract.PetEntry.COLUMN_PET_BREED};
        return new CursorLoader(this, PetContract.PetEntry.CONTENT_URI,projection,null,null,null);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }
}
