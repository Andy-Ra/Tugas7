package com.andyra.tugas7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class Tambah_Data extends Fragment {
    private TextInputLayout ed_nama, ed_jenis, ed_stok;
    private String nama, jenis, stok;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_tambah_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.tambah_barang);

        Button btnadd = getActivity().findViewById(R.id.btnadd);
        ed_nama = getActivity().findViewById(R.id.ed_addnama);
        ed_jenis = getActivity().findViewById(R.id.ed_addjenis);
        ed_stok =  getActivity().findViewById(R.id.ed_addstok);

        clear();

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cek();
            }

        });
    }

    private void deklarasi() {
        nama = ed_nama.getEditText().getText().toString();
        jenis = ed_jenis.getEditText().getText().toString();
        stok = ed_stok.getEditText().getText().toString();
    }

    private void cek() {
        deklarasi();
        if(nama.equals("") || jenis.equals("") || stok.equals("") || !stok.matches("[0-9]+")){

            if(nama.equals("")){
                ed_nama.getEditText().setError("Masukkan Nama barang");
            }

            if(jenis.equals("")){
                ed_jenis.getEditText().setError("Masukkan Jenis barang");
            }

            if(stok.equals("") || !stok.matches("[0-9]+")){
                ed_stok.getEditText().setError("Masukkan Stok barang hanya angka");
            }
        }
        else{
            try {
                DatabaseAdapter dbadapter = new DatabaseAdapter(getActivity());
                dbadapter.open();

                if (dbadapter.tambah_barang(nama, jenis, stok)) {
                    Toast.makeText(getActivity(), "Create Data", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Barang "+nama +" Telah ditambahkan"
                            , Toast.LENGTH_LONG).show();

                }

                dbadapter.close();
            } catch (Exception e) {
                // TODO: handle exception
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }


    private void clear() {
        ed_nama.getEditText().setText("");
        ed_jenis.getEditText().setText("");
        ed_stok.getEditText().setText("");

        ed_nama.getEditText().setError(null);
        ed_jenis.getEditText().setError(null);
        ed_stok.getEditText().setError(null);
    }
}