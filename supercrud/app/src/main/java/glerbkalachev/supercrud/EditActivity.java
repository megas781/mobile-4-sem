package glerbkalachev.supercrud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.UUID;

import glerbkalachev.supercrud.model.Contact;
import glerbkalachev.supercrud.model.ContactLab;

public class EditActivity extends AppCompatActivity {

    EditText mFioEditText;
    EditText mPhoneEditText;
    EditText mEmailEditText;

    Contact mContact;
    boolean makingNewContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        this.mFioEditText = findViewById(R.id.fio_text_edit);
        this.mFioEditText.addTextChangedListener(mTextWatcher);
        this.mPhoneEditText = findViewById(R.id.phone_text_edit);
        this.mEmailEditText = findViewById(R.id.email_text_edit);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            makingNewContact = false;
            //Если передали id, то редактируем существующий
            UUID contactId = UUID.fromString(extras.getString("contact_id"));
            mContact = ContactLab.get(this).getContact(contactId);
        } else {
            //иначе создаем новый контакт
            makingNewContact = true;
            mContact = new Contact();
        }

        setResult(RESULT_CANCELED);
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            findViewById(R.id.save_button_id).setEnabled(
                    EditActivity.this.mFioEditText.getText().toString().length()  != 0);
        }
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void afterTextChanged(Editable s) {}
    };

    public void saveButtonTapped(View view) {
        setResult(RESULT_OK);

        mContact.mFio = mFioEditText.getText().toString();
        mContact.mPhone = mPhoneEditText.getText().toString();
        mContact.mEmail = mEmailEditText.getText().toString();

        if (makingNewContact) {
            ContactLab.get(this).addContact(mContact);
        } else {
            ContactLab.get(this).updateContact(mContact);
        }
        finish();
    }
}
