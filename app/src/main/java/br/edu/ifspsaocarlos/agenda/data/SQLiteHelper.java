package br.edu.ifspsaocarlos.agenda.data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "agenda.db";
    static final String DATABASE_TABLE = "contatos";
    static final String KEY_ID = "id";
    static final String KEY_NAME = "nome";
    static final String KEY_FONE = "fone";
    static final String KEY_EMAIL = "email";
    static final String KEY_FAVORITE = "favorito";
    static final String KEY_CELULAR = "celular";
    static final String KEY_DIAANIVERSARIO = "dia";
    static final String KEY_MESANIVERSARIO = "mes";
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_CREATE = "CREATE TABLE "+ DATABASE_TABLE +" (" +
            KEY_ID  +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_NAME + " TEXT NOT NULL, " +
            KEY_FONE + " TEXT, "  +
            KEY_EMAIL + " TEXT); "; //+
            //KEY_FAVORITE + " INTEGER," +
            //KEY_CELULAR + " TEXT," +
            //KEY_DIAANIVERSARIO + " INTEGER," +
            //KEY_MESANIVERSARIO + " INTEGER);";


    SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        Log.e("DataBaseUpgrade", "Updating table from " + oldVersion + " to " + newVersion);

        if (oldVersion < 2){
            database.execSQL("ALTER TABLE " + DATABASE_TABLE  + " ADD COLUMN "+ KEY_FAVORITE +" INTEGER;");
        }

        if (oldVersion < 3){
            database.execSQL("ALTER TABLE " + DATABASE_TABLE  + " ADD COLUMN "+ KEY_CELULAR +" INTEGER;");
        }

        if (oldVersion <= 4){
            database.execSQL("ALTER TABLE " + DATABASE_TABLE  + " ADD COLUMN "+ KEY_DIAANIVERSARIO+" INTEGER;");
            database.execSQL("ALTER TABLE " + DATABASE_TABLE  + " ADD COLUMN "+ KEY_MESANIVERSARIO +" INTEGER;");
        }

    }
}

