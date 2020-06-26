package glerbkalachev.supercrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import glerbkalachev.supercrud.model.Contact;
import glerbkalachev.supercrud.model.ContactLab;

public class MainActivity extends AppCompatActivity {

    RecyclerView theRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        theRecyclerView = findViewById(R.id.contacts_recycler_view);

//        Contact newContact = new Contact();
//        newContact.mFio = "Глебка Романыч";
//        newContact.mPhone = "02";
//        newContact.mEmail = "megas781@gmail.com";

//        ContactLab.get(this).addContact(newContact);

        Log.e("mytag","fetched fio: " + ContactLab.get(this).getContactList().get(0).mFio);

    }


    class ContactHolder extends RecyclerView.ViewHolder {
        public ContactHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class ContactHolderAdapter extends RecyclerView.Adapter<ContactHolder> {

        @NonNull
        @Override
        public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull ContactHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }


}
