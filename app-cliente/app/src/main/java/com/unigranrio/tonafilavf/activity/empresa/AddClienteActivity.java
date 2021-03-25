package com.unigranrio.tonafilavf.activity.empresa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.unigranrio.tonafilavf.R;
import com.unigranrio.tonafilavf.activity.cliente.PaginaActivity;
import com.unigranrio.tonafilavf.adapter.AdapterEmpresa;
import com.unigranrio.tonafilavf.helper.ConfiguracaoFirebase;
import com.unigranrio.tonafilavf.listener.RecyclerItemClickListener;
import com.unigranrio.tonafilavf.model.Empresa;

import java.util.ArrayList;
import java.util.List;

public class AddClienteActivity extends AppCompatActivity {

    private RecyclerView recyclerViewEmpresa1;
    private AdapterEmpresa adapterEmpresa;
    private List<Empresa> empresas = new ArrayList<>();
    private DatabaseReference firebaseRef;
    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cliente);

        inicializarComponentes();
        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("TôNaFila! - Empresas");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerViewEmpresa1.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEmpresa1.setHasFixedSize(true);
        adapterEmpresa = new AdapterEmpresa(empresas);
        recyclerViewEmpresa1.setAdapter(adapterEmpresa);

        recuperarEmpresas();

        searchView.setHint("Pesquisar restaurantes");
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pesquisarEmpresas(newText);
                return true;
            }
        });

        recyclerViewEmpresa1.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerViewEmpresa1, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Empresa empresaSelecionada = empresas.get(position);
                        Intent i = new Intent(AddClienteActivity.this, PaginaActivity.class);
                        i.putExtra("empresa", empresaSelecionada);
                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    }
                }
                )
        );
    }

    private void pesquisarEmpresas(String pesquisa) {

        DatabaseReference empresasRef = firebaseRef
                .child("empresas");

        Query query = empresasRef.orderByChild("nome").startAt(pesquisa).endAt(pesquisa + "\uf8ff");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                empresas.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    empresas.add(ds.getValue(Empresa.class));
                }
                adapterEmpresa.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void recuperarEmpresas() {

        DatabaseReference empresaRef = firebaseRef
                .child("empresas");
        empresaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                empresas.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    empresas.add(ds.getValue(Empresa.class));
                }
                adapterEmpresa.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void inicializarComponentes() {
        searchView = findViewById(R.id.materialSearchView);
        recyclerViewEmpresa1 = findViewById(R.id.recyclerViewEmpresa1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);

        MenuItem item = menu.findItem(R.id.menuPesquisa);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }
}
