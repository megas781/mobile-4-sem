package glerbkalachev.supercrud.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.UUID;

public class Contact {
    public UUID getId() {
        return mId;
    }
    private UUID mId;
//    @NonNull
    public String mFio = "";
//    @NonNull
    public String mPhone = "";
//    @NonNull
    public String mEmail = "";

    public Contact() {
        this.mId = UUID.randomUUID();
    }
    public Contact(UUID uuid) {
        this.mId = uuid;
    }
}
