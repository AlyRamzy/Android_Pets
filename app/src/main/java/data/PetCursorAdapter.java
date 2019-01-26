package data;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pets.R;

import org.w3c.dom.Text;

public class PetCursorAdapter extends CursorAdapter {

    public PetCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name=(TextView)view.findViewById(R.id.name);
        TextView summary=(TextView)view.findViewById(R.id.summary);
        int nameIndex=cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
        int breedIndex=cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED);
        String petName=cursor.getString(nameIndex);
        String petBreed= cursor.getString(breedIndex);
        name.setText(petName);
        if(TextUtils.isEmpty(petBreed))
            summary.setText("UnKnown Breed");
        else
            summary.setText(petBreed);


    }
}
