package com.tistory.puzzleleaf.rankofalcohol.fb;

import com.google.firebase.auth.FirebaseAuth;
import com.tistory.puzzleleaf.rankofalcohol.model.FbUser;

/**
 * Created by cmtyx on 2017-07-25.
 */

public class FbAuth {
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FbUser mUser;

    public static String getGoogleUserId(){
        String result;
        try {
            result = mAuth.getCurrentUser().getUid();
        } catch (NullPointerException e){
            result = "public";
        }
        return result;
    }

    public static String getGoogleUserName(){
        String result;
        try {
            result = mAuth.getCurrentUser().getDisplayName();
        } catch (NullPointerException e){
            result = "사용자";
        }
        return result;
    }
}
