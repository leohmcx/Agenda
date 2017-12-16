package br.edu.ifspsaocarlos.agenda.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.edu.ifspsaocarlos.agenda.model.Contato;
import java.util.ArrayList;
import java.util.List;

public class ContatoDAO {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    public ContatoDAO(Context context) {
        this.dbHelper = new SQLiteHelper(context);
    }

    public List<Contato> buscaTodosContatos() {
        database = dbHelper.getReadableDatabase();
        List<Contato> contatos = new ArrayList<>();
        Cursor cursor;
        String[] cols = new String[]{SQLiteHelper.KEY_ID, SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_FONE, SQLiteHelper.KEY_EMAIL, SQLiteHelper.KEY_FAVORITE, SQLiteHelper.KEY_CELULAR, SQLiteHelper.KEY_DIAANIVERSARIO, SQLiteHelper.KEY_MESANIVERSARIO};
        cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, null, null,null, null, SQLiteHelper.KEY_NAME);
        while (cursor.moveToNext()) {
            Contato contato = new Contato();
            contato.setId(cursor.getInt(0));
            contato.setNome(cursor.getString(1));
            contato.setFone(cursor.getString(2));
            contato.setEmail(cursor.getString(3));
            contato.setFavorito(cursor.getInt(4));
            contato.setCelular(cursor.getString(5));
            contato.setDiaAniversario(cursor.getInt(6));
            contato.setMesAniversario(cursor.getInt(7));
            contatos.add(contato);
        }
        cursor.close();
        database.close();
        return contatos;
    }

    public List<Contato> buscaContato(String nome) {
        database = dbHelper.getReadableDatabase();
        List<Contato> contatos = new ArrayList<>();
        Cursor cursor;
        String[] cols = new String[]{SQLiteHelper.KEY_ID, SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_FONE, SQLiteHelper.KEY_EMAIL, SQLiteHelper.KEY_FAVORITE, SQLiteHelper.KEY_CELULAR, SQLiteHelper.KEY_DIAANIVERSARIO, SQLiteHelper.KEY_MESANIVERSARIO};
        String where = SQLiteHelper.KEY_NAME + " like ? OR " + SQLiteHelper.KEY_EMAIL + " like ?";
        String[] argWhere = new String[]{nome + "%", nome + "%"};
        cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, where, argWhere,null, null, SQLiteHelper.KEY_NAME);
        while (cursor.moveToNext()) {
            Contato contato = new Contato();
            contato.setId(cursor.getInt(0));
            contato.setNome(cursor.getString(1));
            contato.setFone(cursor.getString(2));
            contato.setEmail(cursor.getString(3));
            contato.setFavorito(cursor.getInt(4));
            contato.setCelular(cursor.getString(5));
            contato.setDiaAniversario(cursor.getInt(6));
            contato.setMesAniversario(cursor.getInt(7));
            contatos.add(contato);
        }
        cursor.close();
        database.close();
        return contatos;
    }

    public List<Contato> buscaContatoFavorito(int favorito) {
        database = dbHelper.getReadableDatabase();
        List<Contato> contatos = new ArrayList<>();
        Cursor cursor;
        String[] cols = new String[]{SQLiteHelper.KEY_ID, SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_FONE, SQLiteHelper.KEY_EMAIL, SQLiteHelper.KEY_FAVORITE, SQLiteHelper.KEY_CELULAR, SQLiteHelper.KEY_DIAANIVERSARIO, SQLiteHelper.KEY_MESANIVERSARIO};
        String where = SQLiteHelper.KEY_FAVORITE + " = ? ";
        String[] argWhere = new String[]{favorito + ""};
        cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, where, argWhere,null, null, SQLiteHelper.KEY_NAME);
        while (cursor.moveToNext()) {
            Contato contato = new Contato();
            contato.setId(cursor.getInt(0));
            contato.setNome(cursor.getString(1));
            contato.setFone(cursor.getString(2));
            contato.setEmail(cursor.getString(3));
            contato.setFavorito(cursor.getInt(4));
            contato.setCelular(cursor.getString(5));
            contato.setDiaAniversario(cursor.getInt(6));
            contato.setMesAniversario(cursor.getInt(7));
            contatos.add(contato);
        }
        cursor.close();
        database.close();
        return contatos;
    }

    public void salvaContato(Contato c) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.KEY_NAME, c.getNome());
        values.put(SQLiteHelper.KEY_FONE, c.getFone());
        values.put(SQLiteHelper.KEY_EMAIL, c.getEmail());
        values.put(SQLiteHelper.KEY_FAVORITE, c.getFavorito());
        values.put(SQLiteHelper.KEY_CELULAR, c.getCelular());
        values.put(SQLiteHelper.KEY_DIAANIVERSARIO, c.getDiaAniversario());
        values.put(SQLiteHelper.KEY_MESANIVERSARIO, c.getMesAniversario());
        if (c.getId() > 0)
            database.update(SQLiteHelper.DATABASE_TABLE, values, SQLiteHelper.KEY_ID + "=" + c.getId(), null);
        else
            database.insert(SQLiteHelper.DATABASE_TABLE, null, values);
        database.close();
    }

    public void apagaContato(Contato c) {
        database = dbHelper.getWritableDatabase();
        database.delete(SQLiteHelper.DATABASE_TABLE, SQLiteHelper.KEY_ID + "=" + c.getId(), null);
        database.close();
    }
}
