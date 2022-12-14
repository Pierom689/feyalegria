package com.example.feyalegria;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.feyalegria.adapter.horarioAdapter;
import com.example.feyalegria.databinding.ActivityHorariosBinding;
import com.example.feyalegria.model.Horarios;
import com.example.feyalegria.retroit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HorariosActivity extends AppCompatActivity implements View.OnClickListener {
    public int iddocente;
    Bundle parametros;
    private static String TAG = "Horarios";
    private ActivityHorariosBinding binding;
    horarioAdapter adapter;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;


    List<Horarios> listahorarios = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHorariosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnvolver.setOnClickListener(this);
        //Agregados para realizar el get
        recyclerView = binding.rvhorario;
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter =  new horarioAdapter(listahorarios);
        recyclerView.setAdapter(adapter);

        String usuario;
        parametros = this.getIntent().getExtras();
        if(parametros != null) {
            usuario = parametros.getString("usuario");
            binding.txtdocenteh.setText(usuario.toUpperCase());}

        //Configuracion de Parametros
        parametros = this.getIntent().getExtras();
        if(parametros != null){
            iddocente = parametros.getInt("iddocente");
        }
        cargarDatosHo(iddocente);

    }

    private void cargarDatosHo(int iddocente) {
        Call<List<Horarios>>call =  RetrofitClient.getRetrofitCliente().obtenerListaHorarios(iddocente);
        call.enqueue(new Callback<List<Horarios>>() {
            @Override
            public void onResponse(Call<List<Horarios>> call, Response<List<Horarios>> response) {
                try{
                    if(response.isSuccessful() && response.body() != null){
                        listahorarios.addAll(response.body());
                        adapter.notifyDataSetChanged();
                        Log.e(TAG," TODO BIEN EN HORARIOS: "+response.body());
                    }else{
                        Log.e(TAG," HAY UN ERROR EN HORARIOS: "+response.body());
                    }
                }catch (Exception e){
                    Log.e(TAG," TODOD MAL: "+response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Horarios>> call, Throwable t) {
                Toast.makeText(HorariosActivity.this, "Error: " +t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnvolver: iraMenu();
                break;
        }
    }
    private void iraMenu(){
        Intent intentMenu = new Intent(this,MenuActivity.class);
        intentMenu.putExtras(parametros);
        startActivity(intentMenu);
    }
}