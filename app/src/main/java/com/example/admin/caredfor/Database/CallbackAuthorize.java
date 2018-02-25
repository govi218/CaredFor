package com.example.admin.caredfor.Database;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by Gov on 7/25/2017.
 *
 */

public interface CallbackAuthorize {

    int authorizeCaretaker(DataSnapshot dataSnapshot);
    int authorizeRelative(DataSnapshot dataSnapshot);
    void authorizeFail();
    void userNotFound();

}