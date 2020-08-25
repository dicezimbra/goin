package br.com.legacy.utils;

import br.com.goin.base.BuildConfig;

/**
 * Created by AppSimples on 19/04/17.
 */

public class Constants {

    public static String GRAPHQL_BASE_URL = BuildConfig.BASE_URL;
    public static String COGNITO_IDENTITY_POOL_ID = br.com.BuildConfig.COGNITO_IDENTITY_POOL_ID;
    public static String COGNITO_USER_POOL_ID = br.com.BuildConfig.COGNITO_USER_POOL_ID;
    public static String COGNITO_CLIENT_ID = br.com.BuildConfig.COGNITO_CLIENT_ID;
    public static String COGNITO_CLIENT_SECRET = br.com.BuildConfig.COGNITO_CLIENT_SECRET;
    public static String COGNITO_IP_PREFIX = br.com.BuildConfig.COGNITO_IP_PREFIX;
    public static int TIMEOUT = 30; //seconds

    // PAYPAL
    public static String PAYPAL_APP_ID = "AYWplw8TFLYGF07bQgl1sPSGvhGqRulIK1Ty6uTI92y8ymUwT_I6OncOPhUOdiLQiRv97RS5TmeFhurC";
    public static String PAYPAL_APP_SECRET = "EEfhVhLoYvTIteFo79ckgkWTKIfVX28yowni7aKFhfZ1UkJf4eRFRNUpfdB4QfA0hk4dxwr2uHifRgvt";
    public static String COMPANY_NAME = "GO IN";

    public static String ACTIVITY_NAME_REGIONS = "";

    // SHARED PREFERENCES CONSTANTS
    public static String SP_USER = "USER";
    public static String SP_JUMPERS = "SHAREDPREFERENCES_JUMPERS";
    public static String FOLLOWERS_COUNT = "FOLLOWERS_COUNT";

    // AWS S3 BUCKET

    public static String BUCKET_NAME = br.com.BuildConfig.BUCKET_NAME;
    public static String BUCKET_PATH = br.com.BuildConfig.BUCKET_PATH;
    public static String IMAGE_EXT = ".jpeg";
    public static String VIDEO_EXT = ".mp4";
    public static String USERS_PATH = "Users";
    public static String POST_PATH = "Posts";
    public static String CHAT_PATH = "Chats";
    public static String GROUP_PATH = "Groups";

    // UBER
    public static String UBER_AUTH_TOKEN = "0rdzwbkE37saPIdLiKo-Co3njKIL3vvRSdyXaD93";
    public static String UBER_API_ENDPOINT = "https://api.uber.com";

    // IUGU
    public static String IUGU_API_ENDPOINT = "https://api.iugu.com";
    public static String IUGU_AUTH_TOKEN = "NWU1MDNlMGYwN2NiOTE1Y2NkNjg2OTFmMDYzZTgzNDk6";
    public static String IUGU_ACCOUNT_ID = "C449514687A248C8993849C7D12C5BEE";

