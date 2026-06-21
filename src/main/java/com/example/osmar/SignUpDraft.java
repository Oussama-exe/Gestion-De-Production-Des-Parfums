package com.example.osmar;

/**
 * Holds the email/password collected in SignUpStepOneView while the
 * user fills in nom/prénom on SignUpStepTwoView. Kept separate from
 * Session (which holds the fully-authenticated Client) since this is
 * just transient draft data for an account that doesn't exist yet.
 */
public final class SignUpDraft {

    private static String email;
    private static String pwd;

    private SignUpDraft() {
    }

    public static void setCredentials(String email, String pwd) {
        SignUpDraft.email = email;
        SignUpDraft.pwd = pwd;
    }

    public static String getEmail() {
        return email;
    }

    public static String getPwd() {
        return pwd;
    }

    public static void clear() {
        email = null;
        pwd = null;
    }
}
