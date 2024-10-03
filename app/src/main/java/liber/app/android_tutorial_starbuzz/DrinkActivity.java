package liber.app.android_tutorial_starbuzz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLData;

public class DrinkActivity extends AppCompatActivity {

    protected static final String EXTRA_DRINK_NO = "EXTRA_DRINK_NO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        int drinkNo = (Integer) getIntent().getExtras().get(EXTRA_DRINK_NO);
        //Drink drink = Drink.drinks[drinkNo];



        try {
            SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
            SQLiteDatabase database = starbuzzDatabaseHelper.getWritableDatabase();
            Cursor cursor = database.query("DRINK",
                    new String[] {"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"},
                    "_id = ?",
                    new String[] {Integer.toString(drinkNo)},
                    null, null, null);

            if(cursor.moveToFirst()){
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);
                boolean isFavorite = (cursor.getInt(3) == 1);

                TextView name = findViewById(R.id.name);
                TextView description = findViewById(R.id.description);
                ImageView photo = findViewById(R.id.photo);
                CheckBox favorite = findViewById(R.id.favorite);

                name.setText(nameText);
                description.setText(descriptionText);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);
                favorite.setChecked(isFavorite);
            }
            cursor.close();
            database.close();
        } catch (SQLException e){
            Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    public void onFavoriteClicked(View view){
        int drinkNo = (Integer)getIntent().getExtras().get("EXTRA_DRINK_NO");
        new UpdateDrinkTask().execute(drinkNo);
    }

    private class UpdateDrinkTask extends AsyncTask<Integer, Void, Boolean> {
        ContentValues drinkValues;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CheckBox favorite = findViewById(R.id.favorite);
            drinkValues = new ContentValues();
            drinkValues.put("FAVORITE", favorite.isChecked());
        }

        @Override
        protected Boolean doInBackground(Integer... drinks) {
            int drinkNo = drinks[0];
            SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(DrinkActivity.this);

            try {
                SQLiteDatabase database = starbuzzDatabaseHelper.getWritableDatabase();
                database.update("DRINK", drinkValues,
                        "_id = ?", new String[] {Integer.toString(drinkNo)});
                database.close();
                return true;
            }catch (SQLException e){
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(!aBoolean){
                Toast.makeText(DrinkActivity.this, "Database unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }
}