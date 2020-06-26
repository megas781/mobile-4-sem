package glerbkalachev.supercrud;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import glerbkalachev.supercrud.model.Contact;
import glerbkalachev.supercrud.model.ContactLab;

public class MainActivity extends AppCompatActivity {

    //Код для детального контакта
    private static final int CONTACT_DETAIL_REQUEST_CODE = 1;
    //Код для нового контакта
    private static final int CONTACT_NEW_REQUEST_CODE = 2;

    //Экземпляр recyclerView
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

//        Log.e("mytag","fetched fio: " + ContactLab.get(this).getContactList().get(0).mFio);

        theRecyclerView = findViewById(R.id.contacts_recycler_view);
        theRecyclerView.setAdapter(new ContactHolderAdapter(ContactLab.get(this).getContactList()));
        theRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void onAddButtonTapped(View view) {
        Intent i = new Intent(this, EditActivity.class);
        startActivityForResult(i, CONTACT_NEW_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("mytag", "onActivityResult: ");
        ((ContactHolderAdapter) this.theRecyclerView.getAdapter()).setContactsList(ContactLab.get(this).getContactList());
    }

    class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected Contact mContact;
        protected TextView mFioTextView;
        protected TextView mPhoneTextView;
        protected TextView mEmailTextView;


        public ContactHolder(@NonNull View itemView) {
            super(itemView);

            mFioTextView = itemView.findViewById(R.id.fio_text_view);
            mPhoneTextView = itemView.findViewById(R.id.phone_text_view);
            mEmailTextView = itemView.findViewById(R.id.email_text_view);

            itemView.setOnClickListener(this);


        }

        public void bind(Contact contact) {

            mContact = contact;
            mFioTextView.setText(mContact.mFio);
            mPhoneTextView.setText(mContact.mPhone);
            mEmailTextView.setText(mContact.mEmail);

        }

        /**
         * Реагирует на нажатие на самого себя
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            //здесь будет intent
            Intent i = new Intent(MainActivity.this, EditActivity.class);
            i.putExtra("contact_id", mContact.getId().toString());
            startActivityForResult(i, CONTACT_DETAIL_REQUEST_CODE);
        }

    }

    class ContactHolderAdapter extends RecyclerView.Adapter<ContactHolder> {

        public ArrayList<Contact> mContactList;

        public void setContactsList(ArrayList<Contact> list) {
            this.mContactList = list;
            this.notifyDataSetChanged();
        }

        //Адаптер принимает на вход список контактов
        public ContactHolderAdapter(ArrayList<Contact> contactList) {
            mContactList = contactList;
        }

        @NonNull
        @Override
        public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            //inflate'им layout ячейки
            View v = layoutInflater.inflate(R.layout.cell_contact, parent, false);
            //Возвращаем инициализируемый contactHolder с этой view'шкой
            return new ContactHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
            holder.bind(mContactList.get(position));
        }

        @Override
        public int getItemCount() {
            return mContactList.size();
        }
    }


}
