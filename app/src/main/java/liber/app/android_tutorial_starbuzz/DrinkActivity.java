package liber.app.android_tutorial_starbuzz;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DrinkActivity extends AppCompatActivity {

    protected static final String EXTRA_DRINK_NO = "EXTRA_DRINK_NO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        int drinkNo = (Integer) getIntent().getExtras().get(EXTRA_DRINK_NO);
        Drink drink = Drink.drinks[drinkNo];

        ImageView photo = findViewById(R.id.photo);
        photo.setImageResource(drink.getImageResource());
        photo.setContentDescription(drink.getDescription());

        TextView name = findViewById(R.id.name);
        name.setText(drink.getName());

        TextView description = findViewById(R.id.description);
        description.setText(drink.getDescription());
    }
}