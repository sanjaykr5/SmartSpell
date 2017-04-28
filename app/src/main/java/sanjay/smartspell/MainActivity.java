package sanjay.smartspell;

import android.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements
        LoaderManager.Loade  rCallbacks<Cursor> {
/** Identifier for the pet data loader */
private static final int PET_LOADER = 0;

        /** Adapter for the ListView */
        CursorAdapter mCursorAdapter;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, Text.class);
        startActivity(intent);
        }
        });

        // Find the ListView which will be populated with the pet data
        ListView petListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);
        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.

        // Setup an Adapter to create a list item for each row of pet data in the Cursor.
        // There is no pet data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new CursorAdapter(this, null);
        petListView.setAdapter(mCursorAdapter);

        // Setup the item click listener
        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
@Override
public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        // Create new intent to go to {@link EditorActivity}
        Intent intent = new Intent(MainActivity.this, Text.class);

        // Form the content URI that represents the specific pet that was clicked on,
        // by appending the "id" (passed as input to this method) onto the
        // {@link PetEntry#CONTENT_URI}.
        // For example, the URI would be "content://com.example.android.pets/pets/2"
        // if the pet with ID 2 was clicked on.
        Uri currentPetUri = ContentUris.withAppendedId(Contract.DataEntry.CONTENT_URI, id);

        // Set the URI on the data field of the intent
        intent.setData(currentPetUri);

        // Launch the {@link EditorActivity} to display the data for the current pet.
        startActivity(intent);
        }
        });

        // Kick off the loader
        getLoaderManager().initLoader(PET_LOADER, null, this);
        }

/**
 * Helper method to insert hardcoded pet data into the database. For debugging purposes only.
 */
 /*   private void insertPet() {
        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put(Contract.DataEntry.StoryName, "Toto");
        values.put(Contract.DataEntry.story_text, "Terrier");

        // Insert a new row for Toto into the provider using the ContentResolver.
        // Use the {@link PetEntry#CONTENT_URI} to indicate that we want to insert
        // into the pets database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.
        Uri newUri = getContentResolver().insert(Contract.DataEntry.CONTENT_URI, values);
    }*/

@Override
public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
        Contract.DataEntry._ID,
        Contract.DataEntry.StoryName,
        Contract.DataEntry.story_text };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
        Contract.DataEntry.CONTENT_URI,   // Provider content URI to query
        projection,             // Columns to include in the resulting Cursor
        null,                   // No selection clause
        null,                   // No selection arguments
        null);                  // Default sort order
        }

@Override
public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link PetCursorAdapter} with this new cursor containing updated pet data
        mCursorAdapter.swapCursor(data);
        }

@Override
public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
        }
        }