    // INTENT BUNDLES
    public static String POST_ID = "POST_ID";
    public static String CLUB_ID = "CLUB_ID";
    public static String EVENT_ID = "EVENT_ID";
    public static String TICKET_ID = "TICKET_ID";
    public static String USER_ID = "USER_ID";
    public static String USER_NAME = "USER_NAME";
    public static String LAST_COMMENT_CONTENT = "LAST_COMMENT_CONTENT";
    public static String LAST_COMMENT_AUTHOR = "LAST_COMMENT_AUTHOR";
    public static String LAST_COMMENT_ID = "LAST_COMMENT_ID";
    public static String COMMENT_ADDED = "br.com.goin.Utils.COMMENT_ADDED";
    public static String COMMENT_COUNT = "COMMENT_COUNT";
    public static int LAST_COMMENT_REQUEST = 001;
    public static String START_DATE_CALENDAR = "StartDate";
    public static String END_DATE_CALENDAR = "EndDate";
    public static final int LIMIT = 25;
    public static final String CITY_FILTER = "CityFilter";
    public static final String STATE_FILTER = "StateFilter";
    public static int REQUEST_UPDATED_PROFILE = 80;
    public static final int REQUEST_TURN_ON_LOCATION = 007;
    public static String INGRESSE_TOKEN = "INGRESSE_TOKEN";
    public static String COUPON = "COUPON";
    public static String ORIGIN_TYPE = "ORIGIN_TYPE";
    public static String UPDATED_USER = "UPDATED_USER";
    public static String UPDATED_PROFILE_PICTURE = "UPDATED_USER_PROFILE_PICTURE";
    public static String RATING = "rating";
    public static String RATING_COUNT = "rating_count";
    public static String COMMENT = "COMMENT";
    public static String CATEGORIES_LIST = "CATEGORIES_LIST";
    public static String PRICE_RANGE = "PRICE_RANGE";
    public static String QUERY = "QUERY";
    public static String FILTER = "FILTER";
    public static String DATA = "DATA";
    public static String CAME_FROM_CLUB = "CAME_FROM_CLUB";
    public static String MUSIC_CATEGORIES_NAME = "MUSIC_CATEGORIES_NAME";
    public static String MUSIC_CATEGORIES_ID = "MUSIC_CATEGORIES_ID";
    public static String SELECTED_CATEGORIES = "SELECTED_CATEGORIES";
    public static int STAR_STATUS_VERIFICATION = 055;
    public static String EVENT_CONFIRMATION = "EVENT_CONFIRMATION";
    public static final String PROFILE_UPDATED = "br.com.goin.Utils.PROFILE_UPDATED";
    public static String FEELING = "FEELING";
    public static final int ID_FROM = 0;
    public static final int ID_TO = 1;
    public static String FEELING_DISPLAY_NAME = "FEELING_DISPLAY_NAME";
    public static String TICKETS = "TICKETS";
    public static String URL = "URL";
    public static String RECEIVED_NOTIFICATION = "notifications";
    public static String CHAT_ID = "CHAT_ID";
    public static String PHOTO_URLS = "PHOTO_URLS";
    public static String POSITION = "POSITION";
    public static String IS_CLUB_GALLERY = "IS_CLUB_GALLERY";
    public static String ACTION_TYPE = "ACTION_TYPE";
    public static String TABS_TYPE = "TABS_TYPE";
    public static String IS_EVENT_HAPPENING = "IS_EVENT_HAPPENING";
    public static String IS_GROUP_CONVERSATION = "IS_GROUP_CONVERSATION";
    public static String IS_EDIT_GROUP = "IS_EDIT_GROUP";
    public static String GROUP_ID = "GROUP_ID";
    public static String TICKET = "TICKET";
    public static final String ACTION_EDIT_GROUP = "ACTION_EDIT_GROUP";
    public static final String ACTION_LEAVE_GROUP = "ACTION_LEAVE_GROUP";
    public static String PAYMENT_METHOD = "PAYMENT_METHOD";
    public static String SELECTED_FRIENDS = "SELECTED_FRIENDS";
    public static String PURCHASE_MODEL = "PURCHASE_MODEL";
    public static String TOKEN = "TOKEN";
    public static String DISPLAY_NUMBER = "DISPLAY_NUMBER";
    public static String COMPONENT = "COMPONENT";
    public static String IS_PAST_EVENT = "IS_PAST_EVENT";
    public static String CATEGORY_ID = "CATEGORY_ID";
    public static String SUBCATEGORY_ID = "SUBCATEGORY_ID";
    public static String CATEGORY_NAME = "CATEGORY_NAME";
    public static String CATEGORY = "CATEGORY";
    public static String CATEGORY_TYPE = "CATEGORY_TYPE";
    public static String SEARCH_FILTERS = "SEARCH_FILTERS";
    public static String IS_FROM_PURCHASE_ACTIVITY = "IS_FROM_PURCHASE_ACTIVITY";
    public static String ENABLE_BUTTON = "ENABLE_BUTTON";
    public static String SHOW = "SHOW";
    public static final int UPDATE_FILTER = 8521;
    public static String SO_TRACK_BOA = "Só Track Boa";

    public static String INVINTED_BY_WITH_BRANCH_IO = "INVINTED_BY_WITH_BRANCH_IO";

    // SEARCH
    public static final String SEARCH_OPTION = "SEARCH_OPTION";
    public static final String SEARCH_EVENT = "SEARCH_EVENT";
    public static final String SEARCH_CLUB = "SEARCH_CLUB";
    public static final int REQUEST_SEARCH = 004;
    public static final int REQUEST_MUSIC_TYPE = 005;
    public static final int REQUEST_CLUB_TYPE = 006;
    public static final int MAX_NUMBER_WORDS_TO_CONSIDER = 5;
    public static final int MAX_TICKETS_VISIBLE_ON_MY_TICKETS = 2;
    // EVENT FILTER TYPE
    public static final String TODAY = "Today";
    public static final String TOMORROW = "Tomorrow";
    public static final String WEEKEND = "Weekend";
    public static final String ALL = "All";
    public static final String FUTURE = null;

