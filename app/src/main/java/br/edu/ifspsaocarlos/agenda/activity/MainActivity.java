package br.edu.ifspsaocarlos.agenda.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.agenda.R;
import br.edu.ifspsaocarlos.agenda.adapter.ContatoAdapter;
import br.edu.ifspsaocarlos.agenda.data.ContatoDAO;
import br.edu.ifspsaocarlos.agenda.model.Contato;


public class MainActivity extends AppCompatActivity {

    private ContatoDAO cDAO;
    private RecyclerView recyclerView;

    private List<Contato> contatos = new ArrayList<>();
    private TextView empty;

    private ContatoAdapter adapter;
    private SearchView searchView;

    private FloatingActionButton fab;
    private int filtroFavorito;

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {

            searchView.onActionViewCollapsed();
            updateUI(null, 0);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchView.clearFocus();
            updateUI(query, 0);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        handleIntent(intent);

        cDAO = new ContatoDAO(this);

        empty = (TextView) findViewById(R.id.empty_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);

        adapter = new ContatoAdapter(contatos, this);
        recyclerView.setAdapter(adapter);

        setupRecyclerView();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DetalheActivity.class);
                startActivityForResult(i, 1);
            }
        });

        updateUI(null, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.pesqContato).getActionView();

        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.search_src_text);
                if (et.getText().toString().isEmpty())
                    searchView.onActionViewCollapsed();

                searchView.setQuery("", false);
                updateUI(null, 0);
            }
        });

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1)
            if (resultCode == RESULT_OK) {
                showSnackBar(getResources().getString(R.string.contato_adicionado));
                updateUI(null, 0);
            }

        if (requestCode == 2) {
            if (resultCode == RESULT_OK)
                showSnackBar(getResources().getString(R.string.contato_alterado));
            if (resultCode == 3)
                showSnackBar(getResources().getString(R.string.contato_apagado));

            updateUI(null, 0);
        }
    }

    private void showSnackBar(String msg) {
        CoordinatorLayout coordinatorlayout = (CoordinatorLayout) findViewById(R.id.coordlayout);
        Snackbar.make(coordinatorlayout, msg,
                Snackbar.LENGTH_LONG)
                .show();
    }


    private void updateUI(String nomeContato, int favorito) {

        contatos.clear();

        if (favorito == 1) {
            contatos.addAll(cDAO.buscaContatoFavorito(favorito));
        } else if (nomeContato == null) {
            contatos.addAll(cDAO.buscaTodosContatos());
            empty.setText(getResources().getString(R.string.lista_vazia));
            fab.setVisibility(View.VISIBLE);
        } else {
            contatos.addAll(cDAO.buscaContato(nomeContato));
            empty.setText(getResources().getString(R.string.contato_nao_encontrado));
            fab.setVisibility(View.GONE);
        }

        recyclerView.getAdapter().notifyDataSetChanged();

        if (recyclerView.getAdapter().getItemCount() == 0)
            empty.setVisibility(View.VISIBLE);
        else
            empty.setVisibility(View.GONE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pesqFavoritos:
                if (filtroFavorito == 0) {
                    updateUI(null, 1);
                    item.setIcon(getResources().getDrawable(R.drawable.not_favorite));
                    filtroFavorito = 1;
                } else {
                    updateUI(null, 0);
                    item.setIcon(getResources().getDrawable(R.drawable.favorite_user));
                    filtroFavorito = 0;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView() {


        adapter.setClickListener(new ContatoAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                final Contato contato = contatos.get(position);
                Intent i = new Intent(getApplicationContext(), DetalheActivity.class);
                i.putExtra("contato", contato);
                startActivityForResult(i, 2);
            }
        });

        adapter.setClickListenerFavoritar(new ContatoAdapter.ItemClickListenerFavoritar() {
            @Override
            public void onItemClickFavoritar(int position) {
                final Contato contato = contatos.get(position);

                if (contato.getFavorito() == 0) {
                    contato.setFavorito(1);
                } else {
                    contato.setFavorito(0);
                }
                Log.i("teste", String.valueOf(contato.getFavorito()));
                cDAO.salvaContato(contato);
                updateUI(null, 0);

            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                if (swipeDir == ItemTouchHelper.RIGHT) {
                    Contato contato = contatos.get(viewHolder.getAdapterPosition());
                    cDAO.apagaContato(contato);
                    contatos.remove(viewHolder.getAdapterPosition());
                    recyclerView.getAdapter().notifyDataSetChanged();
                    showSnackBar(getResources().getString(R.string.contato_apagado));
                    updateUI(null, 0);
                }
            }


            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                Paint p = new Paint();
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(ContextCompat.getColor(getBaseContext(), R.color.colorDelete));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_remove);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }


        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


    }

}
