package xyz.jncode.manufriend.Share;

public class Api {

    public static final String BASE_URL = "http://flbackend.jn-code.xyz/index.php/";


    public static final String GET_LOGIN = BASE_URL + "api/v1/auth/";
    public static final String GET_PRICE_SERVICE = BASE_URL + "api/v1/pricetag/";
    public static final String GET_USER_BY_ID = BASE_URL + "api/v1/getuser/";
    public static final String GET_RIWAYAT = BASE_URL + "api/v1/riwayat/";
    public static final String GET_NOTIFIKASI = BASE_URL + "api/v1/ongoing/";
    public static final String GET_ITEM_RIWAYAT = BASE_URL + "api/v1/itemriwayat/";

    public static final String POST_TRX= BASE_URL + "api/v1/transaction/";
    public static final String POST_REGISTER= BASE_URL + "api/v1/register/";
    public static final String POST_UPDATE_USER= BASE_URL + "api/v1/updateuser/";





    public static final String PARAM_ID_USER="manf_id_user";
    public static final String PARAM_TARGET_RIWAYAT="riwayat";




}