    // EVENTS
    public static final String SHORT_EVENTS = "SHORT_EVENTS";
    public static final String MY_EVENTS = "MY_EVENTS";
    public static final String SUGGESTED_EVENTS = "SUGGESTED_EVENTS";
    public static int EVENT_COUNT = 20;
    public static final String EVENT = "EVENT";

    // POSTS
    public static int POST_COUNT = 20;
    public static final int PICK_LOCATION = 002;
    public static final int REQUEST_NEW_POST = 111;
    public static final int PICK_FEELING = 222;
    public static final int POST = 11;
    public static final int RAFFLE = 33;
    public static final String PICK_MEDIA = "PICK_MEDIA";
    public static final String PICK_IMAGE = "PICK_IMAGE";
    public static final String PICK_VIDEO = "PICK_VIDEO";

    // DETAIL EVENT
    public static final int EVENT_DETAIL = 003;

    // MAPS
    public static final double MIN_OFFSET = .99999;
    public static final double MAX_OFFSET = 1.00001;

    // CLUB DETAILS
    public static final int DESCRIPTION_MAX_LINES = 3;

    //PERMISSIONS
    public static final int REQUEST_PERMISSIONS_CALENDAR = 22;

    public static final Integer MAP_ZOOM = 12;

    public static final Integer MAX_SEARCH_PRICE = 500;

    // TERMS AND PRIVACY POLICY

    public static final String TERMS_OF_USE = "https://s3.amazonaws.com/goin-public/termos_de_uso.pdf";
    public static final String HALF_PRICE_POLICY = "https://goin.com.br/Politica_Meia_Entrada.pdf";
    public static final String PRIVACY_POLICY = "https://s3.amazonaws.com/goin-public/politica_de_privacidade.pdf";


    // FIREBASE CHAT PARAMETERS
    public static final String AUTHOR_ID = "authorId";
    public static final String AUTHOR_NAME = "authorName";
    public static final String DATE = "date";
    public static final String TYPE = "type";
    public static final String SELECTED_CATEGORIE = "SELECTED_CATEGORIE";
    public static final String MESSAGE_TEXT_TYPE = "message";
    public static final String MESSAGE_IMAGE_TYPE = "photo";
    public static final String MESSAGE = "message";
    public static final String PHOTO_URL = "photoUrl";
    public static final String DB_CHAT_ID = "chatId";
    public static final String DB_NAME = "name";
    public static final String DB_AVATAR = "avatar";
    public static final String DB_UNREAD_MESSAGES = "unreadMessages";
    public static final String DB_MEMBERS = "members";

    // HASH POST TAG


    // CREATE GROUP
    public static final Integer REQUEST_CREATE_GROUP = 13;
    public static final Integer REQUEST_EDIT_GROUP = 14;
    public static final Integer REQUEST_LEAVE_GROUP = 14;

    // SELECT TICKET OWNERS

    public static final Integer REQUEST_SELECTED_FRIENDS = 15;

    // GOOGLE ANALYTICS SCREENS NAMES
    public static final String ANALYTICS_FEED = "Feed";
    public static final String ANALYTICS_EVENTS = "Events";
    public static final String ANALYTICS_CONNECTIONS = "Connections";
    public static final String ANALYTICS_CHAT = "Chat";
    public static final String ANALYTICS_PROFILE = "Profile";
    public static final String ANALYTICS_SEARCH = "Search";
    public static String SHARED_PREFERENCES = "SHARED_PREFERENCES_GOIN";
    // CACHE CONSTANTS
//    public static final String

    // GENERAL
    public static final String EVENT_BANNER_DAFAULT_HOME = "https://s3.amazonaws.com/goin-public/resources/goin/images/bunner/banner-home-app.png";
    public static final String GOIN_LOGO_SHARE = "https://s3.amazonaws.com/goin-public/resources/goin/images/logos/logo_goin.png";
    public static final String PROFILE_PLACE_HOLDER = "PROFILE";
    public static final String WALLET_PLACE_HOLDER = "WALLET";
    public static final String FEED_PLACE_HOLDER = "FEED";


    // MOVIES
    public static final String MOVIE_BUTTON_SELECTOR = "Filme";
    public static final String MOVIE_TYPE = "Movie";
    public static final String THEATER_PLAY_TYPE = "Play";
    public static String PLAY_TYPE = "PLAY_TYPE";
    public static String PLAY_ID = "PLAY_ID";
    public static String MOVIE_TITLE = "MOVIE_TITLE";


}
