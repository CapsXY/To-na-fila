package com.unigranrio.tonafilavf.activity.cliente;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.unigranrio.tonafilavf.R;
import com.unigranrio.tonafilavf.activity.comum.SobreActivity;
import com.unigranrio.tonafilavf.adapter.AdapterEmpresa;
import com.unigranrio.tonafilavf.helper.ConfiguracaoFirebase;
import com.unigranrio.tonafilavf.listener.RecyclerItemClickListener;
import com.unigranrio.tonafilavf.model.Empresa;

import java.util.ArrayList;
import java.util.List;

public class UsuarioActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private RecyclerView recyclerViewEmpresa;
    private AdapterEmpresa adapterEmpresa;
    private List<Empresa> empresas = new ArrayList<>();
    private DatabaseReference firebaseRef;
    private MaterialSearchView searchView;
    private SensorManager sensorManager;
    private float acelVal;
    private float acelLast;
    private float shake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;

        inicializarComponentes();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("TôNaFila!");
        setSupportActionBar(toolbar);

        recyclerViewEmpresa.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEmpresa.setHasFixedSize(true);
        adapterEmpresa = new AdapterEmpresa(empresas);
        recyclerViewEmpresa.setAdapter(adapterEmpresa);

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

        recyclerViewEmpresa.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerViewEmpresa, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Empresa empresaSelecionada = empresas.get(position);
                        Intent i = new Intent(UsuarioActivity.this, PaginaActivity.class);
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

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            acelLast = acelVal;
            acelVal = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = acelVal - acelLast;
            shake = shake * 0.01f + delta;

            if (shake > 12) {
                Toast toast = Toast.makeText(getApplicationContext(), "Que desagradável!", Toast.LENGTH_SHORT);
                toast.show();
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

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
        recyclerViewEmpresa = findViewById(R.id.recyclerViewEmpresa);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_usuario, menu);

        MenuItem item = menu.findItem(R.id.menuPesquisa);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menuFila:
                abrirFila();
                break;

            case R.id.menuHistorico:
                abrirHistorico();
                break;

            case R.id.menuPerfil:
                abrirPerfil();
                break;

            case R.id.menuSobre:
                abrirSobre();
                break;

            case R.id.menuSair:
                deslogarUsuario();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void abrirFila() {
        startActivity(new Intent(UsuarioActivity.this, FilaUsuarioActivity.class));
    }

    private void abrirHistorico() {
        startActivity(new Intent(UsuarioActivity.this, HistoricoActivity.class));
    }

    private void abrirPerfil() {
        startActivity(new Intent(UsuarioActivity.this, PerfilActivity.class));
    }

    private void abrirSobre() {
        startActivity(new Intent(UsuarioActivity.this, SobreActivity.class));
    }

    private void deslogarUsuario() {
        try {
            autenticacao.signOut();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}