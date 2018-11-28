package GSON;

import com.google.gson.annotations.SerializedName;

public class GSONLogin {
    @SerializedName("message")
    public String message;

    @SerializedName("code")
    public String code;

    @SerializedName("response")
    public String response;

    @SerializedName("sess_email")
    public String sess_email;
}
